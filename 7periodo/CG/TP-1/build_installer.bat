@echo off
setlocal
cd /d "%~dp0"

set "ISCC=%ProgramFiles(x86)%\Inno Setup 6\ISCC.exe"
if not exist "%ISCC%" set "ISCC=%ProgramFiles%\Inno Setup 6\ISCC.exe"

if not exist "dist\TP1-CG\TP1-CG.exe" (
    echo O executavel ainda nao existe. Execute build_windows.bat primeiro.
    exit /b 1
)

if not exist "%ISCC%" (
    echo Inno Setup 6 nao foi encontrado no caminho padrao.
    echo Abra installer.iss manualmente no Inno Setup e escolha Compile.
    exit /b 1
)

"%ISCC%" installer.iss
if errorlevel 1 exit /b 1

echo Instalador criado em installer_saida\TP1-CG-Setup.exe

