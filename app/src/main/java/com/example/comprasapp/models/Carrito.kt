package com.example.comprasapp.models

object Carrito {
    val productos = mutableListOf<Producto>()

    fun agregarProducto(producto: Producto) {
        productos.add(producto)
    }

    fun eliminarProducto(producto: Producto) {
        productos.remove(producto)
    }

    fun vaciarCarrito() {
        productos.clear()
    }

    fun calcularTotal(): Double {
        return productos.sumOf { it.price }
    }
}
