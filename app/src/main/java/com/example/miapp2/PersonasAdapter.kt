package com.example.miapp2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PersonasAdapter (
    private val personas: List<Persona>,
    private val editarClickListener: (Persona) -> Unit,
    private val eliminarClickListener: (Persona) -> Unit

    )
    : RecyclerView.Adapter<PersonasAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewId: TextView = itemView.findViewById(R.id.textViewId)
        val textViewNombre: TextView = itemView.findViewById(R.id.textViewNombre)
        val textViewCorreo: TextView = itemView.findViewById(R.id.textViewCorreo)
        val btnEditar: Button = itemView.findViewById(R.id.btnEditar)
        val btnEliminar: Button = itemView.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val persona = personas[position]

        // Configurar los TextView con los datos de la persona
        holder.textViewId.text = persona.id.toString()
        holder.textViewNombre.text = persona.nombre
        holder.textViewCorreo.text = persona.email

        // Configurar clics en los botones de editar y eliminar
        holder.btnEditar.setOnClickListener { editarClickListener(persona) }
        holder.btnEliminar.setOnClickListener { eliminarClickListener(persona) }
    }

    override fun getItemCount(): Int {
        return personas.size
    }
}