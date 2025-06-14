#!/bin/bash

echo "🔍 Starte Vergleich deiner lokalen Änderungen mit GitHub..."

# Stelle sicher, dass du im richtigen Git-Repo bist
if ! git rev-parse --is-inside-work-tree &> /dev/null; then
    echo "❌ Dieses Verzeichnis ist kein Git-Repository."
    exit 1
fi

# Aktuelle Änderungen im Arbeitsverzeichnis?
if [[ -n $(git status --porcelain) ]]; then
    echo "⚠️ Es gibt lokale Änderungen, die noch nicht committed sind:"
    git status --short
else
    echo "✅ Keine lokalen Änderungen – Arbeitsverzeichnis ist sauber."
fi

# Abrufen der neuesten Remote-Daten
git fetch origin

# Unterschiede zwischen lokalem HEAD und Remote-Branch (main oder master)
BRANCH=$(git symbolic-ref --short HEAD)

echo ""
echo "🔄 Prüfe Unterschiede zum Remote-Zweig origin/$BRANCH..."

LOCAL=$(git rev-parse $BRANCH)
REMOTE=$(git rev-parse origin/$BRANCH)

if [ "$LOCAL" = "$REMOTE" ]; then
    echo "🎉 Alles ist aktuell – keine Unterschiede zum Remote-Zweig auf GitHub."
else
    echo "⚠️ Deine lokale Version unterscheidet sich vom Remote:"
    echo "→ Lokaler Commit: $LOCAL"
    echo "→ Remote-Commit:  $REMOTE"
    echo ""
    echo "📝 Liste der Unterschiede:"
    git log --oneline $REMOTE..$LOCAL
fi
