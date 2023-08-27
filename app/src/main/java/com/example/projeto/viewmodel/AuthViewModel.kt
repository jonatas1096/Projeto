package com.example.projeto.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projeto.listener.ListenerAuth
import com.example.projeto.repositorio.AuthRepositorio
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

//classe criada ainda no processo de autenticação e criação dos usuarios:

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepositorio: AuthRepositorio): ViewModel() {

    fun cadastro(email:String, senha:String, listenerAuth: ListenerAuth){
        viewModelScope.launch {
            authRepositorio.cadastro(email,senha,listenerAuth)
        }
    }
}