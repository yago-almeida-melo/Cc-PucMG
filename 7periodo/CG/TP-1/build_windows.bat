@echo off
setlocal
cd /d "%~dp0"

echo Instalando a ferramenta de empacotamento...
py -m pip install -r requirements-build.txt
if errorlevel 1 goto :erro

echo Executando os testes...
set "PYTHONPATH=%~dp0tp1"
py -m unittest discover -s tests -v
if errorlevel 1 goto :erro

echo Gerando o executavel...
py -m PyInstaller --noconfirm --clean TP1-CG.spec
if errorlevel 1 goto :erro

echo.
echo Executavel criado em dist\TP1-CG\TP1-CG.exe
exit /b 0

:erro
echo.
echo Nao foi possivel concluir a geracao. Consulte as mensagens acima.
exit /b 1

