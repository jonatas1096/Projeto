package com.connectstudent.projeto.repositorio


import com.connectstudent.projeto.datasource.AuthenticationCPS
import com.connectstudent.projeto.listener.ListenerAuth
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


@ViewModelScoped
class AuthRepositorioCPS @Inject constructor(private val auth: AuthenticationCPS) {


    //Repositório dos dados de professores/administração
    fun cpsCadastro(email: String, senha: String, id:String, codigoEtec:String, listenerAuth: ListenerAuth){
        auth.cadastroCps(email, senha, id, codigoEtec, listenerAuth)
    }

    fun verificarUsuarioLogado(): Flow<Boolean> {
        return auth.verificarUsuarioLogado()
    }
}