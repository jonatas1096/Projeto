package com.connectstudent.projeto.datasource


data class PostagemData(
    val fotoPerfil: String,
    val nomeAutor: String,
    val rm: String,
    val cpsID:String,
    val textoPostagem: String,
    val imagensPost: List<String>,
    val tituloPost: String,
    val turmasMarcadas: List<String>,
    val idPostagem: String,
    var curtidas:Int,
    var comentarios:Int,
)
