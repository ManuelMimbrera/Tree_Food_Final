package com.example.practica1.ui.ventas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.practica1.R
import com.google.gson.Gson


class VentasAdapter(val datos: Array<DatosVenta.datosCarrito>): RecyclerView.Adapter<CustomView>() {

    override fun getItemCount(): Int {
        return datos.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomView {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.carrito_row_layout, parent,false)
        return CustomView(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomView, position: Int) {

        var eliminarVen = holder?.itemView.findViewById(R.id.btn_elim_car) as TextView
        var prod = holder?.itemView.findViewById(R.id.txt_producto) as TextView
        var descri = holder?.itemView.findViewById(R.id.txt_descripcion) as TextView
        var costo = holder?.itemView.findViewById(R.id.txt_costo) as TextView

        eliminarVen.setOnClickListener{
            val navController = holder?.itemView?.findNavController()

            var objJson = Gson()

            var datos = objJson.toJson(datos[position])

            val bundle = bundleOf("datosCompra" to datos)

            navController.navigate(R.id.nav_eliminar_ven, bundle)
        }

        prod.text = datos[position].producto
        descri.text = datos[position].descri
        costo.text = datos[position].costo.toString()
    }
}

class CustomView(varV: View): RecyclerView.ViewHolder(varV){

}