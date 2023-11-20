package com.example.miapp2

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.miapp2.databinding.ActivityAddEditBinding

class AddEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditBinding
    private lateinit var bdHelper : miSQLiteHelper
    private var personaId: Int = -1

    companion object {
        const val EXTRA_PERSONA_ID = "extra_persona_id"
        const val EXTRA_NOMBRE = "extra_nombre"
        const val EXTRA_CORREO = "extra_correo"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btnGuardar: Button = binding.btnGuardar
        val btnRegresar: Button = binding.btnRegresar

        bdHelper = miSQLiteHelper(this)

        personaId = intent.getIntExtra(EXTRA_PERSONA_ID, -1)
        val nombre = intent.getStringExtra(EXTRA_NOMBRE) ?: ""
        val correo = intent.getStringExtra(EXTRA_CORREO) ?: ""

        if (personaId != -1) {
            cargarDatosPersona(personaId, nombre, correo)
        }

        btnGuardar.setOnClickListener {
            if (isValidInput()) {
                saveData()
                finish()
            } else {
                Toast.makeText(this, "Por favor, completa los campos correctamente", Toast.LENGTH_LONG).show()
            }
        }

        btnRegresar.setOnClickListener {
            finish()
        }
    }

    private fun cargarDatosPersona(personaId: Int, nombre:String, correo:String) {
        // Mostrar los datos de la persona en los campos de EditText
        binding.etId.setText(personaId.toString())
        binding.etNombre.setText(nombre)
        binding.etCorreo.setText(correo)
    }

    private fun isValidInput(): Boolean {
        val nombre = binding.etNombre.text.toString()
        val correo = binding.etCorreo.text.toString()

        return nombre.isNotBlank() && correo.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()
    }

    private fun saveData() {
        val nombre = binding.etNombre.text.toString()
        val correo = binding.etCorreo.text.toString()

        if (personaId == -1) {
            bdHelper.saveDate(nombre, correo)
        } else {
            bdHelper.updatePersona(personaId, nombre, correo)
        }

        binding.etNombre.text!!.clear()
        binding.etCorreo.text!!.clear()

        Toast.makeText(this, "Guardado", Toast.LENGTH_SHORT).show()

        // Establecer el resultado y cerrar la actividad
        setResult(Activity.RESULT_OK)

        finish()
    }
}