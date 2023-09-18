package com.example.projeto.datasource

import android.media.Image
import com.example.projeto.listener.ListenerAuth
import com.example.projeto.listener.ListenerPublicacao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject


class NovaPublicacao  @Inject constructor() {

    //Primeiro separei uma função para reconhecer quem está logado
    fun reconhecerUsuario(listenerPublicacao:ListenerPublicacao){
        //Iniciando o banco de dados
        val firestore = FirebaseFirestore.getInstance()

        //Pegando o uid do usuário logado atualmente
        var usuarioUID = FirebaseAuth.getInstance().currentUser?.uid.toString()

        var rm = "" //vamo armazenar aqui o rm depois
        var cpsID = "" //aqui armazenaremos o ID do professor
        var nome = "" //e aqui o nome


        //Começando procurando nos alunos
        if (usuarioUID != null) {
            val usuarioColecao = firestore.collection("Alunos")

            usuarioColecao.whereEqualTo("usuarioID", usuarioUID)
                .get()
                .addOnSuccessListener { documents ->
                    println("Entrou no documento para alunos")
                    if (!documents.isEmpty) { //(caso não esteja vazio)
                        //Pegando o RM
                        val rmEncontrado = documents.documents[0].getString("rm")
                        if (rmEncontrado != null) {
                            rm = rmEncontrado
                            listenerPublicacao.onSucess(rm,cpsID,nome)
                        }

                        //Pegando o nome
                        val nomeEncontrado = documents.documents[0].getString("nome")
                        if (nomeEncontrado != null) {
                            nome = nomeEncontrado
                            listenerPublicacao.onSucess(rm,cpsID,nome)
                        }

                    } else {
                        // Nenhum documento com esse UID encontrado
                       listenerPublicacao.onFailure("Nenhum rm foi encontrado.")
                    }

                }
                .addOnFailureListener { exception ->
                    // Qualquer falha
                    println("Erro aleatório: $exception")
                }
        }

        //Agora procurando nos professores
        if (usuarioUID != null) {
            val usuarioColecao = firestore.collection("Cps")
            usuarioColecao.whereEqualTo("cpsID", usuarioUID)
                .get()
                .addOnSuccessListener { documents ->

                    println("Entrou no documento para professores")
                    if (!documents.isEmpty) { //(caso não esteja vazio)
                        //Pegando o cpsID
                        val cpsidEncontrado = documents.documents[0].getString("id")
                        if (cpsidEncontrado != null) {
                            cpsID = cpsidEncontrado
                            println("cpsID: $cpsID")
                            listenerPublicacao.onSucess(rm,cpsID,nome)
                        }

                        //Pegando o nome
                        val nomeEncontrado = documents.documents[0].getString("nome")
                        if (nomeEncontrado != null) {
                            nome = nomeEncontrado
                            println("Nome: $nome")
                            listenerPublicacao.onSucess(rm,cpsID,nome)
                        }

                    } else {
                        // Nenhum documento com esse UID encontrado
                        listenerPublicacao.onFailure("Nenhum cps ID foi encontrado.")
                    }

                }
                .addOnFailureListener { exception ->
                    // Qualquer falha
                    println("Erro aleatório: $exception")
                }
        }
    }



    /*fun criarPublicacao(texto:String, titulo:String, listenerPublicacao: ListenerPublicacao){




        if (titulo.isEmpty()) {
            ListenerPublicacao.onSucess("Insira o título da sua publicação..")
        } else if (texto.isEmpty()) {
            ListenerPublicacao.onFailure("Insira a descrição da publicação.")
        }

        /*val postsMap = hashMapOf(
            "nome" to nome,
            "texto" to texto,
            "titulo" to titulo,
            "foto" to foto,
            "rm" to rm
        )


        db.collection("Posts").document("titulo").set(postsMap)*/


    }*/


}

