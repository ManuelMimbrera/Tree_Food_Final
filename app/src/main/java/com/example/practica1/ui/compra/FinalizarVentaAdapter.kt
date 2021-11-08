package com.example.practica1.ui.compra

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.practica1.R
import com.example.practica1.base.dbHelper
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class FinalizarVentaAdapter(val datos: Array<FinalizarVentaFragment.datosTotal>): RecyclerView.Adapter<CustomView>() {

    override fun getItemCount(): Int {
        return datos.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomView {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.finalizar_row_layout, parent,false)
        return CustomView(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomView, position: Int) {


        var total = holder?.itemView.findViewById(R.id.txt_total) as TextView
        var reci = holder?.itemView.findViewById<TextView>(R.id.txt_efectivo) as TextView
        var entre = holder?.itemView.findViewById<TextView>(R.id.txt_cambio) as TextView
        total.text = datos[position].total.toString()


    }
}

class CustomView(varV: View): RecyclerView.ViewHolder(varV){

}