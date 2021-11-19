package com.example.practica1.ui.pedido

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.findNavController
import com.example.practica1.R
import com.example.practica1.base.dbHelper
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
 * Use the [EditarPedido.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditarPedido : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_editar_pedido, container, false)

        var btnListo = view.findViewById<Button>(R.id.button)

        var alimento = view.findViewById<TextView>(R.id.textAlimento)
        var precio = view.findViewById<TextView>(R.id.textPrecio)
        var cantidad = view.findViewById<TextView>(R.id.textCantidad)
        var pago = view.findViewById<TextView>(R.id.textPago)
        var estatus = view.findViewById<Spinner>(R.id.snEstatus)
        var valor = view.findViewById<TextView>(R.id.textEstatusE)

        val opciones = resources.getStringArray(R.array.opcionesP)

        ArrayAdapter.createFromResource(context as Context,R.array.opcionesP,android.R.layout.simple_spinner_item).also {
                adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            estatus.adapter = adapter
        }
        estatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                valor.text =  opciones[position]
            }
        }

        var objJson = Gson()

        var datosP = objJson.fromJson(arguments?.getString("datosEdit"),
            ControlPedidos.datosPedido::class.java)

        alimento.text = datosP?.nombre
        precio.text = datosP?.precio.toString()
        cantidad.text = datosP?.cantidad.toString()
        pago.text = datosP?.pago

        btnListo.setOnClickListener {

            var url = "http://10.0.76.173:8000/api/guarda_pedido"

            val jSon = Gson()

            val tipoPet = "application/json; charset=utf-8".toMediaType()

            var datosJsonProd = jSon.toJson(
                datosPedido(
                    datosP?.id,
                    valor.text.toString()
                )
            )

            var request = Request.Builder().url(url).post(datosJsonProd.toRequestBody(tipoPet))

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

            var client = OkHttpClient()

            client.newCall(request.build()).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    val actMain = activity as Activity

                    actMain.runOnUiThread {
                        Toast.makeText(context, "Datos guardados", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    val actMain = activity as Activity
                    actMain.runOnUiThread{
                        Toast.makeText(context,"Algo salió mal" + e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            })

            val navController = view.findNavController()
            navController.navigate(R.id.nav_pedidos)
        }

        return view
    }

    data class datosPedido(
        val id: Int?,
        val estatus: String
    )

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EditarPedido.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditarPedido().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}