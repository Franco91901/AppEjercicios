package com.example.appejercicios.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AppDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "freefit.db", null, 3) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE usuario (
                id_usuario INTEGER PRIMARY KEY AUTOINCREMENT,
                correo TEXT NOT NULL UNIQUE,
                contrasenia TEXT NOT NULL,
                nombres TEXT NOT NULL,
                apellidos TEXT NOT NULL,
                sexo TEXT NOT NULL,
                edad INTEGER,
                peso REAL,
                altura REAL,
                rol TEXT NOT NULL
            );
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE rutina (
                id_rutina INTEGER PRIMARY KEY AUTOINCREMENT,
                id_usuario INTEGER NOT NULL,
                nombre_rutina TEXT NOT NULL,
                descripcion TEXT,
                FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
            );
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE dia_rutina (
                id_dia_rutina INTEGER PRIMARY KEY AUTOINCREMENT,
                id_rutina INTEGER NOT NULL,
                dia_semana INTEGER NOT NULL,
                FOREIGN KEY (id_rutina) REFERENCES rutina(id_rutina)
            );
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE ejercicio (
                id_ejercicio INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                descripcion TEXT,
                tipo_medida TEXT NOT NULL,
                dificultad TEXT NOT NULL
            );
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE dia_ejercicio (
                id_dia_ejercicio INTEGER PRIMARY KEY AUTOINCREMENT,
                id_dia_rutina INTEGER NOT NULL,
                id_ejercicio INTEGER NOT NULL,
                series INTEGER NOT NULL,
                repeticiones INTEGER,
                duracion_segundos INTEGER,
                descanso_serie INTEGER NOT NULL,
                orden_ejercicio INTEGER NOT NULL,
                FOREIGN KEY (id_dia_rutina) REFERENCES dia_rutina(id_dia_rutina),
                FOREIGN KEY (id_ejercicio) REFERENCES ejercicio(id_ejercicio)
            );
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE categoria (
                id_categoria INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL UNIQUE
            );
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE ejercicio_categoria (
                id_ejercicio INTEGER NOT NULL,
                id_categoria INTEGER NOT NULL,
                PRIMARY KEY (id_ejercicio, id_categoria),
                FOREIGN KEY (id_ejercicio) REFERENCES ejercicio(id_ejercicio),
                FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria)
            );
        """.trimIndent())

        // Usuarios iniciales
        db.execSQL("""
            INSERT OR IGNORE INTO usuario (correo, contrasenia, nombres, apellidos, sexo, edad, peso, altura, rol)
            VALUES
            ('admin@gmail.com', '1234', 'Admin', 'Principal', 'M', 25, 70.5, 1.75, 'ADMIN'),
            ('user@gmail.com', 'abcd', 'Usuario', 'Basico', 'F', 22, 60.0, 1.65, 'BASICO');
        """.trimIndent())

        // Categorías iniciales
        db.execSQL("""
            INSERT OR IGNORE INTO categoria (id_categoria, nombre) VALUES
            (1, 'Pecho'), (2,'Brazos'), (3,'Espalda'),
            (4,'Piernas'), (5,'Abdominales'), (6,'Cardio');
        """.trimIndent())

        // Ejercicio “Descanso” con id=0
        db.execSQL("""
            INSERT OR IGNORE INTO ejercicio (id_ejercicio, nombre, descripcion, tipo_medida, dificultad)
            VALUES (0, 'Descanso', NULL, 'TIEMPO', 'FACIL');
        """.trimIndent())

        // Resto de ejercicios
        db.execSQL("""
            INSERT OR IGNORE INTO ejercicio (nombre, descripcion, tipo_medida, dificultad) VALUES
            ('Flexiones', 'Ejercicio clásico que trabaja pecho, hombros y tríceps.', 'AMBAS', 'FACIL'),
            ('Flexiones declinadas', 'Flexiones con pies elevados para mayor intensidad.', 'AMBAS', 'MEDIA'),
            ('Flexiones con palmada', 'Variante explosiva de flexiones para potencia y coordinación.', 'AMBAS', 'DIFICIL'),
            ('Curl de bíceps con botella', 'Ejercicio de bíceps usando botellas o mancuernas caseras.', 'REPETICIONES', 'FACIL'),
            ('Fondos en silla', 'Trabaja tríceps y hombros usando una silla estable.', 'REPETICIONES', 'MEDIA'),
            ('Flexiones diamante', 'Flexiones cerradas centradas en tríceps y pecho interno.', 'AMBAS', 'DIFICIL'),
            ('Superman', 'Extiende brazos y piernas para fortalecer la espalda baja.', 'TIEMPO', 'FACIL'),
            ('Remo invertido', 'Tira del cuerpo hacia una superficie para espalda media.', 'REPETICIONES', 'MEDIA'),
            ('Dominadas', 'Ejercicio exigente para espalda y brazos.', 'REPETICIONES', 'DIFICIL'),
            ('Sentadillas', 'Ejercicio base para piernas y glúteos.', 'AMBAS', 'FACIL'),
            ('Zancadas', 'Fortalece cuádriceps, glúteos y equilibrio.', 'AMBAS', 'MEDIA'),
            ('Sentadillas con salto', 'Ejercicio explosivo para fuerza y potencia.', 'AMBAS', 'DIFICIL'),
            ('Abdominales', 'Clásico ejercicio para abdomen superior.', 'AMBAS', 'FACIL'),
            ('Plancha', 'Isométrico para core, espalda y hombros.', 'TIEMPO', 'MEDIA'),
            ('Elevaciones de piernas', 'Fortalece abdomen inferior y flexores de cadera.', 'REPETICIONES', 'DIFICIL'),
            ('Jumping Jacks', 'Ejercicio cardiovascular para calentar.', 'TIEMPO', 'FACIL'),
            ('Mountain Climbers', 'Cardio que involucra core y piernas.', 'TIEMPO', 'MEDIA'),
            ('Burpees', 'Ejercicio de cuerpo completo y alta intensidad.', 'AMBAS', 'DIFICIL'),
            ('Escaladores cruzados', 'Versión cruzada de mountain climbers para abdomen y cardio.', 'TIEMPO', 'MEDIA'),
            ('Puente de glúteos', 'Fortalece glúteos, espalda baja y piernas.', 'REPETICIONES', 'FACIL'),
            ('Shadow Boxing', 'Cardio de boxeo al aire, mejora resistencia y coordinación.', 'TIEMPO', 'MEDIA');
        """.trimIndent())

        // Relaciones ejercicio-categoría
        db.execSQL("""
            INSERT OR IGNORE INTO ejercicio_categoria (id_ejercicio, id_categoria) VALUES
            (1,1),(1,2),(2,1),(2,2),(3,1),(3,2),(4,2),(5,2),(6,2),(6,1),
            (7,3),(8,3),(9,3),(9,2),(10,4),(11,4),(12,4),(20,4),(12,6),
            (13,5),(14,5),(15,5),(19,5),(16,6),(17,6),(18,6),(19,6),(21,6),(18,4);
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ejercicio_categoria;")
        db.execSQL("DROP TABLE IF EXISTS dia_ejercicio;")
        db.execSQL("DROP TABLE IF EXISTS categoria;")
        db.execSQL("DROP TABLE IF EXISTS dia_rutina;")
        db.execSQL("DROP TABLE IF EXISTS rutina;")
        db.execSQL("DROP TABLE IF EXISTS ejercicio;")
        db.execSQL("DROP TABLE IF EXISTS usuario;")
        onCreate(db)
    }
}
