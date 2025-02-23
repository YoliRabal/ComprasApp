package com.example.comprasapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.comprasapp.R
import com.example.comprasapp.models.Producto

class CarritoAdapter(
    private val productos: MutableList<Producto>,
    private val onRemoveFromCart: (Producto) -> Unit
) : RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder>() {

    class CarritoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgProducto: ImageView = view.findViewById(R.id.imgProductoCarrito)
        val txtNombre: TextView = view.findViewById(R.id.txtNombreCarrito)
        val txtPrecio: TextView = view.findViewById(R.id.txtPrecioCarrito)
        val btnEliminar: Button = view.findViewById(R.id.btnEliminarCarrito)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_carrito, parent, false)
        return CarritoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarritoViewHolder, position: Int) {
        val producto = productos[position]
        holder.txtNombre.text = producto.title
        holder.txtPrecio.text = "€${producto.price}"
        Glide.with(holder.itemView.context).load(producto.thumbnail).into(holder.imgProducto)

        holder.btnEliminar.setOnClickListener {
            onRemoveFromCart(producto)
        }
    }

    override fun getItemCount() = productos.size
}
