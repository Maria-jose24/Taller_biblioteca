package com.example.crudlibrary.config

class config {
    //se crea una url static, para consultar sin instanciar metedo companion object sirve para almacenar
    companion object{
        //ejemplo: se coloca la pi1 del wifi que debe estar conectado con la misma red del celular y se coloca el puerto de la bses de datos
        var urlbBase="http://192.168.47.153:8085/api/v1/"
        var urlLibros= urlbBase+"libro/"
        var urlUsuarios= urlbBase+"usuario/"
    }
}