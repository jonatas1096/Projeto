package com.connectstudent.projeto.listener

import com.connectstudent.projeto.datasource.UserData

interface ListenerReconhecerUsuario {

    fun onSucess(rm:String, cpsID:String,apelido:String, nome:String, turma:String, codigoEtec:String){
        UserData.rmEncontrado = rm
        UserData.cpsIDEncontrado = cpsID
        UserData.apelidoUsuario = apelido
        UserData.nomeEncontrado = nome
        UserData.turmaEncontrada = turma
        UserData.codigoEncontrado = codigoEtec
    }
    fun onFailure(erro:String)
}