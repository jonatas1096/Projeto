package com.connectstudent.projeto.datasource

import android.annotation.SuppressLint
import com.connectstudent.projeto.listener.ListenerReconhecerUsuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject


class ReconhecerUsuario  @Inject constructor() {

    //Essa classe separada vem com o objetivo de montar a lógica de uma nova publicação, mas, antes de tudo precisei implementar um
    //método para obter dados do usuários logado e saber quem está usando o app.
    //Então, primeiro separei uma função para reconhecer quem está logado:
  //  @SuppressLint("SuspiciousIndentation")
    @SuppressLint("SuspiciousIndentation")
    fun reconhecerUsuario(listenerReconhecerUsuario:ListenerReconhecerUsuario){
        //Iniciando o banco de dados
        val firestore = FirebaseFirestore.getInstance()

        //Pegando o uid do usuário logado atualmente
        var usuarioUID = FirebaseAuth.getInstance().currentUser?.uid.toString()

        var rm = "" //vamo armazenar aqui o rm depois
        var cpsID = "" //aqui armazenaremos o ID do professor
        var apelidoEncontrado = ""
        var nomeEncontrado = "" //aqui o nome pré-definido
        var turmaEncontrada = "" //e aqui a turma do aluno
        var codigoEtec = "" //codigo etec ne


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
                            //listenerReconhecerUsuario.onSucess(rm,cpsID,apelidoEncontrado,nomeEncontrado, turmaEncontrada)
                        }

                        //Pegando o apelido
                        val apelidoUsuario = documents.documents[0].getString("apelido")
                        if (apelidoUsuario != null){
                            apelidoEncontrado = apelidoUsuario
                           //listenerReconhecerUsuario.onSucess(rm,cpsID,apelidoEncontrado,nomeEncontrado, turmaEncontrada)
                        }

                        //Pegando o nome e o código da Etec juntos
                        val rmData = firestore.collection("Data")
                        val rmDocument = rmData.document("RM")
                            rmDocument.get()
                            .addOnSuccessListener {document ->
                                if (document.exists() && document != null){

                                    val arrayRM = document.get("$rm") as? List<String>

                                    if (arrayRM != null && arrayRM.size > 1){
                                        nomeEncontrado = arrayRM[1]
                                        turmaEncontrada = arrayRM[2]
                                        codigoEtec = arrayRM[3]
                                        println("O nome: $nomeEncontrado")
                                        println("O nome: $turmaEncontrada")
                                    }
                                    listenerReconhecerUsuario.onSucess(rm,cpsID,apelidoEncontrado,nomeEncontrado, turmaEncontrada, codigoEtec)
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
                       listenerReconhecerUsuario.onFailure("Nenhum rm foi encontrado.")
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
                            //listenerReconhecerUsuario.onSucess(rm,cpsID,apelidoEncontrado,nomeEncontrado, turmaEncontrada)
                        }

                        //Pegando o apelido
                        val apelidoUsuario = documents.documents[0].getString("apelido")
                        if (apelidoUsuario != null){
                            apelidoEncontrado = apelidoUsuario
                            //listenerReconhecerUsuario.onSucess(rm,cpsID,apelidoEncontrado,nomeEncontrado, turmaEncontrada)
                        }

                        //Pegando o nome e o código da Etec juntos
                        val cpsData = firestore.collection("Data")
                        val cpsDocument = cpsData.document("ID")
                        var nomeEncontrado = ""
                        cpsDocument.get()
                            .addOnSuccessListener {document ->
                                if (document.exists() && document != null){
                                    val arrayID = document.get("$cpsID") as? List<String>

                                    if (arrayID != null && arrayID.size > 1){
                                        nomeEncontrado = arrayID[1]
                                        codigoEtec = arrayID[2]
                                    }
                                    listenerReconhecerUsuario.onSucess(rm,cpsID,apelidoEncontrado,nomeEncontrado, turmaEncontrada, codigoEtec)
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
                        listenerReconhecerUsuario.onFailure("Nenhum cps ID foi encontrado.")
                    }

                }
                .addOnFailureListener { exception ->
                    // Qualquer falha
                    println("Erro aleatório: $exception")
                }
        }
    }


}

