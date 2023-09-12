package com.example.projeto.datasource

import android.media.Image
import com.google.firebase.firestore.FirebaseFirestore

class NovaPublicacao {

    //Iniciando o banco de dados
    private val db = FirebaseFirestore.getInstance()

    fun novaPublicacao(nome:String, texto:String, titulo:String, foto: Image){

        val postsMap = hashMapOf(
            "nome" to nome,
            "texto" to texto,
            "titulo" to titulo,
            "foto" to foto
        )
        db.collection("Posts").document("titulo").set(postsMap)


    }
}