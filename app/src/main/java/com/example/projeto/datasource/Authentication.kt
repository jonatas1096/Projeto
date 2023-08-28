package com.example.projeto.datasource

import android.content.Context
import android.widget.Toast
import com.example.projeto.listener.ListenerAuth
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject


//essa classe aqui está servindo apenas como autenticação de um cadastro. É praticamente pronto do firebase.
class Authentication @Inject constructor() {

    //iniciando o serviço de autenticação do firebase, é preciso fazer isso aqui:
    val auth = FirebaseAuth.getInstance()

    //Usado para interagir com o Firestore, aqui conseguimos usar isso para puxar os dados dele
    val firestore = FirebaseFirestore.getInstance()


    //Primeiro o cadastro do aluno:
    fun cadastroAluno(
        nome: String,
        email: String,
        senha: String,
        rm: String,
        codigoturma: String,
        listenerAuth: ListenerAuth
    ) {

        val rmBox = rm

        firestore.collection("Data")
            .get()
            .addOnSuccessListener { querySnapshot ->
                var rmCondicao = false

                for (document in querySnapshot.documents) {
                    val rmArray = document.get("item_lista") as? List<String>
                    println("$rmArray")
                    if (rmArray != null && rmBox in rmArray) {
                        // O RM está previamente cadastrado.
                        rmCondicao = true
                        break
                    }
                }





                if (nome.isEmpty()) {
                    listenerAuth.onFailure("Insira o seu nome para a identificação!")
                } else if (email.isEmpty()) {
                    listenerAuth.onFailure("O email não pode estar vazio.")
                } else if (senha.isEmpty()) {
                    listenerAuth.onFailure("Insira uma senha válida!")
                } else if (rm.isEmpty()) {
                    listenerAuth.onFailure("O RM é necessário para a validação!")
                }
                else if (codigoturma.isEmpty()) {
                    listenerAuth.onFailure("Forneça o seu código de turma!")
                }
                else if(rmCondicao == true){
                    //testando a validação:
                    auth.createUserWithEmailAndPassword(email, senha)
                        .addOnCompleteListener { cadastro ->
                            if (cadastro.isSuccessful) { //Depois que o cadastro for um "sucesso", a gente vai salvar os seguintes dados no firebase:

                                //Pegando o ID do usuário que acabou de se cadastrar usando o "auth", ou seja, o usuário atual.
                                var usuarioID =
                                    FirebaseAuth.getInstance().currentUser?.uid.toString()

                                //mapeando o que eu quero salvar com as variaveis nos parâmetros:
                                val dadosUsuariosMap = hashMapOf(
                                    "nome" to nome,
                                    "email" to email,
                                    "rm" to rm,
                                    "codigoturma" to codigoturma,
                                    "usuarioID" to usuarioID
                                )

                                //Iniciando o comando para setar um caminho para uma coleção e gravando os dados nela
                                //O document(rm) significa que o identificador de cada aluno vai se dar pelo RM. Caso deixe o nome vai acabar sobreescrevendo os dados
                                firestore.collection("Alunos").document(rm).set(dadosUsuariosMap)
                                    .addOnCompleteListener {
                                        listenerAuth.onSucess("Usuário cadastrado com sucesso!")
                                    }.addOnFailureListener {
                                        listenerAuth.onFailure("Ocorreu um erro ao salvar os dados.")
                                    }

                            }
                        }//Fechamento do cadastrado com sucesso. Agora, vamos tratar alguns possíveis erros que evite o cadastro:


                        //Comando pra iniciar o tratamento de "erro" no cadastramento:
                        .addOnFailureListener { exception ->

                            //"quando" o erro for igual a determinada coisa, faça tal coisa:
                            val erro = when (exception) {
                                is FirebaseAuthUserCollisionException -> "Outra conta já cadastrada com estes dados."
                                is FirebaseAuthWeakPasswordException -> "A senha deve ter no mínimo 6 caracteres."
                                is FirebaseNetworkException -> "Sem conexão."

                                else -> "Email inválido."
                            }


                            listenerAuth.onFailure(erro)
                        }
                }


                else {
                    listenerAuth.onFailure("RM não encontrado no banco de dados.")
                }

            }
    }
}
