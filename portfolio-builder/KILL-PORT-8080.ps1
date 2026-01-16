Write-Host "========================================" -ForegroundColor Green
Write-Host "  Killing Process on Port 8080" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""

Write-Host "Checking for processes on port 8080..." -ForegroundColor Yellow
Write-Host ""

$connections = Get-NetTCPConnection -LocalPort 8080 -State Listen -ErrorAction SilentlyContinue

if ($connections) {
    Write-Host "Found process(es) on port 8080:" -ForegroundColor Red
    foreach ($conn in $connections) {
        $process = Get-Process -Id $conn.OwningProcess -ErrorAction SilentlyContinue
        if ($process) {
            Write-Host "  PID: $($conn.OwningProcess) - $($process.ProcessName)" -ForegroundColor Yellow
            Write-Host "  Killing process..." -ForegroundColor Yellow
            Stop-Process -Id $conn.OwningProcess -Force
            Write-Host "  ✓ Killed!" -ForegroundColor Green
        }
    }
    Write-Host ""
    Write-Host "Done! Port 8080 is now free." -ForegroundColor Green
} else {
    Write-Host "No process found on port 8080." -ForegroundColor Green
    Write-Host "Port is already free!" -ForegroundColor Green
}

Write-Host ""
Write-Host "You can now run ULTIMATE-FIX.bat" -ForegroundColor Cyan
Write-Host ""
Read-Host "Press Enter to exit"
