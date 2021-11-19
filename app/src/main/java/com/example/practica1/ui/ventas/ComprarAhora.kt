package com.example.practica1.ui.ventas

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
import com.example.practica1.ui.menu.Menu
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
 * Use the [ComprarAhora.newInstance] factory method to
 * create an instance of this fragment.
 */
class ComprarAhora : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_comprar_ahora, container, false)

        var btnFinalizar = view.findViewById<Button>(R.id.btn_finalizar)
        var preciofinal = view.findViewById<TextView>(R.id.txt_precioFinal)
        var identi = view.findViewById<TextView>(R.id.identificador)
        var alim = view.findViewById<TextView>(R.id.Alimento)
        var precio = view.findViewById<TextView>(R.id.Precio)
        var cantidad = view.findViewById<TextView>(R.id.Cantidad)
        var tipo = view.findViewById<TextView>(R.id.textTipo)
        var descripcion = view.findViewById<TextView>(R.id.textPago)
        var spinner = view.findViewById<Spinner>(R.id.spTipoPago)
        val pago = resources.getStringArray(R.array.opcionesPago)

        ArrayAdapter.createFromResource(context as Context,R.array.opcionesPago,android.R.layout.simple_spinner_item).also {
                adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                tipo.text = pago[position]
                descripcion.text =  pago[position]
                if(pago[position] == "Efectivo"){
                    descripcion.text = "Nuestros repartidores no llevan más de $200 de cambio."
                } else if(pago[position] == "Tarjeta"){
                    descripcion.text = "Nuestros repartidores te llevarán la terminal bancaria."
                }
            }
        }

        var objJson = Gson()

        var datosProd = objJson.fromJson(arguments?.getString("datosVenta"), Menu.datosProducto::class.java)

        identi.text = datosProd?.id.toString()
        alim.text = datosProd?.nombre.toString()
        precio.text = datosProd?.precio

        btnFinalizar.setOnClickListener {


            var url = "http://10.0.76.173:8000/api/guardar_venta"

            val jSon = Gson()

            val tipoPet = "application/json; charset=utf-8".toMediaType()

            var datosJsonVen = jSon.toJson(
                datosVenta(
                    preciofinal.text.toString().toFloat(),
                    identi.text.toString().toInt(),
                    cantidad.text.toString().toInt(),
                    tipo.text.toString()
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
                        Toast.makeText(context, "¡Tu compra ha sido realizada!", Toast.LENGTH_LONG).show()
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
            navController.navigate(R.id.nav_pedido)

        }

        return view
    }

    class datosVenta(
        val total: Float,
        val id_producto: Int,
        val cantidad: Int,
        val pago: String
    )

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ComprarAhora.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ComprarAhora().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}