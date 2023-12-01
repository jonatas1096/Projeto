package com.connectstudent.projeto.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.connectstudent.projeto.listener.ListenerAuth
import com.connectstudent.projeto.repositorio.AuthRepositorioCPS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

//classe criada ainda no processo de autenticação e criação dos usuarios:

@HiltViewModel
class AuthViewModelCPS @Inject constructor(private val authRepositorioCPS: AuthRepositorioCPS): ViewModel() {


    //Viewmodel dos professores/administração
    fun cpsCadastro(email: String, senha: String, id:String, codigoEtec:String, listenerAuth: ListenerAuth){

        viewModelScope.launch {
            authRepositorioCPS.cpsCadastro( email, senha, id, codigoEtec, listenerAuth)
        }

    }

    fun verificarUsuarioLogado(): Flow<Boolean> {
        return authRepositorioCPS.verificarUsuarioLogado()
    }
}