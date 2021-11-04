package com.example.practica1.ui.categoria


import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.practica1.R
import com.google.gson.Gson


class CategoriasAdapter(val datos:Array<CategoriasFragment.datosCategorias>):RecyclerView.Adapter<CustomView>(){
    override fun getItemCount(): Int {
        return datos.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomView {
        var layoutInflater = LayoutInflater.from(parent?.context)
        var cellForRow = layoutInflater.inflate(R.layout.row_layout, parent, false)
        return CustomView(cellForRow)
    }



    override fun onBindViewHolder(holder: CustomView, position: Int) {

        var eliminar = holder?.itemView.findViewById(R.id.btn_elim_cate) as TextView
        var editar = holder?.itemView.findViewById(R.id.btn_act_cate) as TextView
        var nombre = holder?.itemView.findViewById(R.id.txt_nombre) as TextView
        var descripcion = holder?.itemView.findViewById(R.id.txt_descripcion) as TextView
        var estado = holder?.itemView.findViewById(R.id.txt_estado) as TextView

        editar.setOnClickListener {
            val navController = holder?.itemView?.findNavController()

            var objJson = Gson()

            var datos = objJson.toJson(datos[position])

            val bundle = bundleOf("datosCategori" to datos)

            navController.navigate(R.id.nav_datos_categoria,bundle)
        }

        eliminar.setOnClickListener {
            val navController = holder?.itemView?.findNavController()

            var objJson = Gson()

            var datos = objJson.toJson(datos[position])

            val bundle = bundleOf("datosCategoris" to datos)

            navController.navigate(R.id.nav_categorias_eliminar,bundle)
        }


        nombre.text = datos[position].nomCate
        descripcion.text = datos[position].desCate
        estado.text = datos[position].estado
    }

}



class CustomView(varV: View):RecyclerView.ViewHolder(varV){

}