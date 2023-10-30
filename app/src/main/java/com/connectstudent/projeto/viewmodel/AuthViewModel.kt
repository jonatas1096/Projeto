package com.connectstudent.projeto.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.connectstudent.projeto.listener.ListenerAuth
import com.connectstudent.projeto.repositorio.AuthRepositorio
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

//classe criada ainda no processo de autenticação e criação dos usuarios:

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepositorio: AuthRepositorio): ViewModel() {

    //View model dos alunos
    fun cadastro(email: String, senha: String, rm:String, listenerAuth: ListenerAuth){
        viewModelScope.launch {
            authRepositorio.cadastro(email, senha,rm, listenerAuth)
        }
    }

    fun login(email: String, senha: String, listenerAuth: ListenerAuth){
        authRepositorio.login(email,senha,listenerAuth)
    }
    /////////

    fun verificarUsuarioLogado(): Flow<Boolean> {
        return authRepositorio.verificarUsuarioLogado()
    }
}