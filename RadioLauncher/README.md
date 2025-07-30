# Radio Launcher

Una aplicación launcher personalizada diseñada específicamente para dispositivos de radio Android (Android Auto, unidades principales de vehículos, etc.).

## Características

### 🎵 Radio FM/AM
- Sintonizador de radio FM y AM
- Presets guardados
- Búsqueda automática de estaciones
- Control de volumen y silencio
- Indicador de fuerza de señal

### 📱 Navegación Principal
- **Radio**: Control completo de radio FM/AM
- **Media**: Reproductor de música y audio
- **Apps**: Lista de aplicaciones instaladas
- **Teléfono**: Funciones de llamadas y contactos
- **Navegación**: GPS y mapas
- **Configuración**: Ajustes del sistema

### 🎨 Diseño
- Tema oscuro optimizado para uso nocturno
- Interfaz táctil amigable para uso en vehículos
- Botones grandes y legibles
- Animaciones suaves
- Orientación horizontal (landscape)

### 🔧 Funcionalidades del Sistema
- Información de tiempo y fecha en tiempo real
- Indicadores de conectividad (WiFi, Bluetooth)
- Información del clima
- Gestión de volumen del sistema

## Requisitos

- Android 5.0 (API 21) o superior
- Orientación landscape recomendada
- Pantalla táctil

## Instalación

1. Clona el repositorio
2. Abre el proyecto en Android Studio
3. Compila e instala en tu dispositivo Android
4. Configura como launcher predeterminado en Configuración > Apps > Apps predeterminadas > App de inicio

## Estructura del Proyecto

```
RadioLauncher/
├── app/
│   ├── src/main/
│   │   ├── java/com/radiolauncher/
│   │   │   ├── MainActivity.kt
│   │   │   ├── fragments/
│   │   │   ├── adapters/
│   │   │   ├── models/
│   │   │   ├── utils/
│   │   │   ├── service/
│   │   │   ├── receiver/
│   │   │   └── provider/
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   ├── values/
│   │   │   ├── drawable/
│   │   │   ├── mipmap/
│   │   │   └── anim/
│   │   └── AndroidManifest.xml
│   └── build.gradle
├── build.gradle
├── settings.gradle
└── gradle.properties
```

## Personalización

### Colores
Edita `res/values/colors.xml` para cambiar la paleta de colores.

### Temas
Modifica `res/values/themes.xml` para ajustar el tema visual.

### Presets de Radio
Los presets se pueden configurar en `RadioFragment.kt` o implementar persistencia con SharedPreferences/Database.

## Desarrollo Futuro

- [ ] Integración con hardware de radio real
- [ ] Reproductor de música completo
- [ ] Lista de aplicaciones con lanzamiento
- [ ] Integración con GPS
- [ ] Configuración de clima en tiempo real
- [ ] Soporte para Android Auto
- [ ] Base de datos para presets y configuraciones

## Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo LICENSE para detalles.

## Contribuciones

Las contribuciones son bienvenidas. Por favor:

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/NuevaCaracteristica`)
3. Commit tus cambios (`git commit -m 'Añadir nueva característica'`)
4. Push a la rama (`git push origin feature/NuevaCaracteristica`)
5. Abre un Pull Request

## Contacto

Para preguntas o sugerencias, por favor abre un issue en el repositorio.