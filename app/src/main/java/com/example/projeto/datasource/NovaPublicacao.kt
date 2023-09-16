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

        if (usuarioUID != null) {
            val alunoColecao = firestore.collection("Alunos")

            alunoColecao.whereEqualTo("usuarioID", usuarioUID)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        val rmEncontrado = documents.documents[0].getString("rm")
                        if (rmEncontrado != null) {
                            rm = rmEncontrado
                            println("Rm: $rm")
                            listenerPublicacao.onSucess(rm)
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

