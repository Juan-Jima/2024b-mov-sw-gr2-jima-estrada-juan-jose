package org.example

import java.sql.Date

fun main(args: Array<String>) {
    //INMUTABLES (No se reasigna "=")
    val inmutable: String = "Juan";
    //inmutable = "Jose" //ERROR
    //MUTABLES
    var mutable: String = "Jose"
    mutable = "Juan" //OK
    //VAL > VAR

    //DUCK TYPING
    val ejemploVariable = "Juan Jima"
    ejemploVariable.trim()
    val edadEjemplo: Int = 12
    // ejemploVariable = edadEjemplo // Error!
    // Variables Primitivas
    val nombreProfesor: String = "Juan Jima"
    val sueldo: Double = 1.2
    val estadoCivil: Char = 'C'
    val mayorEdad: Boolean= true
    // Clases en Java
    val fechaNacimiento: java.util.Date = java.util.Date()


    // when switch
    val estadoCivilWhen = "C"
    when (estadoCivilWhen) {
        ("C") -> {
            println("Casado")
        }
        "S" -> {
            println("Soltero")
        }
        else -> {
            println("No sabemos")
        }
    }
    val esSoltero = (estadoCivilWhen == "S")
    val coqueteo = if (esSoltero) "Si" else "No" // if else chiquito

    imprimirNombre("Juan");

    calcularSueldo(10.00) //solo parametro requerido
    calcularSueldo(10.00,15.00,20.00)//parametro requerido y sobreeecribir parametros
    //named parameters
    //calcularSueldo(sueldo,tasa, bonoEspecial)
    calcularSueldo(10.00, bonoEspecial = 20.00)
    //gracias a los parametros nombrados
    calcularSueldo(bonoEspecial = 20.00, sueldo = 10.00, tasa = 14.00)
    //usando el parametro bonoEspecial en 1ra posicion
    //usando el parametro sueldo en 2da posicion
    //usando el parametro tasa en 3ra posicion
    //gracias a los parametros nombrados

    val SumaA = Suma(1,1)
    val SumaB = Suma(null,1)
    val SumaC = Suma(1,null)
    val SumaD = Suma(null,null)
    SumaA.sumar()
    SumaB.sumar()
    SumaC.sumar()
    SumaD.sumar()
    println(Suma.pi)
    println(Suma.elevarAlCuadrado(2))
    println(Suma.historialSumas)


    // Arreglos
    // Estaticos

    val arregloEstatico: Array<Int> = arrayOf<Int>(1,2,3)
    println(arregloEstatico);

    //Dinamicos
    val arregloDinamico: ArrayList<Int> = arrayListOf<Int>(
        1,2,3,4,5,6,7,8,9,10
    )
    println(arregloDinamico)
    arregloDinamico.add(11)
    arregloDinamico.add(12)
    println(arregloDinamico)


    // FOR EACH = > Unit
    // Iterar un arreglo

    val respuestaForEach: Unit = arregloDinamico
        .forEach { valorActual: Int ->
            println("Valor actual: ${valorActual}");
        }

    // "it" (en ingles "eso") significa el elemento iterado
    arregloDinamico.forEach { println("Valor Actual (it): ${it}")}

    // MAP -> MUTA(Modifica cambia) el arreglo
    // 1) Enviamos el nuevo valor de la iteración
    // 2) Nos devuelve un nuevo arreglo con VALORES de las iteraciones

    val respuestaMap: List<Double> = arregloDinamico
        .map { valorActual: Int ->
            return@map valorActual.toDouble() + 100.00
        }

    println(respuestaMap)
    val respuestaMapDos = arregloDinamico.map{ it + 15 }
    println(respuestaMapDos)

    // FILTER -> Filtrar el arreglo
    // 1) Devolver una expresion (TRUE o FALSE)
    // 2) Nuevo arreglo Filtrado

    val respuestaFilter: List<Int> = arregloDinamico
        .filter { valorActual: Int ->
            //Expresión o Condicion
            val mayoresACinco: Boolean = valorActual > 5
            return@filter mayoresACinco
        }

    val respuestaFilterDos = arregloDinamico.filter{ it <= 5}
    println(respuestaFilter)
    println(respuestaFilterDos)

    // OR AND
    // OR -> ANY (Alguno Cumple?)
    // And -> All (Todos Cumplen?)
    val respuestaAny: Boolean = arregloDinamico
        .any{ valorActual: Int ->
            return@any (valorActual > 5)
        }
    println(respuestaAny) //True

    val respuestaAll: Boolean = arregloDinamico
        .all{ valorActual:Int ->
            return@all (valorActual > 5)
        }
    println(respuestaAll) //False


    // Reduce -> valor acumulado
    // Valor acumulado = 0 (Siempre empieza en 0 en Kotlin)
    // [1,2,3,4,5] -> Acumular "Sumar" estos valores del arreglo
    // ValorIteracion1 = valorEmpieza + 1 = 0 + 1 = 1 -> Iteracion1
    // ValorIteracion2 = valorAcumuladoIteracion1 + 2 = 1 + 2 = 3 -> Iteracion2
    // ValorIteracion1 = valorAcumuladoIteracion2 + 3 = 3 + 3 = 6 -> Iteracion3
    // ValorIteracion1 = valorAcumuladoIteracion3 + 4 = 6 + 4 = 10 -> Iteracion4
    // ValorIteracion1 = valorAcumuladoIteracion4 + 5 = 10 + 5 = 15 -> Iteracion5

    val respuestaReduce: Int = arregloDinamico
        .reduce{ acumulado:Int, valorActual: Int ->
            return@reduce (acumulado + valorActual) // Cambiar o usar la logica
        }
    println(respuestaReduce);
    //Return@reduce acumulado + (itemCarrito.cantidad * itemCarrito.precio)







}

