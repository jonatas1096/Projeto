package com.example.projeto.listener

interface ListenerPublicacao {


    fun onSucess(mensagem:String)
    fun onFailure(erro:String)
}