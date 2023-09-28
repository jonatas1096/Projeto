package com.example.projeto.datasource

object UserData {
    var rmEncontrado: String = ""
    var cpsIDEncontrado: String = ""
    var nomeEncontrado: String = ""
    var imagemUrl: String = ""
    var UID: String = ""
    var emailEncontrado: String = ""



    //Atualizar os dados padrões:
    fun setUserData(rm:String, cpsID:String, nome: String, uid: String, email: String) {
        rmEncontrado = rm
        cpsIDEncontrado = cpsID
        nomeEncontrado = nome
        UID = uid
        emailEncontrado = email
    }

    //Atualizar a imagem:
    fun updateUrl(UrlObtida: String) {
        imagemUrl = UrlObtida
    }

}





