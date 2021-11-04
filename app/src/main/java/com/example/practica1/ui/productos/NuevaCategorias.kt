package com.example.practica1.ui.productos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.practica1.R
import com.example.practica1.ui.categoria.CustomView

class NuevaCategorias(val datos:Array<DatosProducto.datosCategoria>): RecyclerView.Adapter<CustomView2>() {
    override fun getItemCount(): Int {
        return datos.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomView2 {
        var layoutInflater = LayoutInflater.from(parent?.context)
        var cellForRow = layoutInflater.inflate(R.layout.fragment_seleccion_categoria, parent, false)
        return CustomView2(cellForRow)
    }


    override fun onBindViewHolder(holder: CustomView2, position: Int) {

        var nombre = holder?.itemView.findViewById(R.id.txtNombre) as TextView
        nombre.text = datos[position].nomCate

    }
}

class CustomView2(varV: View): RecyclerView.ViewHolder(varV){

}