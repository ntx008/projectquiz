# Run script for PowerShell
$jdkPath = "C:\Program Files\Android\Android Studio\jbr\bin"
if (Test-Path "$jdkPath\java.exe") {
    $java = "$jdkPath\java.exe"
    $javac = "$jdkPath\javac.exe"
} else {
    $java = "java"
    $javac = "javac"
}

if (-not (Test-Path "out")) { New-Item -ItemType Directory -Path "out" | Out-Null }

Write-Host "Compiling Java sources..." -ForegroundColor Cyan
$sources = Get-ChildItem -Recurse -Filter *.java src | Select-Object -ExpandProperty FullName
& $javac -encoding UTF-8 -d out $sources

if ($LASTEXITCODE -eq 0) {
    Write-Host "Starting Java OOP Android Simulator on http://localhost:3000 ..." -ForegroundColor Green
    & $java -cp out com.quiz.oop.Main
} else {
    Write-Host "Compilation failed" -ForegroundColor Red
}
