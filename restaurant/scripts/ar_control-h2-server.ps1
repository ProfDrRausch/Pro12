# PowerShell Script: H2-Server Control Center
# Dieses Skript in das Installationsverzeichnis des H2-Servers legen und mit PowerShell
# als Adminstator ausführen. 
function Show-Menu {
    Clear-Host
    Write-Host ""
    Write-Host "╔═══════════════════════════════════════╗"
    Write-Host "║    🧪 H2-Server Control Center        ║"
    Write-Host "╠═══════════════════════════════════════╣"
    Write-Host "║ 1. ▶️  H2-Server starten              ║"
    Write-Host "║ 2. 🔍  H2-Server-Status anzeigen      ║"
    Write-Host "║ 3. ⛔  H2-Server beenden              ║"
    Write-Host "║ 4. 🚪  Beenden                        ║"
    Write-Host "╚═══════════════════════════════════════╝"
}

function Start-H2Server {
    Write-Host "`n🚀 Prüfe, ob bereits ein H2-Server läuft..."
    $exists = Get-CimInstance Win32_Process | Where-Object {
        $_.CommandLine -match "h2.*\.jar" -or $_.CommandLine -match "org\.h2\.tools\.Server"
    }

    if ($exists.Count -gt 0) {
        Write-Host "`n⚠️ Ein H2-Server-Prozess läuft bereits:"
        $exists | ForEach-Object {
            Write-Host "   - PID: $($_.ProcessId)"
        }
        Write-Host "`nℹ️ Kein weiterer Startvorgang erforderlich."
    }
    else {
        Write-Host "`n🚀 Starte H2-Server im Hintergrund..."
        try {
            Start-Process -FilePath "java.exe" `
                -ArgumentList '-cp "h2-2.3.232.jar" org.h2.tools.Server -tcp -tcpAllowOthers -tcpPort 9092 -web -webPort 8082' `
                -WindowStyle Hidden -ErrorAction Stop
            Start-Sleep -Seconds 2
            Write-Host "✅ H2-Server wurde gestartet.`n"
        }
        catch {
            Write-Host "`n❌ Fehler beim Starten des H2-Servers:"
            Write-Host "   $($_.Exception.Message)"
        }
    }

    Read-Host "`n[Drücke eine Taste zum Fortfahren]"
}

function Get-H2Server {
    $processes = Get-CimInstance Win32_Process | Where-Object {
        $_.CommandLine -match "h2.*\.jar" -or $_.CommandLine -match "org\.h2\.tools\.Server"
    }

    if ($processes.Count -gt 0) {
        Write-Host "`n📡 Aktive H2-Prozesse:"
        $processes | ForEach-Object {
            Write-Host "   - PID: $($_.ProcessId)"
        }
    }
    else {
        Write-Host "`n❌ Kein laufender H2-Server-Prozess erkannt."
    }

    # 🌐 Portprüfung für TCP-Port 9092
    Write-Host "`n🌐 Prüfe, ob Port 9092 erreichbar ist..."
    try {
        $port = Test-NetConnection -ComputerName localhost -Port 9092
        if ($port.TcpTestSucceeded) {
            Write-Host "✅ Port 9092 ist geöffnet (H2 erreichbar)."
        } else {
            Write-Host "🕳️ Port 9092 ist geschlossen."
        }
    } catch {
        Write-Host "⚠️ Netzwerkprüfung fehlgeschlagen: $($_.Exception.Message)"
    }

    Read-Host "`n[Drücke eine Taste zum Fortfahren]"
}

function Stop-H2Server {
    $processes = Get-CimInstance Win32_Process | Where-Object {
        $_.CommandLine -match "h2.*\.jar" -or $_.CommandLine -match "org\.h2\.tools\.Server"
    }

    if ($processes.Count -gt 0) {
        foreach ($proc in $processes) {
            Write-Host "`n🛑 Beende H2-Server – PID: $($proc.ProcessId)"
            Stop-Process -Id $proc.ProcessId -Force
        }
        Write-Host "`n✅ Alle H2-Prozesse wurden beendet."
    } else {
        Write-Host "`nℹ️ Kein laufender H2-Server-Prozess gefunden."
    }
    Read-Host "`n[Drücke eine Taste zum Fortfahren]"
}

# Hauptmenü-Schleife
$running = $true
while ($running) {
    Show-Menu
    $choice = Read-Host "`nWähle eine Option [1–4]"

    switch ($choice) {
        "1" { Start-H2Server }
        "2" { Get-H2Server }
        "3" { Stop-H2Server }
        "4" {
            Write-Host "`n👋 Auf Wiedersehen!"
            $running = $false
        }
        default {
            Write-Host "`n⚠️ Ungültige Eingabe. Bitte 1–4 wählen."
            Start-Sleep -Seconds 2
        }
    }
}

