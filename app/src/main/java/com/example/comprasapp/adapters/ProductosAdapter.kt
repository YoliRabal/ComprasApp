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

class ProductosAdapter(
    private val productos: List<Producto>,
    private val onAddToCart: (Producto) -> Unit
) : RecyclerView.Adapter<ProductosAdapter.ProductoViewHolder>() {

    class ProductoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgProducto: ImageView = view.findViewById(R.id.imgProducto)
        val txtNombre: TextView = view.findViewById(R.id.txtNombre)
        val txtPrecio: TextView = view.findViewById(R.id.txtPrecio)
        val btnAgregar: Button = view.findViewById(R.id.btnAgregar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_producto, parent, false)
        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]
        holder.txtNombre.text = producto.title
        holder.txtPrecio.text = "€${producto.price}"
        Glide.with(holder.itemView.context).load(producto.thumbnail).into(holder.imgProducto)

        holder.btnAgregar.setOnClickListener {
            onAddToCart(producto)
        }
    }

    override fun getItemCount() = productos.size
}
