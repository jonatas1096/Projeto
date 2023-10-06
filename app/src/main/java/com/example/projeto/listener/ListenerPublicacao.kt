package com.example.projeto.listener

import com.example.projeto.datasource.UserData
import com.google.firebase.firestore.auth.User

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