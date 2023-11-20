package com.example.miapp2

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.miapp2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bdHelper : miSQLiteHelper
    private val ADD_EDIT_REQUEST_CODE = 1
    private lateinit var recyclerView: RecyclerView
    private lateinit var personasAdapter: PersonasAdapter

    companion object {
        const val EXTRA_PERSONA_ID = "extra_persona_id"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bdHelper = miSQLiteHelper(this)
        val personaId = intent.getIntExtra(EXTRA_PERSONA_ID, -1)

        // Configurar el evento click del botón "Agregar Nuevo"
        binding.btnAgregarNuevo.setOnClickListener {
            val intent = Intent(this, AddEditActivity::class.java)
            startActivityForResult(intent, ADD_EDIT_REQUEST_CODE)
        }

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        personasAdapter = PersonasAdapter(emptyList(),
            { persona -> editarPersona(persona) },
            { persona -> eliminarPersona(persona) })
        recyclerView.adapter = personasAdapter

        //LLamar a la función para cargar los registros al iniciar la aplicación
        cargarRegistros();
    }

    private fun editarPersona(persona: Persona) {
        // Iniciar AddEditActivity con datos de la persona para la edición
        val intent = Intent(this, AddEditActivity::class.java)
        intent.putExtra(AddEditActivity.EXTRA_PERSONA_ID, persona.id)
        intent.putExtra(AddEditActivity.EXTRA_NOMBRE, persona.nombre)
        intent.putExtra(AddEditActivity.EXTRA_CORREO, persona.email)
        startActivityForResult(intent, ADD_EDIT_REQUEST_CODE)
    }

    private fun eliminarPersona(persona: Persona) {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmar Eliminación")
        builder.setMessage("¿Estás seguro de que deseas eliminar a ${persona.nombre}?")

        // Configurar los botones del cuadro de diálogo
        builder.setPositiveButton("Sí") { _, _ ->
            bdHelper.deletePersona(persona.id)
            cargarRegistros()
        }

        builder.setNegativeButton("No") { _, _ ->

        }

        // Mostrar el cuadro de diálogo
        val dialog = builder.create()
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_EDIT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            cargarRegistros()
        }
    }

    private fun cargarRegistros() {

        val registros = bdHelper.getAllData()

        // Actualizar el adaptador con la nueva lista de registros
        personasAdapter = PersonasAdapter(registros,
            { persona -> editarPersona(persona) },
            { persona -> eliminarPersona(persona) })
        recyclerView.adapter = personasAdapter
    }
}