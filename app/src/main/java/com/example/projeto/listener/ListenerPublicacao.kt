package com.example.projeto.listener

import com.example.projeto.datasource.UserData

interface ListenerPublicacao {

    fun onSucess(rm:String, cpsID:String, nome:String){
        UserData.rmEncontrado = rm
        UserData.cpsIDEncontrado = cpsID
        UserData.nomeEncontrado = nome

    }
    fun onFailure(erro:String)
}