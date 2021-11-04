package com.example.practica1.ui.carrito

import android.os.Bundle
import android.os.PersistableBundle
import android.util.DisplayMetrics
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.practica1.R
import com.example.practica1.ui.*
import com.example.practica1.ui.productos.EliminarProductos

class CarritoRowLayout : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.carrito_row_layout)

        var btn = findViewById<Button>(R.id.btn_elim_car)

        btn.setOnClickListener{
            var dialogar = EliminarProductos()
            dialogar.show(supportFragmentManager, "customDialog")
        }
    }
}