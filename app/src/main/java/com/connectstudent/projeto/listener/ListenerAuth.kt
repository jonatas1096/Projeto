package com.connectstudent.projeto.listener

interface ListenerAuth {
//essa interface é para utilizar junto da autenticação do firebase, ela vai me ajudar a retornar algumas mensagens

    fun onSucess(mensagem:String)
    fun onFailure(erro:String)
}