package com.example.projeto.datasource

object UserData {
    var rmEncontrado: String = ""
    var cpsIDEncontrado: String = ""
    var nomeEncontrado: String = ""
    var imagemUrl: String = ""



    //Atualizar os dados padrões:
    fun setUserData(rm:String, cpsID:String, nome: String) {
        rmEncontrado = rm
        cpsIDEncontrado = cpsID
        nomeEncontrado = nome
    }

    //Atualizar a imagem:
    fun updateUrl(UrlObtida: String) {
        imagemUrl = UrlObtida
    }

}





