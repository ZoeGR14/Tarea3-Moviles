# Login API - Node.js/Express

API de autenticación con login y registro usando Node.js, Express y SQLite.

## Características

- ✅ Registro de usuarios con contraseña encriptada (bcryptjs)
- ✅ Login con validación de credenciales
- ✅ Base de datos SQLite
- ✅ Dockerizado para fácil despliegue
- ✅ CORS habilitado

## Requisitos

- Node.js 14+ (o usar Docker)
- npm

## Instalación Local

```bash
# Instalar dependencias
npm install

# Ejecutar en desarrollo (con nodemon)
npm run dev

# Ejecutar en producción
npm start
```

El servidor estará disponible en `http://localhost:5000`

## Instalación con Docker

```bash
# Construir imagen
docker build -t express-login-api .

# Ejecutar contenedor
docker run -p 5000:5000 express-login-api
```

O usar Docker Compose:

```bash
# Iniciar
docker-compose up

# Detener
docker-compose down
```

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
# Registro
curl -X POST http://localhost:5000/register \
  -H "Content-Type: application/json" \
  -d '{"username":"user1","password":"pass123"}'

# Login
curl -X POST http://localhost:5000/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user1","password":"pass123"}'
```

## Notas

- Las contraseñas se encriptan con bcryptjs (10 salt rounds)
- La base de datos SQLite se crea automáticamente al iniciar
- CORS está habilitado para permitir peticiones desde otros dominios
- Los usuarios se almacenan con ID único y username único
