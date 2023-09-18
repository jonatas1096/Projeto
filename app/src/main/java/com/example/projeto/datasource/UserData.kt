package com.example.projeto.datasource

object UserData {
    var rmEncontrado: String = ""
    var cpsIDEncontrado: String = ""
    var nomeEncontrado: String = ""



    fun setUserData(rm:String, cpsID:String, nome: String) {
        rmEncontrado = rm
        cpsIDEncontrado = cpsID
        nomeEncontrado = nome
    }

}





