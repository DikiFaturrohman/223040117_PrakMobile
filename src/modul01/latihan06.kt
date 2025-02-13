package modul01

fun main (){
    val greet = { name: String -> greeting(name) }
    greet("Diki")

}

fun greeting (name: String){
        println("Hello $name!")
}