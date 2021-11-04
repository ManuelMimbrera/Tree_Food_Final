package com.example.practica1.ui.cliente

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.practica1.R
import com.google.gson.Gson

class ClienteAdapter (val datos:Array<ClienteFragment.datosCliente>): RecyclerView.Adapter<CustomView>(){
    override fun getItemCount(): Int {
        return datos.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomView {
        var layoutInflater = LayoutInflater.from(parent?.context)
        var cellForRow = layoutInflater.inflate(R.layout.clientes_row_layout, parent, false)
        return CustomView(cellForRow)
    }


    override fun onBindViewHolder(holder: CustomView, position: Int) {
        var eliminarCli = holder?.itemView.findViewById(R.id.btn_elim_cli) as TextView
        var editarCli = holder?.itemView.findViewById(R.id.btn_act_cli) as TextView
        var cliente_numero = holder?.itemView.findViewById(R.id.txt_cliente_numero) as TextView
        var nombre_cliente = holder?.itemView.findViewById(R.id.txt_nombre_cliente) as TextView
        var ap_pat = holder?.itemView.findViewById(R.id.txt_ap_pat) as TextView
        var ap_mat = holder?.itemView.findViewById(R.id.txt_ap_mat) as TextView
        var razon_social = holder?.itemView.findViewById(R.id.txt_razon_social) as TextView
        var rfc = holder?.itemView?.findViewById(R.id.txt_rfc) as TextView
        var direccion = holder?.itemView.findViewById(R.id.txt_direccion) as TextView
        var pais = holder?.itemView.findViewById(R.id.txt_paiss) as TextView
        var correo_electronico = holder?.itemView.findViewById(R.id.txt_correo_electronico) as TextView
        var telefono = holder?.itemView.findViewById(R.id.txt_telefono) as TextView
        var estado_cliente = holder?.itemView.findViewById(R.id.txt_estado_cliente) as TextView


        editarCli.setOnClickListener{

            val navController = holder?.itemView?.findNavController()
            var objetojson = Gson()
            var datos = objetojson.toJson(datos[position])

            val bundle = bundleOf("datosCliente" to datos)

            navController.navigate(R.id.nav_register_clientes, bundle)
        }

        eliminarCli.setOnClickListener{

            val navController = holder?.itemView?.findNavController()
            var objetojson = Gson()
            var datos = objetojson.toJson(datos[position])

            val bundle = bundleOf("datosClients" to datos)

            navController.navigate(R.id.nav_clientes_eliminar, bundle)
        }

        cliente_numero.text = datos[position].cliente_numero
        nombre_cliente.text = datos[position].nombre_cliente
        ap_pat.text = datos[position].apellido_paterno
        ap_mat.text = datos[position].apellido_materno
        razon_social.text = datos[position].razon_social
        rfc.text = datos[position].rfc
        direccion.text = datos[position].direccion
        pais.text = datos[position].pais
        correo_electronico.text = datos[position].correo_electronico
        telefono.text = datos[position].telefono
        estado_cliente.text = datos[position].estado_cliente
    }
}

class CustomView(varV: View): RecyclerView.ViewHolder(varV){

}