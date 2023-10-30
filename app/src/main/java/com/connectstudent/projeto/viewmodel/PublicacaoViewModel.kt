package com.connectstudent.projeto.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.connectstudent.projeto.listener.ListenerPublicacao
import com.connectstudent.projeto.repositorio.PublicacaoRepositorio
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PublicacaoViewModel @Inject constructor(private val pubRepositorio: PublicacaoRepositorio): ViewModel() {

    fun usuarioEncontrado(listenerPublicacao: ListenerPublicacao){

        viewModelScope.launch {

            pubRepositorio.encontrandoUsuario(listenerPublicacao)

           /* val (usuario, nome) = UserData
            UserData.usuarioEncontrado = usuario
            UserData.nomeEncontrado = nome*/
        }

    }

}