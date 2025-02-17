package com.example.jogodedados

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Colors
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun JogoDeDados( navController: NavController ) { // Recebe o navController como parâmetro

    val systemUiController = rememberSystemUiController() // Instância do rememberSystemUiController
    systemUiController.setStatusBarColor(Color.LightGray) // Define a cor de status bar

    val context = LocalContext.current // Instância do LocalContext

    val dadoSorteado = remember { mutableStateOf(1) } // Seta o valor de dadoSorteado para 1
    val dadoSorteado2 = remember { mutableStateOf(3) } // Seta o valor de dadoSorteado2 para 1
    val isRolling = remember { mutableStateOf(false) } // Seta o valor de isRolling para false

    Scaffold( // Estrutura de layout para a tela
        topBar = { // * Inicio da top bar
            TopAppBar( //
                title = { // Define um titulo para a appbar
                    Text( // Define o texto do titulo para a appbar
                        text = "Jogo de Dados",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                },
                modifier = Modifier.statusBarsPadding() // Adiciona padding para a top bar
            )
        } // * Fim da top bar

    ) { paddingValues ->  // Inicio do conteúdo da tela - Scaffold
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally, // Alinha os elementos horizontalmente no centro
            verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically)
            // Distribui os elementos verticalmente com espaçamento de 32.dp
        ) {
            Row( // Cria uma linha para os dados
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically // Alinha os elementos verticalmente no centro
            ) {
                Image(
                    painter = painterResource(
                        id = when (dadoSorteado.value) {
                            1 -> R.drawable.dado_1
                            2 -> R.drawable.dado_2
                            3 -> R.drawable.dado_3
                            4 -> R.drawable.dado_4
                            5 -> R.drawable.dado_5
                            else -> R.drawable.dado_6
                        }
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(190.dp) // Define o tamanho da imagem como 190.dp

                )
                Image(
                    painter = painterResource(
                        id = when (dadoSorteado2.value) {
                            1 -> R.drawable.dado_1
                            2 -> R.drawable.dado_2
                            3 -> R.drawable.dado_3
                            4 -> R.drawable.dado_4
                            5 -> R.drawable.dado_5
                            else -> R.drawable.dado_6
                        }
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(170.dp) // Define o tamanho da imagem como 170.dp
                )
            }

            Button(
                onClick = {
                    if (!isRolling.value) { // Verifica se o botão não está rolando
                        isRolling.value = true // Seta o valor de isRolling para true

                        val mp3 = MediaPlayer.create(context, R.raw.somdado)
                        mp3.start()

                        rolarDadosAnimado( // Chama a função rolarDadosAnimado
                            onRolled = { numero, numero2 -> // Define a função onRolled
                                dadoSorteado.value = numero // Seta o valor de dadoSorteado para o número sorteado
                                dadoSorteado2.value = numero2 // Seta o valor de dadoSorteado2 para o número sorteado
                            },
                            onComplete = {
                                isRolling.value = false // Seta o valor de isRolling para false
                            }
                        )
                    }
                },
                enabled = !isRolling.value, // Desabilita o botão se isRolling for true
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 12.dp,
                ),
                modifier = Modifier.padding(bottom = 8.dp),
            ) {
                Text(
                    text = if (isRolling.value) "Rolando..." else "Rolar Dados", // Define o texto do botão
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    )
            }
        }

    } // Fim do conteúdo da tela - Scaffold

}

fun rolarDadosAnimado(
    onRolled: (Int, Int) -> Unit,
    onComplete: () -> Unit,
) {
    CoroutineScope(Dispatchers.Main).launch {
        val totalDuration = 3000L
        val interval = 150L
        val steps = totalDuration / interval

        try {
            repeat(steps.toInt()) {
                val numero1 = (1..6).random()
                val numero2 = (1..6).random()
                onRolled(numero1, numero2)
                delay(interval)
            }
            val finalNumero1 = (1..6).random()
            val finalNumero2 = (1..6).random()
            onRolled(finalNumero1, finalNumero2)
        }finally {
            onComplete()
        }


    }
}
