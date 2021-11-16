package com.example.practica1.ui.registro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.practica1.R
import com.example.practica1.databinding.ActivityRegistroBinding
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class Registro : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_registro)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.terminarRegistro.setOnClickListener{

            val user = binding.nomUsuario.editText?.text.toString()
            val email = binding.emailRegistro.editText?.text.toString()
            val pass = binding.passwordRegistro.editText?.text.toString()

            var url = "http://192.168.100.27:8000/api/registro"

            val jSon = Gson()

            val tipoPet = "application/json; charset=utf-8".toMediaType()

            var datosJsonCate = jSon.toJson(datosRegistro(user, email, pass))

            var request = Request.Builder().url(url).post(datosJsonCate.toRequestBody(tipoPet))

            var client = OkHttpClient()

            client.newCall(request.build()).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    Toast.makeText(baseContext, "Usuario registrado", Toast.LENGTH_LONG).show()
                }

                override fun onFailure(call: Call, e: IOException) {
                    Toast.makeText(baseContext,"Algo salió mal" + e.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    data class datosRegistro(
        val name: String,
        val email: String,
        val password: String
    )
}