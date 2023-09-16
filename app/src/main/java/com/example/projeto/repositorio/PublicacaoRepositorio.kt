package com.example.projeto.repositorio


import com.example.projeto.datasource.NovaPublicacao
import com.example.projeto.listener.ListenerPublicacao
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class PublicacaoRepositorio@Inject constructor(private val pub: NovaPublicacao) {

    fun rmUsuario(listenerPublicacao: ListenerPublicacao){

        pub.reconhecerUsuario(listenerPublicacao)
    }

}