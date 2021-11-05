package com.example.practica1.ui.ventas

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.practica1.R
import com.example.practica1.base.dbHelper
import com.example.practica1.ui.categoria.DatosCategoria
import com.example.practica1.ui.productos.DatosProducto
import com.google.android.material.snackbar.Snackbar
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
 * Use the [FinalizarVenta.newInstance] factory method to
 * create an instance of this fragment.
 */
class FinalizarVenta : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_finalizar_venta, container, false)

        var btnFinalizar = view.findViewById<Button>(R.id.btn_finalizar)

        var preciofinal = view.findViewById<TextView>(R.id.txt_precioFinal)
        var reci = view.findViewById<TextView>(R.id.txt_efectivo)
        var entre = view.findViewById<TextView>(R.id.txt_cambio)

        var objJson = Gson()

        var datosVen = objJson.fromJson(arguments?.getString("datosVenta"), DatosProducto.datosProducto::class.java)


        preciofinal.text = datosVen?.precio.toString()


        btnFinalizar.setOnClickListener {


            var url = "http://10.0.76.173:8000/api/guardar_venta"

            val jSon = Gson()

            val tipoPet = "application/json; charset=utf-8".toMediaType()

            var datosJsonVen = jSon.toJson(
                datosVenta(
                    preciofinal.text.toString().toFloat(),
                    reci.text.toString().toFloat(),
                    entre.text.toString().toFloat()
                )
            )

            var request = Request.Builder().url(url).post(datosJsonVen.toRequestBody(tipoPet))

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
                        Snackbar.make(btnFinalizar , "¡Tu compra ha sido realizada!", Snackbar.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    val actMain = activity as Activity
                    actMain.runOnUiThread{
                        Toast.makeText(context,"Algo salió mal" + e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            })

        }

        return view
    }

    data class datosVenta(
        val total: Float,
        val recibido: Float,
        val entregado: Float,
        )

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FinalizarVenta.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FinalizarVenta().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}