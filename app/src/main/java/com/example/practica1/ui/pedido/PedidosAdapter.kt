package com.example.practica1.ui.pedido

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.practica1.R
import com.example.practica1.ui.ventas.CustomView

class PedidosAdapter(val datos: Array<DatosPedidoCliente.datosPedido>): RecyclerView.Adapter<CustomView>() {

    override fun getItemCount(): Int {
        return datos.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomView {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.datos_pedido, parent, false)
        return CustomView(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomView, position: Int) {
        var alim = holder?.itemView.findViewById(R.id.textAlimento) as TextView
        var pre = holder?.itemView.findViewById(R.id.textPrecio) as TextView
        var can = holder?.itemView.findViewById(R.id.textCantidad) as TextView
        var total = holder?.itemView.findViewById(R.id.textTotal) as TextView
        var pago = holder?.itemView.findViewById(R.id.textPago) as TextView
        var esta = holder?.itemView.findViewById(R.id.textEstatus) as TextView

        alim.text = datos[position].nombre
        pre.text = datos[position].precio
        can.text = datos[position].cantidad.toString()
        total.text = datos[position].total.toString()
        pago.text = datos[position].pago
        esta.text = datos[position].estatus
    }
}

class CustomView(varV: View): RecyclerView.ViewHolder(varV){

}