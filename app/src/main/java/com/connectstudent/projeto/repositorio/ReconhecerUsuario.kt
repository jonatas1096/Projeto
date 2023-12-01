package com.connectstudent.projeto.repositorio


import com.connectstudent.projeto.datasource.ReconhecerUsuario
import com.connectstudent.projeto.listener.ListenerReconhecerUsuario
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class ReconhecerUsuarioRepositorio@Inject constructor(private val pub: ReconhecerUsuario) {

    fun encontrandoUsuario(listenerReconhecerUsuario: ListenerReconhecerUsuario){

        pub.reconhecerUsuario(listenerReconhecerUsuario)
    }

}