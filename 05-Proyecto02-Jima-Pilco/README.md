# 📌 Agenda Estudiantil  

Una aplicación móvil para la gestión de tareas con integración de Google Maps. Permite a los usuarios agregar, buscar, editar y eliminar tareas, asignándoles una ubicación específica en el mapa.  

## 📜 Descripción del Proyecto  

Agenda Estudiantil es una aplicación diseñada para ayudar a los estudiantes a organizar sus tareas diarias de manera eficiente. Con una interfaz intuitiva basada en **Material Design**, esta app ofrece una experiencia fluida y moderna.  

Los usuarios pueden:  
- Registrar tareas con título, descripción y fecha.  
- Asignar una ubicación en el mapa a cada tarea.  
- Buscar tareas fácilmente mediante un campo de búsqueda.  
- Editar o eliminar tareas con un menú de opciones emergente.  

### 🛠 Tecnologías Utilizadas  

- **Kotlin** para el desarrollo nativo en Android.  
- **Google Maps API** para la gestión de ubicaciones.  
- **SQLite** para el almacenamiento de tareas localmente.  
- **Material Design** para una interfaz atractiva y moderna.  

---

## 👥 Integrantes del Proyecto  

- **Mateo Pilco**  
- **Juan Jima**  

---

## 📂 Estructura del Proyecto  

```
📂 app/src/main
│── 📁 java/com/example/agendaestudiantil
│   ├── MainActivity.kt           # Pantalla principal con lista de tareas
│   ├── AddTaskActivity.kt        # Pantalla para agregar tareas
│   ├── EditTaskActivity.kt       # Pantalla para editar tareas
│   ├── SearchTaskActivity.kt     # Pantalla de búsqueda de tareas
│   ├── MapActivity.kt            # Gestión del mapa y ubicaciones
│   ├── TaskController.kt         # Controlador de lógica de tareas
│   ├── DBHelper.kt               # Base de datos SQLite
│   ├── TaskAdapter.kt            # Adaptador para la lista de tareas
│   ├── TaskOptionsBottomSheet.kt # Menú de opciones de tareas
│── 📁 res/layout
│   ├── activity_main.xml
│   ├── activity_add_task.xml
│   ├── activity_edit_task.xml
│   ├── activity_search_task.xml
│   ├── activity_map.xml
│   ├── bottom_sheet_task_options.xml
│   ├── task_item.xml
│── 📁 res/values
│   ├── colors.xml
│   ├── strings.xml
│   ├── themes.xml
│── 📁 res/drawable
│   ├── icon_add.xml
│   ├── icon_search.xml
```

---

## ✨ Características  

✅ **Gestión de Tareas**: Permite agregar, editar y eliminar tareas con detalles completos.  
✅ **Búsqueda en Tiempo Real**: Filtra tareas mientras escribes en el campo de búsqueda.  
✅ **Ubicación con Google Maps**: Asigna una ubicación a cada tarea y visualízala en el mapa.  
✅ **Marcadores con Nombre**: Muestra el nombre de la ubicación seleccionada sobre el marcador.  
✅ **Interfaz Moderna**: Diseño intuitivo y atractivo con **Material Design**.  

---

## 🚀 Instalación  

### 🔹 Requisitos Previos  

- Tener **Android Studio** instalado.  
- Contar con un dispositivo físico/emulador con Android **7.0 o superior**.  
- Tener una clave de **Google Maps API**.  

### 🔹 Pasos de Instalación  

1️⃣ Clona este repositorio:  
```bash
git clone https://github.com/tu-usuario/agenda-estudiantil.git
```
2️⃣ Abre el proyecto en **Android Studio**.  
3️⃣ Agrega tu clave de **Google Maps API** en `google_maps_api.xml`:  
```xml
<string name="google_maps_key">TU_API_KEY_AQUI</string>
```
4️⃣ Compila y ejecuta la aplicación en un emulador o dispositivo físico.  

---

## 📌 Uso de la Aplicación  

1️⃣ **Agregar una Tarea**  
- Pulsa el botón **"+"** en la pantalla principal.  
- Completa los campos: título, descripción y fecha.  
- Asigna una ubicación usando el mapa.  
- Guarda la tarea.  

2️⃣ **Buscar una Tarea**  
- Pulsa el botón de **búsqueda** 🔍.  
- Escribe el título de la tarea para filtrarla.  

3️⃣ **Editar o Eliminar una Tarea**  
- Mantén presionada una tarea para abrir el menú de opciones.  
- Selecciona **editar** para modificar los datos.  
- Selecciona **eliminar** para borrar la tarea.  

4️⃣ **Ver la Ubicación de una Tarea**  
- Si una tarea tiene ubicación, aparecerá un icono de **mapa**.  
- Pulsa el icono para ver la ubicación en **Google Maps**.  

---

## 🔗 Repositorio  

📌 **GitHub Repository**: [Agenda Estudiantil](https://github.com/Juan-Jima/2024b-mov-sw-gr2-jima-estrada-juan-jose/tree/main/05-Proyecto02-Jima-Pilco)  

---

## 📜 Licencia  

MIT © 2025 **Mateo Pilco & Juan Jima**  
