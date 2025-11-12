"""
Tomasulo Algorithm Simulator (Python + Tkinter)
Educational simulator with Reorder Buffer and branch speculation.

How to run:
- Requires Python 3.8+
- Run: python tomasulo_simulator.py

Features:
- Parses simple assembly (LD, ST, ADD, SUB, MUL, DIV, BEQ, BNE, labels)
- Reservation Stations, Reorder Buffer (ROB), Functional Units
- 1-bit branch predictor (configurable)
- Step-by-step and auto-run modes
- Metrics: cycles, IPC, dispatched/completed instructions, bubbles, mispredictions
- GUI: program editor, control buttons, and tables showing RS, ROB, Registers, FUs, Memory

This is an educational implementation prioritizing clarity; not cycle-perfect against any existing microarchitecture model.
"""

import tkinter as tk
from tkinter import ttk, scrolledtext, messagebox
import threading
import time
import copy

# --------------------------- Parser & Program ---------------------------
DEFAULT_PROGRAM = """# Sample program
LD R1, 0
LD R2, 1
ADD R3, R1, R2
MUL R4, R3, R2
SUB R5, R4, R1
BEQ R5, R0, label1
ADD R6, R5, R5
label1: DIV R7, R6, R1
"""


def parse_program(text):
    lines = [ln.split('#')[0].strip() for ln in text.splitlines()]
    program = []
    labels = {}
    for ln in lines:
        if not ln:
            continue
        if ':' in ln:
            parts = ln.split(':')
            lbl = parts[0].strip()
            rest = ':'.join(parts[1:]).strip()
            labels[lbl] = len(program)
            if rest:
                program.append(rest)
        else:
            program.append(ln)
    insts = []
    for i, ln in enumerate(program):
        toks = [t.strip().strip(',') for t in ln.replace(',', ' ').split() if t.strip()]
        op = toks[0].upper()
        args = toks[1:]
        insts.append({'op': op, 'args': args, 'pc': i, 'raw': ln})
    # resolve label args
    for inst in insts:
        for i, a in enumerate(inst['args']):
            if a in labels:
                inst['args'][i] = str(labels[a])
    return insts

# --------------------------- Configuration ---------------------------
class Config:
    def __init__(self):
        self.num_registers = 8
        self.num_memory = 64
        self.reservation = {'ADD': 3, 'MUL': 2, 'LOAD': 3, 'STORE': 2, 'BRANCH': 2}
        self.latency = {'ADD': 2, 'SUB': 2, 'MUL': 10, 'DIV': 20, 'LD': 3, 'ST': 2, 'BRANCH': 1}
        self.rob_size = 16
        self.predictor = 'onebit'  # or 'static-not-taken'

# --------------------------- State & Helpers ---------------------------
class TomasuloState:
    def __init__(self, program, cfg: Config):
        self.cfg = cfg
        self.insts = program
        self.pc = 0
        self.cycle = 0
        self.regs = [0] * cfg.num_registers
        self.reg_status = [None] * cfg.num_registers  # ROB id or None
        self.memory = [0] * cfg.num_memory
        # Reservation Stations
        self.rs = {k: [None] * v for k, v in cfg.reservation.items()}
        # Functional Units (list of occupied RS entries)
        self.fu = {
            'ADD': [None, None],
            'MUL': [None, None],
            'LOAD': [None],
            'STORE': [None],
            'BRANCH': [None]
        }
        self.rob = []  # list of ROB entries
        self.predictor = {}  # pc -> bit
        self.dispatched = 0
        self.completed = 0
        self.bubbles = 0
        self.mispreds = 0

    def copy_for_debug(self):
        return copy.deepcopy(self)


def op_to_type(op):
    if op in ('ADD', 'SUB'):
        return 'ADD'
    if op in ('MUL', 'DIV'):
        return 'MUL'
    if op == 'LD':
        return 'LOAD'
    if op == 'ST':
        return 'STORE'
    if op in ('BEQ', 'BNE', 'J'):
        return 'BRANCH'
    return 'ADD'


def read_reg(arg):
    if arg.upper().startswith('R'):
        try:
            return int(arg[1:])
        except:
            return None
    return None

# Predictor

def predict(state: TomasuloState, pc):
    if state.cfg.predictor == 'static-not-taken':
        return False
    return bool(state.predictor.get(pc, False))


