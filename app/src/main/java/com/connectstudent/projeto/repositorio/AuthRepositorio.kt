package com.connectstudent.projeto.repositorio

import com.connectstudent.projeto.datasource.Authentication
import com.connectstudent.projeto.listener.ListenerAuth
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


@ViewModelScoped
class AuthRepositorio @Inject constructor(private val auth: Authentication) {

    //Repositório dos dados dos alunos
    fun cadastro(email: String, senha: String, rm:String, listenerAuth: ListenerAuth){

        auth.cadastroAluno(email, senha,rm, listenerAuth)
    }

    fun login(email: String, senha: String, listenerAuth: ListenerAuth){
        auth.loginAluno(email,senha,listenerAuth)
    }
    ///////

    fun verificarUsuarioLogado(): Flow<Boolean>{
        return auth.verificarUsuarioLogado()
    }
}