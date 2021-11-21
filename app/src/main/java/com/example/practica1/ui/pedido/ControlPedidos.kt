package com.example.practica1.ui.pedido

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.practica1.R
import com.example.practica1.base.dbHelper
import com.example.practica1.ui.ventas.FinalizarVenta
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ControlPedidos.newInstance] factory method to
 * create an instance of this fragment.
 */
class ControlPedidos : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_control_pedidos, container, false)

        var listaCarro = view.findViewById<RecyclerView>(R.id.rvPedidos)

        var urlDatos = "http://10.0.76.173:8000/api/lista_predidos"

        val tipoPeticion = "application/json; charset=utf-8".toMediaType()

        var njson = Gson()

        var datosJsonOp = njson.toJson(FinalizarVenta.datosPeticion("%"))

        var request = Request.Builder().url(urlDatos).post(datosJsonOp.toRequestBody(tipoPeticion))

        val dbHelp = dbHelper(context as Context)
        val dbRead = dbHelp.readableDatabase
        val cursor = dbRead.query(
            dbHelper.FeedReaderContract.FeedEntry.TABLE_NAME,   // The table to query
            null,             // The array of columns to return (pass null to get all)
            null,              // The columns for the WHERE clause
            null,          // The values for the WHERE clause
            null,             // don't group the rows
            null,              // don't filter by row groups
            null               // The sort order
        )

        var token = ""

        with(cursor) {
            moveToNext()

            token = getString(getColumnIndexOrThrow(dbHelper.FeedReaderContract.FeedEntry.COLUMN_NAME_TOKEN))
        }

        request.addHeader("Accept","application/json")
        request.addHeader("Authorization","Bearer " + token)

        var producto = OkHttpClient()

        producto.newCall(request.build()).enqueue(object : Callback {
            override  fun onResponse(call: Call, response: Response) {
                var textoJson = response?.body?.string()

                print(textoJson)

                val actMain = activity as Activity

                actMain.runOnUiThread{
                    var datosJson = Gson()

                    var prod = datosJson?.fromJson(textoJson, Array<datosPedido>::class.java)

                    listaCarro.adapter = ControlAdapter(prod)

                    //Toast.makeText(context,"¡Sincronización completa!", Toast.LENGTH_SHORT).show()
                }
            }

            override  fun onFailure(call: Call, e: IOException) {
                val actMain = activity as Activity

                actMain.runOnUiThread{
                    Toast.makeText(context,"Falló la petición" + e.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

        listaCarro.layoutManager = LinearLayoutManager(context)

        return view
    }

    data class datosPedido(
        val id: Int?,
        val nombre: String,
        val precio: String,
        val cantidad: Int,
        val total: Int,
        val pago: String,
        val direccion: String,
        val estatus: String
    )

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ControlPedidos.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ControlPedidos().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}