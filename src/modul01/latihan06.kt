package modul01

fun main (){
    val greet = { name: String -> greetings(name) }
    greet("Diki")

}

fun greetings (name: String){
        println("Hello $name!")
}