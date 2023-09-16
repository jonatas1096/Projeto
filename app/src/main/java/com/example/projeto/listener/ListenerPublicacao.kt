package com.example.projeto.listener

interface ListenerPublicacao {


    fun onSucess(rm:String)
    fun onFailure(erro:String)
}