#!/bin/bash

# Script de análisis rápido de APK para macOS
APK_FILE="$1"

if [ -z "$APK_FILE" ]; then
    echo "Uso: $0 <archivo.apk>"
    exit 1
fi

if [ ! -f "$APK_FILE" ]; then
    echo "Error: El archivo $APK_FILE no existe"
    exit 1
fi

echo "=== ANÁLISIS DE APK: $(basename "$APK_FILE") ==="
echo "Archivo: $APK_FILE"
echo "Fecha: $(date)"
echo

echo "1. INFORMACIÓN BÁSICA:"
echo "   Tamaño: $(ls -lh "$APK_FILE" | awk '{print $5}')"
echo "   Tipo: $(file "$APK_FILE")"
echo

echo "2. CONTENIDO PRINCIPAL:"
unzip -l "$APK_FILE" | grep -E "\.(dex|so|xml|png|jpg)$" | head -10
echo

echo "3. ESTRUCTURA DE DIRECTORIOS:"
unzip -l "$APK_FILE" | awk '{print $4}' | grep "/" | sed 's|/.*||' | sort | uniq -c | sort -nr
echo

echo "4. ARCHIVOS DEX (código):"
unzip -l "$APK_FILE" | grep "\.dex$"
echo

echo "5. LIBRERÍAS NATIVAS:"
unzip -l "$APK_FILE" | grep "\.so$"
echo

echo "6. MANIFEST (si está disponible aapt):"
if command -v aapt &> /dev/null; then
    echo "   - Información del paquete:"
    aapt dump badging "$APK_FILE" | head -5
    echo "   - Permisos principales:"
    aapt dump permissions "$APK_FILE" | head -10
else
    echo "   aapt no disponible. Para instalarlo:"
    echo "   brew install android-platform-tools"
fi

echo
echo "=== ANÁLISIS COMPLETADO ==="
