package com.example.projeto.repositorio

import com.example.projeto.datasource.Authentication
import com.example.projeto.listener.ListenerAuth
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject


@ViewModelScoped
class AuthRepositorio @Inject constructor(private val auth: Authentication) {

    //Repositório dos dados dos alunos
    fun cadastro(nome:String, email: String, senha: String, rm:String, codigoturma:String, listenerAuth: ListenerAuth){

        auth.cadastroAluno(nome, email, senha,rm, codigoturma, listenerAuth)
    }

    fun login(email: String, senha: String, listenerAuth: ListenerAuth){
        auth.loginAluno(email,senha,listenerAuth)
    }
    ///////



    //Repositório dos dados de professores/administração
    fun cpsCadastro(nome:String, email: String, senha: String, id:String, codigoEtec:String, listenerAuth: ListenerAuth){
        auth.cadastroCps(nome, email, senha, id, codigoEtec, listenerAuth)
    }
}