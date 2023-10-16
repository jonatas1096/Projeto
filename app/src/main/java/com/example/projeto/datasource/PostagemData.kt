package com.example.projeto.datasource


data class PostagemData(
    val fotoPerfil: String,
    val nomeAutor: String,
    val rm: String,
    val apelidoAutor: String,
    val textoPostagem: String,
    val imagensPost: List<String>,
    val tituloPost: String,
    val turmasMarcadas: List<String>,
)
