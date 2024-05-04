class CelulaDupla {
	public int elemento;
	public CelulaDupla ant;
	public CelulaDupla prox;

	public CelulaDupla() {
		this(0);
	}

	public CelulaDupla(int elemento) {
		this.elemento = elemento;
		this.ant = this.prox = null;
	}
}
