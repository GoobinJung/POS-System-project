$ErrorActionPreference = "Stop"
Push-Location $PSScriptRoot
try {
    $sources = Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { $_.FullName }
    javac -encoding UTF-8 -d out $sources
    if ($LASTEXITCODE -ne 0) { throw "Compilation failed." }
    java -cp out posTest.RegressionTest
    if ($LASTEXITCODE -ne 0) { throw "Business regression checks failed." }
    java -cp out posUi.WorkflowTest
    if ($LASTEXITCODE -ne 0) { throw "Swing workflow checks failed." }
} finally {
    Pop-Location
}
