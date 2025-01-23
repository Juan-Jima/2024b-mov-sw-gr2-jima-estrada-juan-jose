package com.example.app_movil_supermercado_producto_juan_jima

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class ActivityProductoList : AppCompatActivity() {
    private lateinit var controlador: Controlador
    private lateinit var listViewProductos: ListView
    private lateinit var btnAgregarProducto: Button
    private var selectedProducto: Producto? = null
    private var supermercadoId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_producto_list)

        controlador = Controlador(this)
        listViewProductos = findViewById(R.id.listViewProductos)
        btnAgregarProducto = findViewById(R.id.btnAgregarProducto)

        supermercadoId = intent.getIntExtra("supermercadoId", 0)

        btnAgregarProducto.setOnClickListener {
            val intent = Intent(this, ActivityAgregarEditarProducto::class.java)
            intent.putExtra("supermercadoId", supermercadoId)
            startActivity(intent)
        }

        registerForContextMenu(listViewProductos)
        actualizarLista()
    }

    private fun actualizarLista() {
        val productos = controlador.listarProductosPorSupermercado(supermercadoId)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, productos.map { it.nombre })
        listViewProductos.adapter = adapter

        listViewProductos.setOnItemClickListener { _, _, position, _ ->
            selectedProducto = productos[position]
            listViewProductos.showContextMenu()
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu_producto, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuEditarProducto -> {
                val intent = Intent(this, ActivityAgregarEditarProducto::class.java)
                intent.putExtra("supermercadoId", supermercadoId)
                intent.putExtra("productoId", selectedProducto?.id)
                startActivity(intent)
            }
            R.id.menuEliminarProducto -> {
                if (selectedProducto != null) {
                    controlador.eliminarProducto(selectedProducto!!.id)
                    Toast.makeText(this, "Producto eliminado", Toast.LENGTH_SHORT).show()
                    actualizarLista()
                }
            }
        }
        return super.onContextItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        actualizarLista()
    }
}
