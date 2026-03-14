const express = require("express");
const bcrypt = require("bcryptjs");
const sqlite3 = require("sqlite3").verbose();
const cors = require("cors");
const bodyParser = require("body-parser");
const path = require("path");

const app = express();

// SQLite
const dbPath = path.join(__dirname, "site.db");
const db = new sqlite3.Database(dbPath, (err) => {
   if (err) {
      console.error("Error al conectar a la base de datos:", err);
   } else {
      console.log("Conectado a la base de datos SQLite");
   }
});

// Middleware
app.use(cors());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

// Crea tabla de usuarios
db.serialize(() => {
   db.run(`
    CREATE TABLE IF NOT EXISTS User (
      id INTEGER PRIMARY KEY AUTOINCREMENT,
      username TEXT UNIQUE NOT NULL,
      password TEXT NOT NULL
    )
  `);
});

// Endpoint raíz
app.get("/", (req, res) => {
   res.json({ message: "API Funcionando" });
});

// Endpoint de REGISTRO
app.post("/register", (req, res) => {
   const { username, password } = req.body;

   if (!username || !password) {
      return res
         .status(400)
         .json({ message: "Username y password son requeridos" });
   }

   db.get("SELECT * FROM User WHERE username = ?", [username], (err, row) => {
      if (err) {
         return res.status(500).json({ message: "Error en la base de datos" });
      }

      if (row) {
         return res.status(400).json({ message: "El usuario ya existe" });
      }

      // Encriptar contraseña
      bcrypt.hash(password, 10, (err, hashedPassword) => {
         if (err) {
            return res
               .status(500)
               .json({ message: "Error al encriptar la contraseña" });
         }

         // Crear y guardar nuevo usuario
         db.run(
            "INSERT INTO User (username, password) VALUES (?, ?)",
            [username, hashedPassword],
            function (err) {
               if (err) {
                  return res
                     .status(500)
                     .json({ message: "Error al crear el usuario" });
               }

               res.status(201).json({ message: "Usuario creado exitosamente" });
            },
         );
      });
   });
});

// Endpoint de LOGIN
app.post("/login", (req, res) => {
   const { username, password } = req.body;

   if (!username || !password) {
      return res.status(400).json({
         status: "error",
         message: "Username y password son requeridos",
      });
   }

   db.get("SELECT * FROM User WHERE username = ?", [username], (err, user) => {
      if (err) {
         return res.status(500).json({
            status: "error",
            message: "Error en la base de datos",
         });
      }

      if (!user) {
         return res.status(401).json({
            status: "error",
            message: "Credenciales inválidas",
         });
      }

      // Comparar contraseña con el hash
      bcrypt.compare(password, user.password, (err, isMatch) => {
         if (err) {
            return res.status(500).json({
               status: "error",
               message: "Error al verificar la contraseña",
            });
         }

         if (isMatch) {
            return res.status(200).json({
               status: "success",
               message: "Login exitoso",
               user_id: user.id,
               username: user.username,
            });
         } else {
            return res.status(401).json({
               status: "error",
               message: "Credenciales inválidas",
            });
         }
      });
   });
});

// Iniciar servidor
const PORT = 5000;
app.listen(PORT, "0.0.0.0", () => {
   console.log(`Servidor ejecutándose en http://0.0.0.0:${PORT}`);
});

// Cerrar la base de datos al terminar
process.on("SIGINT", () => {
   db.close(() => {
      console.log("Conexión a la base de datos cerrada");
      process.exit(0);
   });
});
