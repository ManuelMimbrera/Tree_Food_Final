package com.example.practica1.ui.categoria

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.practica1.R
import com.example.practica1.base.dbHelper
import com.example.practica1.ui.cliente.ClienteFragment
import com.example.practica1.ui.registro.Registro

import com.google.android.material.navigation.NavigationView
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
 * Use the [CategoriasFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CategoriasFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    //lateinit var btnClientes : Button
    //lateinit var listaClientes : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        var listaCategorias = (activity as Activity).findViewById<RecyclerView>(R.id.lista_categorias)


        when(item.itemId){
            R.id.action_settings -> {
                //sincronizar(listaClientes)
            }

            R.id.btn_sinc_cate -> {
                sincronizar(listaCategorias)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_categorias,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_categorias, container, false)

        var btnCategorias = view.findViewById<Button>(R.id.btnCategorias)
        var listaCategorias = view.findViewById<RecyclerView>(R.id.lista_categorias)

        btnCategorias.setOnClickListener {
            val navController = view.findNavController()
            navController.navigate(R.id.nav_datos_categoria)
        }

        //Toast.makeText(context,"Sincronizando datos", Toast.LENGTH_SHORT).show()

        var urlDatos = "http://10.0.76.173:8000/api/lista_categorias"

        val tipoPeticion = "application/json; charset=utf-8".toMediaType()

        var njson = Gson()

        var datosJsonCat = njson.toJson(datosPeticion("%"))

        var request = Request.Builder().url(urlDatos).post(datosJsonCat.toRequestBody(tipoPeticion))

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

        var cliente = OkHttpClient()

        cliente.newCall(request.build()).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                var textoJson = response?.body?.string()

                //print(textoJson)

                val actMain = activity as Activity

                actMain.runOnUiThread{
                    var datosJson = Gson()

                    var clientes = datosJson?.fromJson(textoJson, Array<datosCategorias>::class.java)

                    listaCategorias.adapter = CategoriasAdapter(clientes)

                    //Toast.makeText(context,"¡Sincronización completa!", Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call, e: IOException) {
                val actMain = activity as Activity

                actMain.runOnUiThread{
                    Toast.makeText(context,"Falló la petición" + e.message, Toast.LENGTH_SHORT).show()
                }
            }
        })


        listaCategorias.layoutManager = LinearLayoutManager(context)
        return view


    }

    class datosCategorias(
        val id: Int,
        val nomCate: String,
        val desCate: String,
        val estado: String
    )



    fun sincronizar(listaCategorias: RecyclerView){

    }

    data class datosPeticion(
        val nombre: String
    )

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ClientesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CategoriasFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}