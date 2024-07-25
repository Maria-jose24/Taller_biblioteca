package com.example.crudlibrary.models

data class libro(
    var id:String,
    var titulo:String,
    var autor:String,
    var isbn:String,
    var genero:String,
    var numero_de_ejemplares_disponibles:String,
    var numero_de_ejemplares_ocupados:String,
)