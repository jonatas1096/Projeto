package com.connectstudent.projeto.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.connectstudent.projeto.listener.ListenerReconhecerUsuario
import com.connectstudent.projeto.repositorio.ReconhecerUsuarioRepositorio
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PublicacaoViewModel @Inject constructor(private val pubRepositorio: ReconhecerUsuarioRepositorio): ViewModel() {

    fun usuarioEncontrado(listenerReconhecerUsuario: ListenerReconhecerUsuario){

        viewModelScope.launch {

            pubRepositorio.encontrandoUsuario(listenerReconhecerUsuario)

        }

    }

}