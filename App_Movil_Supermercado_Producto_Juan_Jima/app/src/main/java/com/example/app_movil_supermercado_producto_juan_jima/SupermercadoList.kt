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

class ActivitySupermercadoList : AppCompatActivity() {
    private lateinit var controlador: Controlador
    private lateinit var listViewSupermercados: ListView
    private lateinit var btnAgregarSupermercado: Button
    private var selectedSupermercado: Supermercado? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supermercado_list)

        controlador = Controlador(this)
        listViewSupermercados = findViewById(R.id.listViewSupermercados)
        btnAgregarSupermercado = findViewById(R.id.btnAgregarSupermercado)

        btnAgregarSupermercado.setOnClickListener {
            val intent = Intent(this, ActivityAgregarEditarSupermercado::class.java)
            startActivity(intent)
        }

        registerForContextMenu(listViewSupermercados)
        actualizarLista()
    }

    private fun actualizarLista() {
        val supermercados = controlador.listarSupermercados()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, supermercados.map { it.nombre })
        listViewSupermercados.adapter = adapter

        listViewSupermercados.setOnItemClickListener { _, _, position, _ ->
            selectedSupermercado = supermercados[position]
            listViewSupermercados.showContextMenu()
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu_supermercado, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuVerProductos -> {
                val intent = Intent(this, ActivityProductoList::class.java)
                intent.putExtra("supermercadoId", selectedSupermercado?.id)
                startActivity(intent)
            }
            R.id.menuEditarSupermercado -> {
                val intent = Intent(this, ActivityAgregarEditarSupermercado::class.java)
                intent.putExtra("supermercadoId", selectedSupermercado?.id)
                startActivity(intent)
            }
            R.id.menuEliminarSupermercado -> {
                if (selectedSupermercado != null) {
                    controlador.eliminarSupermercado(selectedSupermercado!!.id)
                    Toast.makeText(this, "Supermercado eliminado", Toast.LENGTH_SHORT).show()
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
