package com.example.projeto.datasource

import android.content.Context
import android.widget.Toast
import com.example.projeto.listener.ListenerAuth
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject


//essa classe aqui está servindo apenas como autenticação de um cadastro. É praticamente pronto do firebase.
class Authentication @Inject constructor() {

    //iniciando o serviço de autenticação do firebase, é preciso fazer isso aqui:
    val auth = FirebaseAuth.getInstance()

    //Primeiro o cadastro do aluno:
    fun cadastroAluno(email: String, senha: String, listenerAuth: ListenerAuth) {
        if (email.isEmpty()){
            listenerAuth.onFailure("O email está vazio!")
        }
        else if (senha.isEmpty()){
            listenerAuth.onFailure("A senha está vazia!")
        }
        else{
            auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener{cadastro ->
                if (cadastro.isSuccessful){
                    listenerAuth.onSucess("Usuário cadastrado com sucesso!")
                }
            }

        }
    }

}