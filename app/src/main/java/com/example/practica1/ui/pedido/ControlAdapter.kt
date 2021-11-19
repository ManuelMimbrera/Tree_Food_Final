package com.example.practica1.ui.pedido

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.practica1.R
import com.example.practica1.ui.ventas.CustomView
import com.google.gson.Gson

class ControlAdapter(val datos: Array<ControlPedidos.datosPedido>): RecyclerView.Adapter<CustomView>() {

    override fun getItemCount(): Int {
        return datos.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomView {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.datos_pedidos_clientes, parent, false)
        return CustomView(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomView, position: Int) {
        var alim = holder?.itemView.findViewById(R.id.textAlimento) as TextView
        var pre = holder?.itemView.findViewById(R.id.textPrecio) as TextView
        var can = holder?.itemView.findViewById(R.id.textCantidad) as TextView
        var pago = holder?.itemView.findViewById(R.id.textPago) as TextView
        var dire = holder?.itemView.findViewById(R.id.textDireccion) as TextView
        var esta = holder?.itemView.findViewById(R.id.textEstatus) as TextView

        var boton = holder?.itemView.findViewById(R.id.buttonCambiar) as TextView

        boton.setOnClickListener {
            val navController = holder?.itemView?.findNavController()

            var objJson = Gson()

            var datos = objJson.toJson(datos[position])

            val bundle = bundleOf("datosEdit" to datos)

            navController.navigate(R.id.nav_editar, bundle)
        }

        alim.text = datos[position].nombre
        pre.text = datos[position].precio
        can.text = datos[position].cantidad.toString()
        pago.text = datos[position].pago
        dire.text = datos[position].direccion
        esta.text = datos[position].estatus
    }
}