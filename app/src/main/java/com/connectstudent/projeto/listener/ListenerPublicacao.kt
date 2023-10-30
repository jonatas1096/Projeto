package com.connectstudent.projeto.listener

import com.connectstudent.projeto.datasource.UserData

interface ListenerPublicacao {

    fun onSucess(rm:String, cpsID:String,apelido:String, nome:String, turma:String){
        UserData.rmEncontrado = rm
        UserData.cpsIDEncontrado = cpsID
        UserData.apelidoUsuario = apelido
        UserData.nomeEncontrado = nome
        UserData.turmaEncontrada = turma
    }
    fun onFailure(erro:String)
}