# Guía para analizar APK en macOS

## Instalación de herramientas:

### Usando Homebrew:
```bash
# Instalar Homebrew si no lo tienes
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

# Instalar herramientas necesarias
brew install android-platform-tools
brew install apktool
```

### Análisis básico con comandos:

1. **Información básica del APK:**
```bash
file app-release.apk
```

2. **Extraer contenido:**
```bash
unzip -l app-release.apk
```

3. **Ver manifiesto (requiere aapt):**
```bash
aapt dump badging app-release.apk
aapt dump permissions app-release.apk
```

4. **Usar apktool para decompilación:**
```bash
apktool d app-release.apk
```

### Script de análisis automático:
```bash
#!/bin/bash
APK_FILE="$1"

echo "=== Análisis de APK: $APK_FILE ==="
echo
echo "1. Información básica:"
file "$APK_FILE"
echo
echo "2. Tamaño del archivo:"
ls -lh "$APK_FILE"
echo
echo "3. Contenido del APK:"
unzip -l "$APK_FILE" | head -20
echo
echo "4. Manifiesto básico (si aapt está disponible):"
if command -v aapt &> /dev/null; then
    aapt dump badging "$APK_FILE"
else
    echo "aapt no está disponible. Instala android-platform-tools"
fi
```

