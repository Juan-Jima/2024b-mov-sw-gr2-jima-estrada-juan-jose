package com.example.gr2sw2024b_jjje

class BBaseDatosMemoria {
    companion object{
        var arregloBEntrenador = arrayListOf<BEntrenador>()
        init {
            arregloBEntrenador.add(BEntrenador(1,"Juan", "a@a.com"))
            arregloBEntrenador.add(BEntrenador(2,"María", "b@b.com"))
            arregloBEntrenador.add(BEntrenador(3,"Mateo", "c@c.com"))
        }
    }
}