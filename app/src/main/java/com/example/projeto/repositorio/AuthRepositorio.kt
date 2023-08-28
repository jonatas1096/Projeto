package com.example.projeto.repositorio

import com.example.projeto.datasource.Authentication
import com.example.projeto.listener.ListenerAuth
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject


@ViewModelScoped
class AuthRepositorio @Inject constructor(private val auth: Authentication) {

    fun cadastro(nome:String, email: String, senha: String, rm:String, codigoturma:String, listenerAuth: ListenerAuth){

        auth.cadastroAluno(nome, email, senha,rm, codigoturma, listenerAuth)
    }
}