def update_predictor(state: TomasuloState, pc, taken):
    if state.cfg.predictor == 'static-not-taken':
        return
    state.predictor[pc] = bool(taken)

# --------------------------- Pipeline Stages ---------------------------

def find_free_rs(state: TomasuloState, t):
    set_rs = state.rs[t]
    for i, s in enumerate(set_rs):
        if s is None:
            return i
    return -1


def find_free_rob(state: TomasuloState):
    return 0 if len(state.rob) < state.cfg.rob_size else -1


def try_dispatch(state: TomasuloState):
    if state.pc >= len(state.insts):
        return False
    inst = state.insts[state.pc]
    op = inst['op']
    t = op_to_type(op)
    rs_idx = find_free_rs(state, t)
    rob_idx = find_free_rob(state)
    if rs_idx < 0 or rob_idx < 0:
        return False

    rob_entry = {'id': len(state.rob), 'inst': inst, 'dest': None, 'value': None, 'ready': False, 'pc': inst['pc'], 'branch': False}
    rs_entry = {'op': op, 'Vj': None, 'Vk': None, 'Qj': None, 'Qk': None, 'A': None, 'rob_id': rob_entry['id'], 'exec_rem': None}

    if op == 'LD':
        rd = read_reg(inst['args'][0])
        addr = int(inst['args'][1])
        rob_entry['dest'] = ('reg', rd)
        rs_entry['A'] = addr
        state.reg_status[rd] = rob_entry['id']
    elif op == 'ST':
        rsrc = read_reg(inst['args'][0])
        addr = int(inst['args'][1])
        rs_entry['A'] = addr
        q = state.reg_status[rsrc]
        if q is not None:
            rs_entry['Qj'] = q
        else:
            rs_entry['Vj'] = state.regs[rsrc]
        rob_entry['dest'] = ('mem', addr)
    elif op in ('ADD', 'SUB', 'MUL', 'DIV'):
        rd = read_reg(inst['args'][0])
        r1 = read_reg(inst['args'][1])
        r2 = read_reg(inst['args'][2])
        rob_entry['dest'] = ('reg', rd)
        q1 = state.reg_status[r1]
        q2 = state.reg_status[r2]
        if q1 is not None:
            rs_entry['Qj'] = q1
        else:
            rs_entry['Vj'] = state.regs[r1]
        if q2 is not None:
            rs_entry['Qk'] = q2
        else:
            rs_entry['Vk'] = state.regs[r2]
        state.reg_status[rd] = rob_entry['id']
    elif op in ('BEQ', 'BNE'):
        r1 = read_reg(inst['args'][0]); r2 = read_reg(inst['args'][1]); target = int(inst['args'][2])
        q1 = state.reg_status[r1]
        q2 = state.reg_status[r2]
        if q1 is not None:
            rs_entry['Qj'] = q1
        else:
            rs_entry['Vj'] = state.regs[r1]
        if q2 is not None:
            rs_entry['Qk'] = q2
        else:
            rs_entry['Vk'] = state.regs[r2]
        rs_entry['A'] = target
        rob_entry['branch'] = True
        # speculative: mark may set but we just track PC

    state.rob.append(rob_entry)
    state.rs[t][rs_idx] = rs_entry
    state.dispatched += 1

    # branch prediction
    if rob_entry.get('branch'):
        taken = predict(state, inst['pc'])
        if taken:
            state.pc = rs_entry['A']
        else:
            state.pc += 1
    else:
        state.pc += 1
    return True


def issue_execute(state: TomasuloState):
    # assign RS entries with ready operands to FU slots
    for t in state.rs.keys():
        for idx, r in enumerate(state.rs[t]):
            if r is None or r['exec_rem'] is not None:
                continue
            # ALU
            if t in ('ADD', 'MUL'):
                if r.get('Qj') is None and r.get('Qk') is None:
                    # find free FU
                    for fu_idx, slot in enumerate(state.fu[t]):
                        if slot is None:
                            state.fu[t][fu_idx] = r
                            r['exec_rem'] = state.cfg.latency.get(r['op'], 1)
                            break
            elif t == 'LOAD':
                for fu_idx, slot in enumerate(state.fu['LOAD']):
                    if slot is None:
                        state.fu['LOAD'][fu_idx] = r
                        r['exec_rem'] = state.cfg.latency['LD']
                        break
            elif t == 'STORE':
                # need Vj ready
                if r.get('Qj') is None:
                    for fu_idx, slot in enumerate(state.fu['STORE']):
                        if slot is None:
                            state.fu['STORE'][fu_idx] = r
                            r['exec_rem'] = state.cfg.latency['ST']
                            break
            elif t == 'BRANCH':
                if r.get('Qj') is None and r.get('Qk') is None:
                    for fu_idx, slot in enumerate(state.fu['BRANCH']):
                        if slot is None:
                            state.fu['BRANCH'][fu_idx] = r
                            r['exec_rem'] = state.cfg.latency['BRANCH']
                            break


