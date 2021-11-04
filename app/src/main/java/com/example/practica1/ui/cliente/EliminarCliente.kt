package com.example.practica1.ui.cliente

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
 * Use the [EliminarCliente.newInstance] factory method to
 * create an instance of this fragment.
 */
class EliminarCliente : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_eliminar_cliente, container, false)

        var btnEliminarCli = view.findViewById<Button>(R.id.btnEliminarCli)

        var cliente_numero = view.findViewById<TextView>(R.id.txt_cliente_numero)
        var nombre_cliente = view.findViewById<TextView>(R.id.txt_nombre_cliente)
        var apellido_paterno = view.findViewById<TextView>(R.id.txt_ap_pat)
        var apellido_materno = view.findViewById<TextView>(R.id.txt_ap_mat)
        var razon_social = view.findViewById<TextView>(R.id.txt_razon_social)
        var rfc = view.findViewById<TextView>(R.id.txt_rfc)
        var direccion = view.findViewById<TextView>(R.id.txt_direccion)
        var pais = view.findViewById<TextView>(R.id.txt_paiss)
        var correo_electronico = view.findViewById<TextView>(R.id.txt_correo_electronico)
        var telefono = view.findViewById<TextView>(R.id.txt_telefono)
        var estado_cliente = view.findViewById<TextView>(R.id.txt_estado_cliente)

        var objectGson = Gson()

        var datosCli = objectGson.fromJson(arguments?.getString("datosClients"), DatosClientes::class.java)
        cliente_numero.text = datosCli?.cliente_numero
        nombre_cliente.text = datosCli?.nombre_cliente
        apellido_paterno.text = datosCli?.apellido_paterno
        apellido_materno.text = datosCli?.apellido_materno
        razon_social.text = datosCli?.razon_social
        rfc.text = datosCli?.rfc
        direccion.text = datosCli?.direccion
        pais.text = datosCli?.pais
        correo_electronico.text = datosCli?.correo_electronico
        telefono.text = datosCli?.telefono
        estado_cliente.text = datosCli?.estado_cliente


        btnEliminarCli.setOnClickListener{

            var url = "http://10.0.76.173:8000/api/elimina_clientes"

            var njson = Gson()

            val tipoPeticion = "application/json; charset=utf-8".toMediaType()

            var datosJsonCliente = njson.toJson(DatosClientes(datosCli?.id,
                cliente_numero.text.toString(),
                nombre_cliente.text.toString(),
                apellido_paterno.text.toString(),
                apellido_materno.text.toString(),
                razon_social.text.toString(),
                rfc.text.toString(),
                direccion.text.toString(),
                pais.text.toString(),
                correo_electronico.text.toString(),
                telefono.text.toString(),
                estado_cliente.text.toString()
            ))

            var request = Request.Builder().url(url).post(datosJsonCliente.toRequestBody(tipoPeticion))

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
                        Toast.makeText(context, "Cliente eliminado", Toast.LENGTH_LONG).show()
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

    data class DatosClientes(
        val id: Int?,
        val cliente_numero: String,
        val nombre_cliente: String,
        val apellido_paterno: String,
        val apellido_materno: String,
        val razon_social: String,
        val rfc: String,
        val direccion: String,
        val pais: String,
        val correo_electronico: String,
        val telefono: String,
        val estado_cliente: String
    )


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EliminarCliente.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EliminarCliente().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}