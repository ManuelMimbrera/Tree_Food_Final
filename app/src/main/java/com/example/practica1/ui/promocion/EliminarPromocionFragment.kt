package com.example.practica1.ui.promocion

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
import androidx.navigation.findNavController
import com.example.practica1.R
import com.example.practica1.base.dbHelper
import com.example.practica1.ui.productos.EliminarProductos
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
 * Use the [EliminarPromocionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EliminarPromocionFragment : Fragment() {
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
        var view = inflater.inflate(R.layout.fragment_eliminar_promocion, container, false)

        var btnEliminarPromo = view.findViewById<Button>(R.id.btn_eliminar_promo)

        var nombrePromo = view.findViewById<TextView>(R.id.txt_nombrePromo)
        var nombreProduct = view.findViewById<TextView>(R.id.txt_nombreProduc)
        var descripcionPromo = view.findViewById<TextView>(R.id.txt_descripcionPromocion)

        var objJson = Gson()

        var datosPromo = objJson.fromJson(arguments?.getString("datosPromocion"), EliminarPromocionFragment.datosPromocion::class.java)

        nombrePromo.text = datosPromo?.nombre_promocion
        nombreProduct.text = datosPromo?.nombre_producto
        descripcionPromo.text = datosPromo?.descripcion

        btnEliminarPromo.setOnClickListener{

            var url = "http://10.0.76.173:8000/api/eliminar_promociones"

            val jSon = Gson()

            val tipoPet = "application/json; charset=utf-8".toMediaType()

            var datosJsonProd = jSon.toJson(
                EliminarPromocionFragment.datosPromocion(
                    datosPromo?.id,
                    nombrePromo.text.toString(),
                    nombreProduct.text.toString(),
                    descripcionPromo.text.toString()
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
                        Snackbar.make(btnEliminarPromo , "Promocion eliminada", Snackbar.LENGTH_SHORT)
                            .show()
                        val navController = view.findNavController()
                        navController.navigate(R.id.nav_promocion)
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
        return view;
    }


    data class datosPromocion(
        val id: Int?,
        val nombre_promocion: String,
        val nombre_producto: String,
        val descripcion: String
    )


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EliminarPromocionFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EliminarPromocionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}