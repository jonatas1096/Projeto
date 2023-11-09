package com.connectstudent.projeto.datasource

import androidx.compose.runtime.mutableStateOf
import com.connectstudent.projeto.listener.ListenerAuth
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


//essa classe aqui está servindo apenas como autenticação de um cadastro. É praticamente pronto do firebase.
class AuthenticationCPS @Inject constructor() {

    //iniciando o serviço de autenticação do firebase, é preciso fazer isso aqui:
    val auth = FirebaseAuth.getInstance()

    //Usado para interagir com o Firestore, aqui conseguimos usar isso para puxar os dados dele e manipular tudo
    val firestore = FirebaseFirestore.getInstance()
    //Usadas para saber se o usuário está logado ou não (e manter ele logado se precisar):
    val _verificarUsuario = MutableStateFlow(false)
    val verificarUsuario: StateFlow<Boolean> = _verificarUsuario


    //Agora o processo vai se repetir para os professores, com a diferença de alguns parâmetros sendo diferentes.


    fun cadastroCps(
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
                //val arrayID = idData?.get("id_cps") as? List<String>


                //Começo da validação para garantir que o usuário é um professor ou da administração:
                if (idData != null) {
                    for (arrayCont in idData.keys) {
                        val array = idData[arrayCont] as? List<String>
                        if (array != null && array.isNotEmpty() && array.contains(idBox)) {
                            condicaoID = true
                            break
                        }
                    }
                }


                //Começo da validação para garantir que o ID já não está cadastrado por algum professor/administração e acabe sobreescrevendo os dados:
                val cpsColecao = firestore.collection("Cps")
                //Uma condição para garantir nos If abaixo:
                var cadastroID = mutableStateOf(false) //Ela começa como false, e se o rm nao for achado na coleção dos alunos irá mudar para true.

                cpsColecao.get()
                    .addOnSuccessListener { querySnapshot ->
                        for (document in querySnapshot.documents) {
                            val cpsData = document.data
                            val idCps = cpsData?.get("id") as? String

                            if (idCps == id) {
                                cadastroID.value = true
                                println("Situação do cadastro: $cadastroID")
                                break // Não é necessário continuar o loop se já encontramos um rm matriculado.
                            }
                        }


                        //Agora a validação de todos os campos preenchidos:
                        if (email.isEmpty()) {
                            listenerAuth.onFailure("O email não pode estar vazio.")
                        } else if (senha.isEmpty()) {
                            listenerAuth.onFailure("Insira uma senha válida!")
                        } else if (id.isEmpty()) {
                            listenerAuth.onFailure("O ID é necessário para a validação!")
                        }
                        else if (codigoEtec.isEmpty()) {
                            listenerAuth.onFailure("Forneça o código da ETEC!")

                        }
                        else if(condicaoID == true && cadastroID.value == false && codigoEtec == "211"){
                            //testando a validação:
                            auth.createUserWithEmailAndPassword(email, senha)
                                .addOnCompleteListener { cadastroCps ->
                                    if (cadastroCps.isSuccessful) { //Depois que o cadastro for um "sucesso", a gente vai salvar os seguintes dados no firebase:

                                        //Pegando o ID do usuário que acabou de se cadastrar usando o "auth", ou seja, o usuário atual.
                                        var cpsID = FirebaseAuth.getInstance().currentUser?.uid.toString()

                                        //mapeando o que eu quero salvar com as variaveis nos parâmetros:
                                        val cpsUsuariosMap = hashMapOf(
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
                        else if (cadastroID.value == true){
                            listenerAuth.onFailure("ID já cadastrado por outro usuário.")
                        }
                        else if(codigoEtec != "211"){
                            listenerAuth.onFailure("Código ETEC inválido.")
                        }
                        else {
                            listenerAuth.onFailure("ID não encontrado no banco de dados.")
                        }
                    }


            }
    }

    fun verificarUsuarioLogado(): Flow<Boolean> {

        val usuarioLogado = FirebaseAuth.getInstance().currentUser

        _verificarUsuario.value = usuarioLogado != null
        return  verificarUsuario
    }
}