package com.example.projeto.repositorio

import android.content.Context
import com.example.projeto.datasource.Authentication
import com.example.projeto.listener.ListenerAuth
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject


@ViewModelScoped
class AuthRepositorio @Inject constructor (private val auth: Authentication) {

    fun cadastro(email:String, senha:String, listenerAuth: ListenerAuth){

        auth.cadastroAluno(email,senha, listenerAuth)
    }
}