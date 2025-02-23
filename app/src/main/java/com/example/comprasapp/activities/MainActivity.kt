package com.example.comprasapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.example.comprasapp.R
import com.example.comprasapp.adapters.ProductosAdapter
import com.example.comprasapp.models.Carrito
import com.example.comprasapp.models.Producto
import com.example.comprasapp.utils.VolleySingleton

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var spinnerCategorias: Spinner
    private val productos = mutableListOf<Producto>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerProductos)
        spinnerCategorias = findViewById(R.id.spinnerCategorias)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 🔹 Se elimina el botón flotante porque ahora el menú superior manejará "Ver Carrito"

        obtenerCategorias()
    }

    /**
     * 🔹 Obtiene las categorías y las carga en el Spinner.
     * 🔹 Al seleccionar una categoría, filtra los productos.
     */
    private fun obtenerCategorias() {
        val url = "https://dummyjson.com/products/categories"
        val request = JsonArrayRequest(Request.Method.GET, url, null,
            { response ->
                val categorias = mutableListOf<String>()
                categorias.add("Todos") // 🔹 Agregamos opción para ver todos los productos

                // 🔹 Extraemos solo el campo "name" de cada objeto
                for (i in 0 until response.length()) {
                    val categoriaObj = response.getJSONObject(i)  // 🔹 Obtenemos el objeto JSON
                    val categoriaNombre = categoriaObj.getString("name")  // 🔹 Extraemos "name"
                    categorias.add(categoriaNombre)
                }

                // 🔹 Log para verificar los datos
                Log.d("Categorías", "Lista de categorías extraídas: $categorias")

                // 🔹 Configuramos el Spinner con los nombres extraídos
                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categorias)
                spinnerCategorias.adapter = adapter

                // 🔹 Escuchar cambios en el Spinner para filtrar productos
                spinnerCategorias.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        val categoriaSeleccionada = categorias[position]
                        obtenerProductos(categoriaSeleccionada)
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        obtenerProductos() // 🔹 Carga todos los productos por defecto
                    }
                }

                // 🔹 Cargar productos iniciales sin filtro
                obtenerProductos("Todos")
            },
            { error ->
                Log.e("Volley", "Error al obtener categorías: ${error.message}")
            }
        )
        VolleySingleton.getInstance(this).addToRequestQueue(request)
    }



    /**
     * 🔹 Obtiene los productos y los muestra en el RecyclerView.
     * 🔹 Filtra los productos si se selecciona una categoría.
     */
    private fun obtenerProductos(categoria: String? = null) {
        val url = if (categoria == null || categoria == "Todos") {
            "https://dummyjson.com/products" // 🔹 Obtiene todos los productos
        } else {
            "https://dummyjson.com/products/category/$categoria" // 🔹 Filtra por categoría
        }

        val request = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                val productosArray = response.getJSONArray("products")
                val productosFiltrados = mutableListOf<Producto>()

                for (i in 0 until productosArray.length()) {
                    val item = productosArray.getJSONObject(i)
                    productosFiltrados.add(
                        Producto(
                            item.getInt("id"),
                            item.getString("title"),
                            item.getDouble("price"),
                            item.getString("thumbnail")
                        )
                    )
                }

                // 🔹 Actualizar el RecyclerView con los productos filtrados
                recyclerView.adapter = ProductosAdapter(productosFiltrados) { producto ->
                    Carrito.agregarProducto(producto)
                    Toast.makeText(this, "${producto.title} añadido al carrito", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Log.e("Volley", "Error al obtener productos: ${error.message}")
            }
        )
        VolleySingleton.getInstance(this).addToRequestQueue(request)
    }

    /**
     * 🔹 Agrega el menú superior con la opción "Ver Carrito".
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    /**
     * 🔹 Maneja la selección de opciones en el menú.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_ver_carrito -> {
                val intent = Intent(this, SecondActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
