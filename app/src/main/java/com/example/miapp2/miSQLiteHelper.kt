package com.example.miapp2

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class Persona(val id: Int, val nombre: String, val email: String)
class miSQLiteHelper(context: Context) : SQLiteOpenHelper(
    context, "personas.db", null, 1) {

    companion object {
        val NOMBRE_TABLA = "personas"
        val CAMPO_ID = "_id"
        val CAMPO_NOMBRE = "nombre"
        val CAMPO_EMAIL = "email"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val ordenCreacion = "CREATE TABLE $NOMBRE_TABLA " +
                "($CAMPO_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$CAMPO_NOMBRE TEXT, $CAMPO_EMAIL TEXT)"
        db!!.execSQL(ordenCreacion)
    }

    override fun onUpgrade(db: SQLiteDatabase?,
                           oldVersion: Int, newVersion: Int) {
        val ordenBorrado = "DROP TABLE IF EXISTS $NOMBRE_TABLA"
        db!!.execSQL(ordenBorrado)
        onCreate(db)
    }

    fun saveDate(nombre: String, email: String) {
        val datos = ContentValues()
        datos.put(CAMPO_NOMBRE, nombre)
        datos.put(CAMPO_EMAIL, email)

        val db = this.writableDatabase
        db.insert(NOMBRE_TABLA, null, datos)
        db.close()
    }
    fun getAllData(): List<Persona> {
        val listaPersonas = mutableListOf<Persona>()

        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $NOMBRE_TABLA", null)

        while (cursor.moveToNext()) {
            val idIndex = cursor.getColumnIndex(CAMPO_ID)
            val nombreIndex = cursor.getColumnIndex(CAMPO_NOMBRE)
            val emailIndex = cursor.getColumnIndex(CAMPO_EMAIL)

            val id = if (idIndex != -1) cursor.getInt(idIndex) else -1
            val nombre = if (nombreIndex != -1) cursor.getString(nombreIndex) else ""
            val email = if (emailIndex != -1) cursor.getString(emailIndex) else ""

            val persona = Persona(id, nombre, email)
            listaPersonas.add(persona)
        }

        cursor.close()
        db.close()

        return listaPersonas
    }

    fun updatePersona(id: Int, nombre: String, email: String): Int {
        val datos = ContentValues()
        datos.put(CAMPO_NOMBRE, nombre)
        datos.put(CAMPO_EMAIL, email)

        val db = this.writableDatabase
        return db.update(
            NOMBRE_TABLA,
            datos,
            "$CAMPO_ID = ?",
            arrayOf(id.toString())
        )
    }

    fun deletePersona(id: Int): Int {
        val db = this.writableDatabase
        val result = db.delete(NOMBRE_TABLA, "$CAMPO_ID = ?", arrayOf(id.toString()))
        db.close()
        return result
    }
}