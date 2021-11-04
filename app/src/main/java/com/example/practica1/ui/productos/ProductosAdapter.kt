package com.example.practica1.ui.productos

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.practica1.R
import com.example.practica1.base.dbHelper
import com.example.practica1.ui.ventas.DatosVenta
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException


class ProductosAdapter(val datos: Array<ProductosFragment.datosProducto>): RecyclerView.Adapter<CustomView>() {

    override fun getItemCount(): Int {
        return datos.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomView {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.productos_row_layout, parent,false)
        return CustomView(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomView, position: Int) {

        var eliminarProd = holder?.itemView.findViewById(R.id.btn_elim_prod) as TextView
        var editarProd = holder?.itemView.findViewById(R.id.btn_act_prod) as TextView
        var nombreProd = holder?.itemView.findViewById(R.id.txt_nombrePro) as TextView
        var descripcionProd = holder?.itemView.findViewById(R.id.txt_descripcionPro) as TextView
        var precioProd = holder?.itemView.findViewById(R.id.txt_precioPro) as TextView


        editarProd.setOnClickListener{
            val navController = holder?.itemView?.findNavController()

            var objJson = Gson()

            var datos = objJson.toJson(datos[position])

            val bundle = bundleOf("datosProduct" to datos)

            navController.navigate(R.id.nav_datos_producto, bundle)
        }

        eliminarProd.setOnClickListener{
            val navController = holder?.itemView?.findNavController()

            var objJson = Gson()

            var datos = objJson.toJson(datos[position])

            val bundle = bundleOf("datosProducto" to datos)

            navController.navigate(R.id.nav_productos_eliminar, bundle)
        }

        nombreProd.text = datos[position].nombre
        descripcionProd.text = datos[position].descripcion
        precioProd.text = datos[position].precio.toString()
    }
}

class CustomView(varV: View): RecyclerView.ViewHolder(varV){

}