def execute_cycle(state: TomasuloState):
    completed = []
    for t, slots in state.fu.items():
        for i, slot in enumerate(slots):
            if slot is None:
                continue
            slot['exec_rem'] -= 1
            if slot['exec_rem'] <= 0:
                # compute result
                op = slot['op']
                res = 0
                try:
                    if op == 'ADD': res = slot['Vj'] + slot['Vk']
                    elif op == 'SUB': res = slot['Vj'] - slot['Vk']
                    elif op == 'MUL': res = slot['Vj'] * slot['Vk']
                    elif op == 'DIV':
                        res = slot['Vj'] // slot['Vk'] if slot['Vk'] != 0 else 0
                    elif op == 'LD': res = state.memory[slot['A']]
                    elif op == 'ST': res = slot['Vj']
                    elif op == 'BEQ': res = (slot['Vj'] == slot['Vk'])
                    elif op == 'BNE': res = (slot['Vj'] != slot['Vk'])
                except Exception:
                    res = 0
                completed.append({'rs': slot, 'type': t, 'res': res})
                state.fu[t][i] = None
    # CDB broadcast: update ROB and any RS waiting for these ROB ids
    for comp in completed:
        rob_id = comp['rs']['rob_id']
        if rob_id < len(state.rob):
            state.rob[rob_id]['value'] = comp['res']
            state.rob[rob_id]['ready'] = True
        # update RSVs
        for t in state.rs.keys():
            for r in state.rs[t]:
                if r is None: continue
                if r.get('Qj') == rob_id:
                    r['Vj'] = comp['res']; r['Qj'] = None
                if r.get('Qk') == rob_id:
                    r['Vk'] = comp['res']; r['Qk'] = None


def commit_stage(state: TomasuloState):
    if not state.rob:
        return
    head = state.rob[0]
    if not head.get('ready'):
        return
    inst = head['inst']
    dest = head.get('dest')
    # commit register or memory
    # Find destination from earlier dispatch stage: we stored dest in rob entry when dispatching
    # For this simple design, we stored dest only for LD/ST/ALU at dispatch
    if dest:
        if dest[0] == 'reg':
            idx = dest[1]
            # only update reg if RAT still maps to this ROB id
            if state.reg_status[idx] == head['id']:
                state.regs[idx] = head['value']
                state.reg_status[idx] = None
        elif dest[0] == 'mem':
            addr = dest[1]
            state.memory[addr] = head['value']
    if head.get('branch'):
        taken = bool(head['value'])
        predicted = predict(state, head['pc'])
        if taken != predicted:
            # misprediction: flush speculative entries after this ROB entry
            state.mispreds += 1
            new_rob = [r for r in state.rob if r['id'] <= head['id']]
            # undo register status that pointed to flushed ROBs
            flushed_ids = [r['id'] for r in state.rob if r['id'] > head['id']]
            for fid in flushed_ids:
                fr = next((x for x in state.rob if x['id'] == fid), None)
                if fr and fr.get('dest') and fr['dest'][0] == 'reg':
                    ridx = fr['dest'][1]
                    if state.reg_status[ridx] == fr['id']:
                        state.reg_status[ridx] = None
            state.rob = new_rob
            # clear RS and FU entries with rob_id > head.id
            for t in state.rs.keys():
                for i, r in enumerate(state.rs[t]):
                    if r and r['rob_id'] > head['id']:
                        state.rs[t][i] = None
            for t in state.fu.keys():
                for i, f in enumerate(state.fu[t]):
                    if f and f['rob_id'] > head['id']:
                        state.fu[t][i] = None
            # update PC
            if taken:
                state.pc = int(head['inst']['args'][2])
            else:
                state.pc = head['pc'] + 1
            update_predictor(state, head['pc'], taken)
            # after flush we still remove the branch below
        else:
            update_predictor(state, head['pc'], taken)
    # pop head
    state.rob.pop(0)
    # renumber remaining ROB entries ids and fix references
    for i, r in enumerate(state.rob):
        old = r['id']
        if old != i:
            r['id'] = i
            # update RS and reg_status pointers
            for t in state.rs.keys():
                for rs in state.rs[t]:
                    if rs and rs.get('Qj') == old: rs['Qj'] = i
                    if rs and rs.get('Qk') == old: rs['Qk'] = i
            for ri in range(len(state.reg_status)):
                if state.reg_status[ri] == old:
                    state.reg_status[ri] = i
    state.completed += 1

