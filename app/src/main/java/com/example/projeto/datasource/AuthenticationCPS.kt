package com.example.projeto.datasource

import com.example.projeto.listener.ListenerAuth
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject


//essa classe aqui está servindo apenas como autenticação de um cadastro. É praticamente pronto do firebase.
class AuthenticationCPS @Inject constructor() {

    //iniciando o serviço de autenticação do firebase, é preciso fazer isso aqui:
    val auth = FirebaseAuth.getInstance()

    //Usado para interagir com o Firestore, aqui conseguimos usar isso para puxar os dados dele e manipular tudo
    val firestore = FirebaseFirestore.getInstance()

    //Agora o processo vai se repetir para os professores, com a diferença de alguns parâmetros sendo diferentes.


    fun cadastroCps(
        nome: String,
        email: String,
        senha: String,
        id: String,
        codigoEtec: String,
        listenerAuth: ListenerAuth
    ){
        //novamente a variavel para acessar o firebase e puxar tudo de "Data"
        val colecaoData = firestore.collection("Data")
        //Acessando o documento ID
        val idDocumento = colecaoData.document("ID")

        //Puxando tudo que está dentro do documento "ID"
        idDocumento.get()
            .addOnSuccessListener { documentSnapshot ->

                //Validação CPS:
                var condicaoID = false
                //Variavel para guardar o valor de ID que o usuário inserir
                val idBox = id


                val idData = documentSnapshot.data
                //aqui usamos a variavel acima para puxar todos os dados do array que eu criei no firebase e inserir na variavel "arrayID"
                //Ou seja, eu fiz uma cópia do array original do firebase e armazenei em uma variavel aqui localmente.
                val arrayID = idData?.get("id_cps") as? List<String>


                //Começo da validação para garantir que o usuário é um professor ou da administração:
                if (arrayID != null) {
                    for (item in arrayID) {
                        if (item == idBox) {//Se o indice for igual ao IDBox (que é o ID que o usuário vai inserir, a condição vai para true).
                            condicaoID = true
                            // Uma vez que encontramos um valor verdadeiro, não precisamos continuar o loop.
                            break
                        }
                    }
                }



                //Agora a validação de todos os campos preenchidos:
                if (nome.isEmpty()) {
                    listenerAuth.onFailure("Insira o seu nome para a identificação!")
                } else if (email.isEmpty()) {
                    listenerAuth.onFailure("O email não pode estar vazio.")
                } else if (senha.isEmpty()) {
                    listenerAuth.onFailure("Insira uma senha válida!")
                } else if (id.isEmpty()) {
                    listenerAuth.onFailure("O ID é necessário para a validação!")
                }
                else if (codigoEtec.isEmpty()) {
                    listenerAuth.onFailure("Forneça o código da ETEC!")

                }
                else if(condicaoID == true && codigoEtec == "211"){
                    //testando a validação:
                    auth.createUserWithEmailAndPassword(email, senha)
                        .addOnCompleteListener { cadastroCps ->
                            if (cadastroCps.isSuccessful) { //Depois que o cadastro for um "sucesso", a gente vai salvar os seguintes dados no firebase:

                                //Pegando o ID do usuário que acabou de se cadastrar usando o "auth", ou seja, o usuário atual.
                                var cpsID = FirebaseAuth.getInstance().currentUser?.uid.toString()

                                //mapeando o que eu quero salvar com as variaveis nos parâmetros:
                                val cpsUsuariosMap = hashMapOf(
                                    "nome" to nome,
                                    "email" to email,
                                    "id" to id,
                                    "codigoEtec" to codigoEtec,
                                    "cpsID" to cpsID
                                )

                                //Iniciando o comando para setar um caminho para uma coleção e gravando os dados nela
                                //O document(id) significa que o identificador de cada professor/adm vai se dar pelo ID. Caso deixe o nome vai acabar sobreescrevendo os dados.
                                firestore.collection("Cps").document(id).set(cpsUsuariosMap)
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
                    listenerAuth.onFailure("ID não encontrado no banco de dados.")
                }

            }
    }
}