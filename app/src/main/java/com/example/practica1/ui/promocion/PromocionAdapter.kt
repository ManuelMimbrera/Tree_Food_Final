package com.example.practica1.ui.promocion

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.practica1.R
import com.example.practica1.ui.productos.ProductosFragment
import com.google.gson.Gson

class PromocionAdapter(val datos: Array<PromocionFragment.datosPromocion>): RecyclerView.Adapter<CustomView>() {

    override fun getItemCount(): Int {
        return datos.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomView {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.row_layout_promocion, parent,false)
        return CustomView(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomView, position: Int) {

        var eliminarPromo = holder?.itemView.findViewById(R.id.btn_eliminar_promocion) as TextView
        var editarPromo = holder?.itemView.findViewById(R.id.btn_editar_promocion) as TextView

        var nombrePromo = holder?.itemView.findViewById(R.id.txt_nombrePromo) as TextView
        var nombreProducto = holder?.itemView.findViewById(R.id.txt_nombreProducto) as TextView
        var descripcionPromo = holder?.itemView.findViewById(R.id.txt_descripcionPromo) as TextView


        editarPromo .setOnClickListener{
            val navController = holder?.itemView?.findNavController()

            var objJson = Gson()

            var datos = objJson.toJson(datos[position])

            val bundle = bundleOf("datosPromocion" to datos)

            navController.navigate(R.id.nav_register_promo, bundle)
        }

        eliminarPromo.setOnClickListener{
            val navController = holder?.itemView?.findNavController()

            var objJson = Gson()

            var datos = objJson.toJson(datos[position])

            val bundle = bundleOf("datosPromocion" to datos)

            navController.navigate(R.id.nav_eliminar_promo, bundle)
        }


        nombrePromo.text = datos[position].nombre_promocion
        nombreProducto.text = datos[position].nombre_producto
        descripcionPromo.text = datos[position].descripcion
    }
}

class CustomView(varV: View): RecyclerView.ViewHolder(varV){

}