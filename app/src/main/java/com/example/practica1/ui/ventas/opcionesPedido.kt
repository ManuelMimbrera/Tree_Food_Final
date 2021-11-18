package com.example.practica1.ui.ventas

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.practica1.R

class opcionesPedido(val datos: Array<FinalizarVenta.datosCarrito>): RecyclerView.Adapter<CustomView>() {

    override fun getItemCount(): Int {
        return datos.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomView {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.fragment_pedidos, parent,false)
        return CustomView(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomView, position: Int) {

        var id = holder?.itemView.findViewById(R.id.IdP) as TextView
        var prod = holder?.itemView.findViewById(R.id.txtAlimento) as TextView
        var costo = holder?.itemView.findViewById(R.id.txtPrecio) as TextView
        var cantidad = holder?.itemView.findViewById<TextView>(R.id.txtCantidad)

        id.text = datos[position].id_producto.toString()
        prod.text = datos[position].nombre
        costo.text = datos[position].precio.toString()
        cantidad.text = datos[position].cantidad.toString()
    }
}