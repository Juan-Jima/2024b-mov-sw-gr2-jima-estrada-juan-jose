package org.example.main

import org.example.supermercado.dao.SupermercadoCRUD
import org.example.supermercado.model.Producto
import org.example.supermercado.model.Supermercado
import javax.swing.JOptionPane

fun main() {
    val crud = SupermercadoCRUD()

    while (true) {
        val opcion = JOptionPane.showInputDialog(
            null,
            "Seleccione una opción:\n" +
                    "1. Crear Supermercado\n" +
                    "2. Ver Supermercado\n" +
                    "3. Crear Producto\n" +
                    "4. Ver Productos\n" +
                    "5. Actualizar Producto\n" +
                    "6. Eliminar Producto\n" +
                    "7. Eliminar Supermercado\n" +
                    "8. Salir",
            "CRUD Supermercado",
            JOptionPane.QUESTION_MESSAGE
        )

        when (opcion) {
            "1" -> {
                // Crear Supermercado
                val nombre = JOptionPane.showInputDialog("Ingrese el nombre del supermercado:")

                // Verificar si el supermercado ya existe
                val supermercados = crud.leerSupermercados()
                val supermercadoExistente = supermercados.find { it.nombre == nombre }

                if (supermercadoExistente != null) {
                    JOptionPane.showMessageDialog(null, "¡Ya existe un supermercado con ese nombre!")
                } else {
                    val direccion = JOptionPane.showInputDialog("Ingrese la dirección del supermercado:")
                    val telefono = JOptionPane.showInputDialog("Ingrese el teléfono del supermercado:")
                    val supermercado = Supermercado(
                        id = crud.generarIdSupermercado(),
                        nombre = nombre,
                        numeroDeProductos = 0,
                        productos = mutableListOf(),
                        direccion = direccion,
                        telefono = telefono
                    )
                    crud.crearSupermercado(supermercado)
                    JOptionPane.showMessageDialog(null, "Supermercado creado con éxito!")
                }
            }
            "2" -> {
                // Ver Supermercado
                val supermercados = crud.leerSupermercados()
                if (supermercados.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No hay supermercados registrados.")
                } else {
                    val supermercadosString = supermercados.joinToString("\n") {
                        "ID: ${it.id}, Nombre: ${it.nombre}, Dirección: ${it.direccion}, Teléfono: ${it.telefono}"
                    }
                    JOptionPane.showMessageDialog(null, "Supermercados registrados:\n$supermercadosString")
                }
            }
            "3" -> {
                // Crear Producto
                val supermercados = crud.leerSupermercados()
                if (supermercados.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No hay supermercados disponibles para agregar productos.")
                } else {
                    // Mostrar supermercados disponibles
                    val supermercadoNames = supermercados.joinToString("\n") { it.nombre }
                    val nombreSupermercado = JOptionPane.showInputDialog("Seleccione un supermercado para agregar un producto:\n$supermercadoNames")

                    val supermercado = supermercados.find { it.nombre == nombreSupermercado }
                    if (supermercado != null) {
                        val id = JOptionPane.showInputDialog("Ingrese el ID del producto:").toInt()
                        val nombreProducto = JOptionPane.showInputDialog("Ingrese el nombre del producto:")
                        val precio = JOptionPane.showInputDialog("Ingrese el precio del producto:").toDouble()
                        val fechaCaducidad = JOptionPane.showInputDialog("Ingrese la fecha de caducidad del producto:")
                        val enStock = JOptionPane.showInputDialog("¿Está el producto en stock? (true/false):").toBoolean()
                        val producto = Producto(id, nombreProducto, precio, fechaCaducidad, enStock)
                        crud.crearProducto(producto, supermercado)
                        JOptionPane.showMessageDialog(null, "Producto agregado al supermercado exitosamente!")
                    } else {
                        JOptionPane.showMessageDialog(null, "Supermercado no encontrado.")
                    }
                }
            }
            "4" -> {
                // Ver Productos
                val supermercados = crud.leerSupermercados()
                if (supermercados.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No hay supermercados registrados para ver productos.")
                } else {
                    val supermercadoNames = supermercados.joinToString("\n") { it.nombre }
                    val nombreSupermercado = JOptionPane.showInputDialog("Seleccione un supermercado para ver sus productos:\n$supermercadoNames")

                    val supermercado = supermercados.find { it.nombre == nombreSupermercado }
                    if (supermercado != null) {
                        val productosString = supermercado.productos.joinToString("\n") {
                            "ID: ${it.id}, Nombre: ${it.nombre}, Precio: ${it.precio}, Stock: ${it.enStock}"
                        }
                        JOptionPane.showMessageDialog(null, "Productos de $nombreSupermercado:\n$productosString")
                    } else {
                        JOptionPane.showMessageDialog(null, "Supermercado no encontrado.")
                    }
                }
            }
            "5" -> {
                // Actualizar Producto
                val supermercados = crud.leerSupermercados()
                if (supermercados.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No hay supermercados para actualizar productos.")
                } else {
                    val supermercadoNames = supermercados.joinToString("\n") { it.nombre }
                    val nombreSupermercado = JOptionPane.showInputDialog("Seleccione un supermercado para actualizar productos:\n$supermercadoNames")

                    val supermercado = supermercados.find { it.nombre == nombreSupermercado }
                    if (supermercado != null) {
                        val id = JOptionPane.showInputDialog("Ingrese el ID del producto a actualizar:").toInt()
                        val producto = supermercado.productos.find { it.id == id }
                        if (producto != null) {
                            // Mostrar la información actual del producto
                            val infoProducto = """
                    Producto actual:
                    ID: ${producto.id}
                    Nombre: ${producto.nombre}
                    Precio: ${producto.precio}
                    Fecha de caducidad: ${producto.fechaCaducidad}
                    En stock: ${producto.enStock}
                """.trimIndent()

                            JOptionPane.showMessageDialog(null, infoProducto)

                            // Solicitar la actualización de los datos
                            val nuevoNombre = JOptionPane.showInputDialog("Nuevo nombre del producto:") ?: producto.nombre
                            val nuevoPrecio = JOptionPane.showInputDialog("Nuevo precio del producto:")?.toDouble() ?: producto.precio
                            val nuevaFechaCaducidad = JOptionPane.showInputDialog("Nueva fecha de caducidad:") ?: producto.fechaCaducidad
                            val nuevoEnStock = JOptionPane.showInputDialog("¿Está en stock? (true/false):")?.toBoolean() ?: producto.enStock

                            // Crear un nuevo objeto Producto con los nuevos valores
                            val productoActualizado = Producto(id, nuevoNombre, nuevoPrecio, nuevaFechaCaducidad, nuevoEnStock)

                            // Actualizar el producto dentro de la lista de productos del supermercado
                            val indiceProducto = supermercado.productos.indexOfFirst { it.id == id }
                            if (indiceProducto != -1) {
                                supermercado.productos[indiceProducto] = productoActualizado

                                // Actualizar en el CRUD, si es necesario
                                crud.actualizarProducto(productoActualizado)

                                JOptionPane.showMessageDialog(null, "Producto actualizado exitosamente!")
                            } else {
                                JOptionPane.showMessageDialog(null, "Producto no encontrado en la lista del supermercado.")
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Producto no encontrado.")
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Supermercado no encontrado.")
                    }
                }
            }
            "6" -> {
                // Eliminar Producto
                val supermercados = crud.leerSupermercados()
                if (supermercados.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No hay supermercados para eliminar productos.")
                } else {
                    val supermercadoNames = supermercados.joinToString("\n") { it.nombre }
                    val nombreSupermercado = JOptionPane.showInputDialog("Seleccione un supermercado para eliminar productos:\n$supermercadoNames")

                    val supermercado = supermercados.find { it.nombre == nombreSupermercado }
                    if (supermercado != null) {
                        val id = JOptionPane.showInputDialog("Ingrese el ID del producto a eliminar:").toInt()
                        val producto = supermercado.productos.find { it.id == id }
                        if (producto != null) {
                            crud.eliminarProducto(id)
                            JOptionPane.showMessageDialog(null, "Producto eliminado exitosamente!")
                        } else {
                            JOptionPane.showMessageDialog(null, "Producto no encontrado.")
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Supermercado no encontrado.")
                    }
                }
            }
            "7" -> {
                // Eliminar Supermercado
                val supermercados = crud.leerSupermercados()
                if (supermercados.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No hay supermercados para eliminar.")
                } else {
                    val supermercadoNames = supermercados.joinToString("\n") { it.nombre }
                    val nombreSupermercado = JOptionPane.showInputDialog("Ingrese el nombre del supermercado a eliminar:\n$supermercadoNames")
                    val supermercado = supermercados.find { it.nombre == nombreSupermercado }
                    if (supermercado != null) {
                        crud.eliminarSupermercado(nombreSupermercado)
                        JOptionPane.showMessageDialog(null, "Supermercado eliminado con éxito!")
                    } else {
                        JOptionPane.showMessageDialog(null, "Supermercado no encontrado.")
                    }
                }
            }
            "8" -> return
            else -> JOptionPane.showMessageDialog(null, "Opción no válida.")
        }
    }
}