fun imprimirNombre(nombre:String): Unit{
    fun otraFuncionAdentro():Unit{
        println("Otra funcion adentro")
    }
    println("Nombre: $nombre") // Uso sin llaves
    println("Nombre: ${nombre}") // Uso con llaves opcional
    println("Nombre: ${nombre + nombre}") // Uso con llaves concatenado
    println("Nombre: ${nombre.toString()}") // Uso con llaves funcion
    println("Nombre: $nombre.toString()") // INCORRECTO
    //NO se puede usar sin llaves
    otraFuncionAdentro()
}

fun calcularSueldo(
    sueldo: Double, //Requerido
    tasa: Double = 12.00, // Opcional(Defecto)
    bonoEspecial:Double? = null // Opcion (nullable)
    // Variable -> "?" Es nullable (osea que puede en algun momento ser nulo)
): Double {
    //Int -> Int? (nullable)
    //String -> String?(nullable)
    //Date -> Date? (nullable)
    if(bonoEspecial == null) {
        return sueldo * (100/tasa)
    } else {
        return sueldo * (100/tasa) * bonoEspecial
    }

}

abstract class Numeros(
    protected val numeroUno: Int,
    protected val numeroDos: Int,
    parametroNoUsadoNoPropiedadDeLaClase: Int? = null
){
    init {
        this.numeroUno
        this.numeroDos
        println("Inicializando")
    }
}

class Suma( //Constructor primario
    unoParametro: Int, //Parametro
    dosParametro: Int, //Parametros
): Numeros( //Clase papa, Numeros (extendiendo)
    unoParametro,
    dosParametro
){
    public val soyPublicoExplicito: String = "Publicas"
    val soyPublicoImplicite: String = "Publico implicito"

    init { // bloque constructor primario
        this.numeroUno
        this.numeroDos
        numeroUno // el this es opcional [propiedades, metodos]
        numeroDos // el this es opcional [propiedades, metodos]
        this.soyPublicoImplicite
        soyPublicoExplicito
    }
    constructor( // constructor secundario
        uno: Int?,
        dos: Int,
    ):this (
        if (uno == null) 0 else uno,
        dos
    ){
        //Bloque de codigo de construccion secundario

    }
    constructor( // constructor secundario
        uno: Int,
        dos: Int?, // Entero nullable
    ):this (
        uno,
        if (dos == null) 0 else dos,
    )

    constructor( // constructor secundario
        uno: Int?, // Entero nullable
        dos: Int?, // Entero nullable
    ):this (
        if (uno == null) 0 else uno,
        if (dos == null) 0 else dos,
    )

    fun sumar ():Int{
        val total = numeroUno + numeroDos
        agregarHistorial(total)
        return total
    }

    companion object { //Comparte entre todas las instancias similar al STATIC
        //funciones variables
        //NombreClase.metodo, NombreClase.funcion =
        //Suma.pi
        val pi = 3.14
        //Suma.elevarAlCuadrado

        fun elevarAlCuadrado (num:Int):Int{return num * num}
        val historialSumas = arrayListOf<Int>()

        fun agregarHistorial(valorTotalSuma:Int){ //Suma.agregarHistorial
            historialSumas.add(valorTotalSuma)
        }
    }
}