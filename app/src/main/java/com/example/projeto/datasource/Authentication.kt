package com.example.projeto.datasource

import android.content.Context
import android.widget.Toast
import com.example.projeto.listener.ListenerAuth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject


//essa classe aqui está servindo apenas como autenticação de um cadastro. É praticamente pronto do firebase.
class Authentication @Inject constructor() {

    //iniciando o serviço de autenticação do firebase, é preciso fazer isso aqui:
    val auth = FirebaseAuth.getInstance()
    //Usado para interagir com o Firestore
    val firestore = FirebaseFirestore.getInstance()
    //Usado para acessar um documento que eu mesmo criei no firebase
    val dataCollection = firestore.collection("Data")



    //Primeiro o cadastro do aluno:
    fun cadastroAluno(email: String, senha: String, listenerAuth: ListenerAuth) {

        /*Acessando o array RM dentro da coleção para buscar o aluno e validar:
        dataCollection.document("RM").get()*/

        if (email.isEmpty()){
            listenerAuth.onFailure("O email não pode estar vazio!")
        }
        else if(senha.isEmpty()){
            listenerAuth.onFailure("Insira uma senha válida!")
        }
        else{
            auth.createUserWithEmailAndPassword(email,senha).addOnCompleteListener {cadastro ->
                if(cadastro.isSuccessful){
                    listenerAuth.onSucess("Pronto!")
                }

            }

        }

     }
}