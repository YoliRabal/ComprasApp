package com.example.comprasapp.activities

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.comprasapp.R
import com.example.comprasapp.adapters.CarritoAdapter
import com.example.comprasapp.models.Carrito
import com.google.android.material.snackbar.Snackbar
import java.text.DecimalFormat

class SecondActivity : AppCompatActivity() {
    private lateinit var recyclerViewCarrito: RecyclerView
    private lateinit var txtTotalPrecio: TextView
    private lateinit var btnConfirmarCompra: Button
    private lateinit var btnVaciarCarrito: Button
    private lateinit var adapter: CarritoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        recyclerViewCarrito = findViewById(R.id.recyclerCarrito)
        txtTotalPrecio = findViewById(R.id.txtTotalPrecio)
        btnConfirmarCompra = findViewById(R.id.btnConfirmarCompra)
        btnVaciarCarrito = findViewById(R.id.btnVaciarCarrito)

        recyclerViewCarrito.layoutManager = LinearLayoutManager(this)

        // 🔹 Configurar el adaptador del carrito
        adapter = CarritoAdapter(Carrito.productos.toMutableList()) { producto ->
            Carrito.eliminarProducto(producto)
            actualizarTotal()
            adapter.notifyDataSetChanged()
        }
        recyclerViewCarrito.adapter = adapter

        // 🔹 Mostrar el total correctamente formateado
        actualizarTotal()

        // 🔹 Configurar el botón de Confirmar Compra
        btnConfirmarCompra.setOnClickListener {
            if (Carrito.productos.isNotEmpty()) {
                val totalFormateado = DecimalFormat("#.##").format(Carrito.calcularTotal())
                Snackbar.make(it, "Enhorabuena, compra por valor de €$totalFormateado realizada", Snackbar.LENGTH_LONG).show()
                Carrito.vaciarCarrito()
                actualizarCarrito()
            } else {
                Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show()
            }
        }

        // 🔹 Configurar el botón de Vaciar Carrito
        btnVaciarCarrito.setOnClickListener {
            if (Carrito.productos.isNotEmpty()) {
                Snackbar.make(it, "Carrito vaciado", Snackbar.LENGTH_LONG).show()
                Carrito.vaciarCarrito()
                actualizarCarrito()
            } else {
                Toast.makeText(this, "El carrito ya está vacío", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * 🔹 Actualiza el total del carrito con solo 2 decimales.
     */
    private fun actualizarTotal() {
        val totalFormateado = DecimalFormat("#.##").format(Carrito.calcularTotal())
        txtTotalPrecio.text = "Total: €$totalFormateado"
    }

    /**
     * 🔹 Vacía el carrito y actualiza la interfaz correctamente.
     */
    private fun actualizarCarrito() {
        adapter = CarritoAdapter(Carrito.productos.toMutableList()) { producto ->
            Carrito.eliminarProducto(producto)
            actualizarTotal()
            adapter.notifyDataSetChanged()
        }
        recyclerViewCarrito.adapter = adapter  // 🔹 Refrescar el `RecyclerView`
        actualizarTotal()
    }
}
