package com.example.practica1.ui.productos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.practica1.R
import com.example.practica1.ui.categoria.CustomView
import com.google.gson.Gson

class NuevaCategorias(val datos:Array<ReciclerCategorias.datosCategoria>): RecyclerView.Adapter<CustomView2>() {
    override fun getItemCount(): Int {
        return datos.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomView2 {
        var layoutInflater = LayoutInflater.from(parent?.context)
        var cellForRow = layoutInflater.inflate(R.layout.fragment_seleccion_categoria, parent, false)
        return CustomView2(cellForRow)
    }


    override fun onBindViewHolder(holder: CustomView2, position: Int) {

        var iden = holder?.itemView.findViewById(R.id.txtId) as TextView
        var nombre = holder?.itemView.findViewById(R.id.txtNombre) as TextView
        var button = holder?.itemView.findViewById(R.id.btnElegir) as TextView

        button.setOnClickListener {

            val navController = holder?.itemView?.findNavController()

            var objJson = Gson()

            var datos = objJson.toJson(datos[position])

            var bundle = bundleOf("Id" to datos)


            navController.navigate(R.id.nav_datos_producto, bundle)
            /*

            val intent = Intent(holder?.itemView.context,DatosProducto::class.java)
            intent.putExtra("Id", ide)*/
        }

        iden.text = datos[position].id.toString()
        nombre.text = datos[position].nomcate

    }
}

class CustomView2(varV: View): RecyclerView.ViewHolder(varV){

}