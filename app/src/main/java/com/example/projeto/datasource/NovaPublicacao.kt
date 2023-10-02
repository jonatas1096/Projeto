package com.example.projeto.datasource

import android.annotation.SuppressLint
import android.media.Image
import androidx.compose.runtime.Composable
import com.example.projeto.listener.ListenerAuth
import com.example.projeto.listener.ListenerPublicacao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject


class NovaPublicacao  @Inject constructor() {

    //Essa classe separada vem com o objetivo de montar a lógica de uma nova publicação, mas, antes de tudo precisei implementar um
    //método para obter dados do usuários logado e saber quem está usando o app.
    //Então, primeiro separei uma função para reconhecer quem está logado:
  //  @SuppressLint("SuspiciousIndentation")
    fun reconhecerUsuario(listenerPublicacao:ListenerPublicacao){
        //Iniciando o banco de dados
        val firestore = FirebaseFirestore.getInstance()

        //Pegando o uid do usuário logado atualmente
        var usuarioUID = FirebaseAuth.getInstance().currentUser?.uid.toString()

        var rm = "" //vamo armazenar aqui o rm depois
        var cpsID = "" //aqui armazenaremos o ID do professor
        var nomeEncontrado = "" //e aqui o nome


        //Começando procurando nos alunos
        if (rm != null) {
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
                            listenerPublicacao.onSucess(rm,cpsID,nomeEncontrado)
                        }

                        //Pegando o nome
                        val rmData = firestore.collection("Data")
                        val rmDocument = rmData.document("RM")
                        var nomeEncontrado = ""
                            rmDocument.get()
                            .addOnSuccessListener {document ->
                                if (document.exists() && document != null){

                                    val arrayRM = document.get("$rm") as? List<String>

                                    if (arrayRM != null && arrayRM.size > 1){
                                        nomeEncontrado = arrayRM[1]
                                        println("O nome: $nomeEncontrado")
                                    }
                                    listenerPublicacao.onSucess(rm,cpsID,nomeEncontrado)
                                }
                                else{
                                    println("O array nao existe ou está vazio.")
                                }
                            }
                            .addOnFailureListener {
                                println("Não encontrou o documento")
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
        if(cpsID != null){
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
                            listenerPublicacao.onSucess(rm,cpsID,nomeEncontrado)
                        }


                        //Pegando o nome
                        val cpsData = firestore.collection("Data")
                        val cpsDocument = cpsData.document("ID")
                        var nomeEncontrado = ""
                        cpsDocument.get()
                            .addOnSuccessListener {document ->
                                if (document.exists() && document != null){
                                    val arrayID = document.get("$cpsID") as? List<String>

                                    if (arrayID != null && arrayID.size > 1){
                                        nomeEncontrado = arrayID[1]
                                    }
                                    listenerPublicacao.onSucess(rm,cpsID,nomeEncontrado)
                                }
                                else{
                                    println("O array nao existe ou está vazio.")
                                }
                            }
                            .addOnFailureListener {
                                println("Não encontrou o documento")
                            }
                    }
                    else {
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


}

