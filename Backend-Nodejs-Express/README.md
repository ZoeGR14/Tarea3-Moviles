# Backend - API de Autenticación con Node.js/Express

API RESTful completa de autenticación (Login y Registro) desarrollada con Node.js, Express y SQLite.

---

## ✨ Características

- ✅ **Registro de usuarios** con contraseña encriptada usando bcryptjs
- ✅ **Login con validación** de credenciales
- ✅ **Base de datos SQLite** para persistencia de datos
- ✅ **CORS habilitado** para permitir solicitudes desde aplicaciones móviles
- ✅ **Dockerizado** para fácil despliegue y portabilidad
- ✅ **Validación de entrada** en todos los endpoints
- ✅ **Manejo de errores** robusto y consistente
- ✅ **Servidor escalable** con Express.js

---

## 📋 Requisitos

### Instalación Local

- **Node.js** v18 o superior
- **npm** v9 o superior

### Con Docker

- **Docker** instalado y en funcionamiento

---

## 🚀 Guía de Instalación y Ejecución

### Opción 1: Ejecución Local

#### 1. Instalar dependencias

```bash
npm install
```

#### 2. Ejecutar en modo desarrollo

```bash
npm run dev
```

Este comando usa **nodemon** para reiniciar automáticamente el servidor cuando hay cambios en el código.

#### 3. Ejecutar en modo producción

```bash
npm start
```

El servidor estará disponible en:

- **Local**: `http://localhost:5000`
- **Red local** (desde otras máquinas): `http://0.0.0.0:5000`

### Opción 2: Ejecución con Docker

#### Usar Docker directamente

```bash
# Construir la imagen
docker build -t tarea3-backend .

# Ejecutar el contenedor
docker run -p 5000:5000 tarea3-backend
```

#### Usar Docker Compose (Recomendado)

```bash
# Iniciar los servicios
docker-compose up --build

# Detener los servicios
docker-compose down
```

El contenedor expondrá el puerto **5000** automáticamente.

---

## 🛠️ Dependencias

### Producción

- **express** (^4.18.2) - Framework web
- **sqlite3** (^5.1.6) - Driver de base de datos SQLite
- **bcryptjs** (^2.4.3) - Encriptación de contraseñas
- **body-parser** (^1.20.2) - Parseo de JSON
- **cors** (^2.8.5) - Middleware para CORS

### Desarrollo

- **nodemon** (^3.0.1) - Reinicio automático en desarrollo

---

## 🔐 Características de Seguridad

### Encriptación de Contraseñas

- Las contraseñas se encriptan usando **bcryptjs** con 10 rounds
- Se almacenan solo los hashes, nunca la contraseña en texto plano
- Cada contraseña tiene su propio salt único

### CORS

- Habilitado para permitir solicitudes desde cualquier origen
- Necesario para aplicaciones móviles que se ejecutan en emuladores

### Validación de Entrada

- Validación de campos requeridos
- Prevención de inyección SQL mediante parámetros preparados
- Respuestas HTTP apropiadas para diferentes escenarios de error

---

## 💾 Base de Datos

### Tabla de Usuarios (User)

```sql
CREATE TABLE IF NOT EXISTS User (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  username TEXT UNIQUE NOT NULL,
  password TEXT NOT NULL
)
```

**Campos**:

- `id`: Identificador único del usuario (auto-incrementado)
- `username`: Nombre de usuario (único, requerido)
- `password`: Contraseña encriptada (requerida)

La base de datos SQLite se crea automáticamente al ejecutar el servidor por primera vez.

---

## Endpoints

### 1. Health Check

```http
GET /
```

**Respuesta:**

```json
{
   "message": "API Funcionando"
}
```

### 2. Registro de Usuario

```http
POST /register
Content-Type: application/json

{
  "username": "usuario123",
  "password": "contraseña123"
}
```

**Respuesta exitosa (201):**

```json
{
   "message": "Usuario creado exitosamente"
}
```

**Errores:**

- 400: Usuario ya existe o parámetros faltantes
- 500: Error en el servidor

### 3. Login

```http
POST /login
Content-Type: application/json

{
  "username": "usuario123",
  "password": "contraseña123"
}
```

**Respuesta exitosa (200):**

```json
{
   "status": "success",
   "message": "Login exitoso",
   "user_id": 1,
   "username": "usuario123"
}
```

**Errores:**

- 401: Credenciales inválidas
- 400: Parámetros faltantes
- 500: Error en el servidor

## Ejemplos de uso con curl

```bash
# Verificar estado
curl http://localhost:5000/

# Registrar usuario
curl -X POST http://localhost:5000/register \
  -H "Content-Type: application/json" \
  -d '{"username":"user1","password":"pass123"}'

# Iniciar sesión
curl -X POST http://localhost:5000/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user1","password":"pass123"}'
```

---

## 📝 Scripts disponibles

```json
{
   "start": "node app.js",
   "dev": "nodemon app.js"
}
```

**npm start**: Ejecuta la aplicación en modo producción  
**npm run dev**: Ejecuta con nodemon para desarrollo automático

---

## 🔧 Configuración

### Puerto del servidor

Para cambiar el puerto, edita `app.js`:

```javascript
const PORT = 5000; // Cambiar este valor
app.listen(PORT, "0.0.0.0", () => {
   console.log(`Servidor ejecutándose en http://0.0.0.0:${PORT}`);
});
```

### Configuración CORS

Para restringir orígenes en CORS, modifica `app.js`:

```javascript
app.use(
   cors({
      origin: "http://mi-dominio.com",
      credentials: true,
   }),
);
```

---

## Notas

- Las contraseñas se encriptan con bcryptjs (10 salt rounds)
- La base de datos SQLite se crea automáticamente al iniciar
- CORS está habilitado para permitir peticiones desde otros dominios
- Los usuarios se almacenan con ID único y username único
