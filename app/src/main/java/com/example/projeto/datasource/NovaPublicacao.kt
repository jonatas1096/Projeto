package com.example.projeto.datasource

import android.media.Image
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class NovaPublicacao  @Inject constructor() {

    fun criarPublicacao(nome:String, titulo:String, texto:String, foto: Image, rm: String){

        //Iniciando o banco de dados
        val firestore = FirebaseFirestore.getInstance()

        //Pegando o uid do usuário logado atualmente
        var usuarioUID = FirebaseAuth.getInstance().currentUser?.uid.toString()

        var rm : String //vamo armazenar aqui o rm depois

        if (usuarioUID != null) {
            val alunoColecao = firestore.collection("Alunos")

            alunoColecao.whereEqualTo("usuarioID", usuarioUID)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        val rmEncontrado = documents.documents[0].getString("rm")
                        if (rmEncontrado != null) {
                            rm = rmEncontrado
                            println("Nome: $rm")
                        }
                    } else {
                        // Nenhum documento com esse UID encontrado
                        println("Não encontrou nenhum documento com o UID $usuarioUID")
                    }
                }
                .addOnFailureListener { exception ->
                    // Qualquer falha
                    println("Erro aleatório: $exception")
                }
        }

        /*val postsMap = hashMapOf(
            "nome" to nome,
            "texto" to texto,
            "titulo" to titulo,
            "foto" to foto,
            "rm" to rm
        )


        db.collection("Posts").document("titulo").set(postsMap)*/


    }
}