# --------------------------- Single Cycle ---------------------------

def step_cycle(state: TomasuloState):
    state.cycle += 1
    dispatched = try_dispatch(state)
    if not dispatched:
        state.bubbles += 1
    issue_execute(state)
    execute_cycle(state)
    commit_stage(state)

# --------------------------- GUI ---------------------------

class SimulatorGUI:
    def __init__(self, root):
        self.root = root
        root.title('Tomasulo Simulator - Python')
        self.cfg = Config()
        self.program_text = DEFAULT_PROGRAM
        self.state = TomasuloState(parse_program(self.program_text), self.cfg)
        self.running = False
        self.build_ui()
        self.update_views()

    def build_ui(self):
        # Left: editor & controls
        left = ttk.Frame(self.root)
        left.grid(row=0, column=0, sticky='nsew')
        self.root.columnconfigure(0, weight=1)
        self.root.columnconfigure(1, weight=2)
        self.root.rowconfigure(0, weight=1)

        lbl = ttk.Label(left, text='Programa (Assembly)')
        lbl.pack(anchor='w')
        self.editor = scrolledtext.ScrolledText(left, width=40, height=20)
        self.editor.pack(fill='both', expand=True)
        self.editor.insert('1.0', self.program_text)

        btn_fr = ttk.Frame(left)
        btn_fr.pack(fill='x')
        ttk.Button(btn_fr, text='Reset', command=self.reset).pack(side='left')
        ttk.Button(btn_fr, text='Step', command=self.step).pack(side='left')
        ttk.Button(btn_fr, text='Run 10', command=lambda: self.run_n(10)).pack(side='left')
        self.auto_btn = ttk.Button(btn_fr, text='Auto', command=self.toggle_auto)
        self.auto_btn.pack(side='left')

        metrics = ttk.Frame(left)
        metrics.pack(fill='x', pady=6)
        self.metrics_var = tk.StringVar()
        ttk.Label(metrics, textvariable=self.metrics_var, justify='left').pack(anchor='w')

        # Right: displays
        right = ttk.Frame(self.root)
        right.grid(row=0, column=1, sticky='nsew')

        # ROB
        rob_fr = ttk.Labelframe(right, text='ROB')
        rob_fr.pack(fill='x')
        self.rob_tree = ttk.Treeview(rob_fr, columns=('id', 'inst', 'ready', 'dest', 'val'), show='headings', height=6)
        for c in ('id', 'inst', 'ready', 'dest', 'val'):
            self.rob_tree.heading(c, text=c)
        self.rob_tree.pack(fill='x')

        # RS and FUs
        bottom = ttk.Frame(right)
        bottom.pack(fill='both', expand=True)
        rs_fr = ttk.Labelframe(bottom, text='Reservation Stations')
        rs_fr.pack(side='left', fill='both', expand=True)
        self.rs_tabs = ttk.Notebook(rs_fr)
        self.rs_tabs.pack(fill='both', expand=True)
        self.rs_trees = {}
        for k in self.state.rs.keys():
            frame = ttk.Frame(self.rs_tabs)
            tree = ttk.Treeview(frame, columns=('idx', 'op', 'Vj', 'Vk', 'Qj', 'Qk', 'rob'), show='headings')
            for c in ('idx', 'op', 'Vj', 'Vk', 'Qj', 'Qk', 'rob'):
                tree.heading(c, text=c)
            tree.pack(fill='both', expand=True)
            self.rs_tabs.add(frame, text=k)
            self.rs_trees[k] = tree

        fu_fr = ttk.Labelframe(bottom, text='Functional Units')
        fu_fr.pack(side='left', fill='both')
        self.fu_tree = ttk.Treeview(fu_fr, columns=('type', 'slot', 'op', 'rem'), show='headings')
        for c in ('type', 'slot', 'op', 'rem'):
            self.fu_tree.heading(c, text=c)
        self.fu_tree.pack(fill='both', expand=True)

        # Registers
        reg_fr = ttk.Labelframe(right, text='Registers')
        reg_fr.pack(fill='x')
        self.reg_tree = ttk.Treeview(reg_fr, columns=('reg', 'val', 'rob'), show='headings', height=6)
        for c in ('reg', 'val', 'rob'):
            self.reg_tree.heading(c, text=c)
        self.reg_tree.pack(fill='x')

        # Memory (small view)
        mem_fr = ttk.Labelframe(right, text='Memory (0..15)')
        mem_fr.pack(fill='x')
        self.mem_tree = ttk.Treeview(mem_fr, columns=('addr', 'val'), show='headings', height=6)
        for c in ('addr', 'val'):
            self.mem_tree.heading(c, text=c)
        self.mem_tree.pack(fill='x')

    def reset(self):
        txt = self.editor.get('1.0', 'end').strip()
        if not txt:
            messagebox.showwarning('Aviso', 'Programa vazio')
            return
        prog = parse_program(txt)
        self.state = TomasuloState(prog, self.cfg)
        self.update_views()

    def step(self):
        step_cycle(self.state)
        self.update_views()

    def run_n(self, n):
        for _ in range(n):
            step_cycle(self.state)
        self.update_views()

    def toggle_auto(self):
        if not self.running:
            self.running = True
            self.auto_btn.config(text='Stop')
            threading.Thread(target=self.auto_loop, daemon=True).start()
        else:
            self.running = False
            self.auto_btn.config(text='Auto')

    def auto_loop(self):
        while self.running:
            step_cycle(self.state)
            self.root.after(1, self.update_views)
            time.sleep(0.25)

    def update_views(self):
        s = self.state
        ipc = f"{(s.completed / s.cycle) if s.cycle>0 else 0:.3f}"
        metrics = f"PC: {s.pc}  Cycles: {s.cycle}  Dispatched: {s.dispatched}  Completed: {s.completed}\nBubbles: {s.bubbles}  Mispreds: {s.mispreds}  IPC: {ipc}"
        self.metrics_var.set(metrics)

        # ROB
        for i in self.rob_tree.get_children(): self.rob_tree.delete(i)
        for r in s.rob:
            dest = ''
            if r.get('dest'):
                d = r['dest']
                if d[0] == 'reg': dest = f'R{d[1]}'
                else: dest = f'{d[0]}@{d[1]}'
            self.rob_tree.insert('', 'end', values=(r['id'], r['inst']['raw'], 'Y' if r.get('ready') else 'N', dest, r.get('value') if r.get('value') is not None else ''))

        # RS
        for k, tree in self.rs_trees.items():
            for i in tree.get_children(): tree.delete(i)
            for idx, r in enumerate(s.rs[k]):
                if r is None:
                    tree.insert('', 'end', values=(idx, '', '', '', '', '', ''))
                else:
                    tree.insert('', 'end', values=(idx, r.get('op',''), r.get('Vj',''), r.get('Vk',''), r.get('Qj',''), r.get('Qk',''), r.get('rob_id','')))

        # FU
        for i in self.fu_tree.get_children(): self.fu_tree.delete(i)
        for t, slots in s.fu.items():
            for idx, slot in enumerate(slots):
                if slot is None:
                    self.fu_tree.insert('', 'end', values=(t, idx, '', ''))
                else:
                    self.fu_tree.insert('', 'end', values=(t, idx, slot.get('op',''), slot.get('exec_rem','')))

        # Registers
        for i in self.reg_tree.get_children(): self.reg_tree.delete(i)
        for idx, val in enumerate(s.regs):
            robptr = s.reg_status[idx]
            self.reg_tree.insert('', 'end', values=(f'R{idx}', val, robptr if robptr is not None else ''))

        # Memory (0..15)
        for i in self.mem_tree.get_children(): self.mem_tree.delete(i)
        for addr in range(16):
            self.mem_tree.insert('', 'end', values=(addr, s.memory[addr]))

# --------------------------- Main ---------------------------

def main():
    root = tk.Tk()
    app = SimulatorGUI(root)
    root.mainloop()

if __name__ == '__main__':
    main()
