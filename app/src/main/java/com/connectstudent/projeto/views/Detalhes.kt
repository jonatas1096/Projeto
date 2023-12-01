package com.connectstudent.projeto.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.connectstudent.projeto.layoutsprontos.arrowVoltar
import com.connectstudent.projeto.layoutsprontos.loadImage
import com.connectstudent.projeto.ui.theme.Jomhuria
import com.connectstudent.projeto.ui.theme.LARANJA

@Composable
fun Detalhes(navController: NavController){

    val cor = LARANJA
    val scroll = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = LARANJA)
            .padding(top = 25.dp, bottom = 15.dp)
            .padding(horizontal = 8.dp)
    ){
        Card(
            modifier = Modifier
            .fillMaxSize(),
            backgroundColor = Color.White,
            elevation = 8.dp,
            shape = RoundedCornerShape(25.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scroll)
                    .padding(16.dp)
                    .padding(top = 10.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Detalhes do APP",
                        fontSize = 44.sp,
                        fontFamily = Jomhuria,
                        fontWeight = FontWeight.Bold,
                        color = LARANJA
                    )
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                    ) {
                        loadImage(path = "https://raw.githubusercontent.com/jonatas1096/Projeto/master/app/src/main/res/drawable/logo_ofc.png",
                            contentDescription = "Logo do app",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                        )
                    }
                }

                Text(
                    text = "A aplicação ConnectStudent, uma rede social exclusiva para unidades ETEC, foi desenvolvida com o intuito de aproximar professores e alunos," +
                            " aumentando a interação entre si e auxiliando no compartilhamento de informações ou  ideias dentro da própria unidade. A aplicação permitirá que voce faça" +
                            " o cadastro (sendo obrigatoriamente um aluno/professor ou parte da administração), logue-se ao sistema, acesse informações pertinentes sobre si próprio ou da" +
                            " unidade em que estuda, visualize publicações de outros usuários, reaja à elas, crie as suas próprias publicações com imagens, título, texto e, se desejar, as " +
                            "exclua também.\n" +
                            "O projeto em geral associa dados armazenados em tempo real com possíveis alunos/professores, permitindo ou delimitando ações durante o uso do app. Essas" +
                            " informações são armazenadas em um banco de dados e são atualizadas conforme voce, usuário, interage com o sistema como, por exemplo, desde curtidas ou comentários" +
                            " em publicações como também mudanças na foto de perfil.\n",
                    fontSize = 17.sp,
                )
                Text(text = "Desejamos que você aproveite o nosso APP!", fontSize = 17.sp, color = LARANJA,  fontWeight = FontWeight.Bold)

                Text(
                    text = "Políticas de privacidade",
                    fontSize = 50.sp,
                    fontFamily = Jomhuria,
                    fontWeight = FontWeight.Bold,
                    color = LARANJA
                )

                Text(
                    text = "Bem-vindo à nossa rede social móvel para alunos e professores da [ETEC Zona Leste]. Estes termos e condições regem o uso do nosso aplicativo. Ao utilizá-lo, você concorda expressamente com os seguintes termos e condições:",
                    fontSize = 17.sp,
                    modifier = Modifier.padding(bottom = 18.dp)
                )
                //Privacidade
                Text(text = "1. Privacidade:", fontSize = 17.sp, color = cor, modifier = Modifier.padding(bottom = 5.dp))
                Text(text = "O ConnectStudent respeita a sua privacidade e se compromete a proteger seus dados pessoais dentro do app. Esta política descreve como coletamos, usamos e protegemos suas informações pessoais. Para mais detalhes sobre a coleta e uso de dados, entre em contato conosco por email ou procure um dos desenvolvedores na unidade escolar.\n" +
                        " jonatas109620@gmail.com"
                    ,fontSize = 17.sp, modifier = Modifier.padding(bottom = 10.dp))
                //Cadastro de usuário
                Text(text = "2. Cadastro de usuário:", fontSize = 17.sp, color = cor, modifier = Modifier.padding(bottom = 5.dp))
                Text(text = "Para utilizar nosso aplicativo, você deve criar uma conta. Você é responsável por manter a confidencialidade de suas credenciais de login e por todas as atividades que ocorrerem em sua conta durante o uso. O cadastro em si só será permitido para alunos, professores ou agentes da administração da unidade escolar ETEC, disponibilizado previamente um “rm” ou “cpsID” para tal."
                    ,fontSize = 17.sp, modifier = Modifier.padding(bottom = 10.dp))
                //Uso Aceitável
                Text(text = "3. Uso Aceitável e Comportamento:", fontSize = 17.sp, color = cor, modifier = Modifier.padding(bottom = 5.dp))
                Text(text = "Você concorda em utilizar nosso aplicativo de maneira respeitosa e ética. Comportamentos inadequados como agressão verbal, assédio, machismo, racismo ou qualquer outro tipo de discurso de ódio que viole os direitos de terceiros, não será tolerado."
                    ,fontSize = 17.sp, modifier = Modifier.padding(bottom = 10.dp))
                //Propriedade Intelectual:
                Text(text = "4. Propriedade Intelectual e Conteúdo do Usuário:", fontSize = 17.sp, color = cor, modifier = Modifier.padding(bottom = 5.dp))
                Text(text = "Todo o conteúdo gerado pelos usuários como postagens, fotos, vídeos ou comentários, pertence aos respectivos criadores. Você não tem permissão para usar esse conteúdo sem a devida autorização."
                    ,fontSize = 17.sp, modifier = Modifier.padding(bottom = 10.dp))
                //Responsabilidade:
                Text(text = "5. Responsabilidade e Danos:", fontSize = 17.sp, color = cor, modifier = Modifier.padding(bottom = 5.dp))
                Text(text = "Você reconhece que os desenvolvedores não são responsáveis por qualquer dano, perda, inconveniência ou prejuízo causado pelo uso de nosso aplicativo. Utilize-o por sua conta e risco."
                    ,fontSize = 17.sp, modifier = Modifier.padding(bottom = 10.dp))
                //Encerramento de Conta:
                Text(text = "6. Encerramento de conta e Exclusão de Dados:", fontSize = 17.sp, color = cor, modifier = Modifier.padding(bottom = 5.dp))
                Text(text = "Você pode encerrar sua conta a qualquer momento contactando qualquer um dos desenvolvedores. Isso resultará na exclusão permanente de seus dados, ou seja, não podemos recuperar informações de contas excluídas."
                    ,fontSize = 17.sp, modifier = Modifier.padding(bottom = 10.dp))
                //Diretrizes de Conteúdo:
                Text(text = "7. Diretrizes de Conteúdo e Proibições:", fontSize = 17.sp, color = cor, modifier = Modifier.padding(bottom = 5.dp))
                Text(text = "É estritamente proibido qualquer tipo de postagem que inclua conteúdo ilegal como discurso de ódio, nudez, violência, etc. O usuário irá arcar com as consequências caso o mesmo ocorra"
                    ,fontSize = 17.sp, modifier = Modifier.padding(bottom = 10.dp))
                //Rescisão de Serviço:
                Text(text = "8. Rescisão de Serviço:", fontSize = 17.sp, color = cor, modifier = Modifier.padding(bottom = 5.dp))
                Text(text = "Reservamos o direito para que os desenvolvedores possam encerrar ou modificar o serviço a qualquer momento, com ou sem aviso prévio."
                    ,fontSize = 17.sp, modifier = Modifier.padding(bottom = 10.dp))

                Row(modifier = Modifier.fillMaxWidth().padding(top = 5.dp)) {
                    Text(
                        text = "Voltar",
                        fontSize = 40.sp,
                        fontFamily = Jomhuria,
                        color = LARANJA,
                        modifier = Modifier.clickable {
                            navController.navigate("Login")
                        }
                    )
                }

            }
        }
    }

}