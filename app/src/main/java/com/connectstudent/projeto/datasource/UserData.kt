package com.connectstudent.projeto.datasource

object UserData {
    var rmEncontrado: String = ""
    var cpsIDEncontrado: String = ""
    var nomeEncontrado: String = ""
    var turmaEncontrada: String = ""
    var imagemUrl: String = ""
    var UID: String = ""
    var emailEncontrado: String = ""
    var apelidoUsuario: String = ""
    var codigoEncontrado: String = ""


    //Atualizar os dados padrões:
    fun setUserData(
        rm: String,
        cpsID: String,
        nome: String,
        turma: String,
        uid: String,
        email: String,
        codigoEtec: String
    ) {
        rmEncontrado = rm
        cpsIDEncontrado = cpsID
        nomeEncontrado = nome
        turmaEncontrada = turma
        UID = uid
        emailEncontrado = email
        codigoEncontrado = codigoEtec
    }

    //Atualizar a imagem:
    fun updateUrl(UrlObtida: String) {
        imagemUrl = UrlObtida
    }

    //Atualizar apelido do usuário:
    fun setApelido(apelido: String) {
        apelidoUsuario = apelido
    }

}





