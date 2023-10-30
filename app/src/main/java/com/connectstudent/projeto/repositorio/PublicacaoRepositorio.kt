package com.connectstudent.projeto.repositorio


import com.connectstudent.projeto.datasource.NovaPublicacao
import com.connectstudent.projeto.listener.ListenerPublicacao
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class PublicacaoRepositorio@Inject constructor(private val pub: NovaPublicacao) {

    fun encontrandoUsuario(listenerPublicacao: ListenerPublicacao){

        pub.reconhecerUsuario(listenerPublicacao)
    }

}