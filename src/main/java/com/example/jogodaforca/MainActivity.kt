package com.example.jogodaforca

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.jogodaforca.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // Variável de ligação para acessar os elementos da interface do usuário
    private lateinit var binding: ActivityMainBinding

    // Lista de palavras possíveis para o jogo com tema infantil
    private val palavras = listOf("elefante", "girafa", "abacaxi", "banana", "cachorro", "gato")

    // Variáveis do jogo
    private var palavraSelecionada = ""
    private lateinit var palavraOculta: CharArray
    private var tentativas = 6
    private val letrasAdivinhadas = mutableListOf<Char>()

    /**
     * Função onCreate, chamada quando a atividade é criada.
     * @param savedInstanceState Instância salva do estado anterior da atividade (pode ser nulo).
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Infla o layout usando o binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializa o jogo
        iniciarJogo()

        // Define o comportamento do botão de adivinhar quando é clicado
        binding.buttonAdivinhar.setOnClickListener {
            // Obtém a entrada do usuário e a converte para maiúsculas
            val entrada = binding.editEntrada.text.toString().uppercase()
            // Verifica se a entrada não está vazia
            if (entrada.isNotEmpty()) {
                // Pega a primeira letra da entrada
                val letra = entrada[0]
                // Verifica se a letra ainda não foi adivinhada
                if (!letrasAdivinhadas.contains(letra)) {
                    // Adiciona a letra à lista de letras adivinhadas
                    letrasAdivinhadas.add(letra)
                    // Verifica se a palavra selecionada contém a letra
                    if (palavraSelecionada.contains(letra)) {
                        // Atualiza o array da palavra oculta com a letra correta nas posições certas
                        for (i in palavraSelecionada.indices) {
                            if (palavraSelecionada[i] == letra) {
                                palavraOculta[i] = letra
                            }
                        }
                    } else {
                        // Se a letra não estiver na palavra, decrementa o número de tentativas
                        tentativas--
                    }
                    // Limpa o campo de entrada
                    binding.editEntrada.text.clear()
                    // Atualiza a tela com os novos valores
                    atualizarTela()
                }
            }
        }
    }

    /**
     * Inicializa ou reinicia o jogo.
     */
    private fun iniciarJogo() {
        // Seleciona uma nova palavra aleatória da lista e a converte para maiúsculas
        palavraSelecionada = palavras.random().uppercase()
        // Cria um novo array de caracteres para representar a palavra oculta, inicialmente preenchida com '_'
        palavraOculta = CharArray(palavraSelecionada.length) { '_' }
        // Reseta o número de tentativas e a lista de letras adivinhadas
        tentativas = 6
        letrasAdivinhadas.clear()
        // Atualiza a tela inicial com os valores padrão
        atualizarTela()
    }

    /**
     * Função que atualiza a interface com a palavra oculta e o número de tentativas restantes.
     */
    private fun atualizarTela() {
        // Exibe a palavra oculta com as letras adivinhadas
        binding.textOculto.text = palavraOculta.joinToString(" ")

        // Exibe o número de tentativas restantes
        binding.textTentativas.text = "Restam: $tentativas tentativas"

        // Verifica se o jogador ganhou ou perdeu e atualiza o status
        if (!palavraOculta.contains('_')) {
            binding.textStatus.text = "Parabéns! Você acertou: $palavraSelecionada"
            binding.buttonAdivinhar.isEnabled = false
            // Reinicia o jogo após uma pequena pausa
            binding.root.postDelayed({ iniciarJogo() }, 2000)
        } else if (tentativas <= 0) {
            binding.textStatus.text = "Você perdeu! A palavra era: $palavraSelecionada"
            binding.buttonAdivinhar.isEnabled = false
            // Reinicia o jogo após uma pequena pausa
            binding.root.postDelayed({ iniciarJogo() }, 2000)
        }
    }
}
