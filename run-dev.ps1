# ──────────────────────────────────────────────
#  run-dev.ps1
#  Carga variables de .env en la sesión actual de PowerShell
#  y arranca Gestix con `mvn spring-boot:run`.
#
#  Uso:   .\run-dev.ps1
# ──────────────────────────────────────────────

$ErrorActionPreference = 'Stop'
$envFile = Join-Path $PSScriptRoot '.env'

if (-not (Test-Path $envFile)) {
    Write-Host "[!] No existe .env en $PSScriptRoot" -ForegroundColor Red
    Write-Host "    Copia .env.example a .env y completa los valores." -ForegroundColor Yellow
    exit 1
}

Write-Host "[*] Cargando variables de $envFile ..." -ForegroundColor Cyan
$loaded = 0
Get-Content $envFile | ForEach-Object {
    $line = $_.Trim()
    if ($line -and -not $line.StartsWith('#') -and $line -match '^([^=]+)=(.*)$') {
        $name  = $matches[1].Trim()
        $value = $matches[2].Trim()
        # Quitar comillas externas si las hay
        if ($value -match '^"(.*)"$' -or $value -match "^'(.*)'$") {
            $value = $matches[1]
        }
        [Environment]::SetEnvironmentVariable($name, $value, 'Process')
        $loaded++
    }
}
Write-Host "[+] $loaded variables cargadas." -ForegroundColor Green
Write-Host "[*] Arrancando Spring Boot en http://localhost:$($env:SERVER_PORT) ..." -ForegroundColor Cyan
Write-Host ""

mvn spring-boot:run
