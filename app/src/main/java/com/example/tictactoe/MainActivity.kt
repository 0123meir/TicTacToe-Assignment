package com.example.tictactoe

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var buttons: Array<Array<Button>>
    private lateinit var turnIndicatorTextView: TextView
    private lateinit var playAgainButton: Button

    private var gameState = GameState()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttons = arrayOf(
            arrayOf(findViewById(R.id.button1), findViewById(R.id.button2), findViewById(R.id.button3)),
            arrayOf(findViewById(R.id.button4), findViewById(R.id.button5), findViewById(R.id.button6)),
            arrayOf(findViewById(R.id.button7), findViewById(R.id.button8), findViewById(R.id.button9))
        )

        turnIndicatorTextView = findViewById(R.id.turnText)
        playAgainButton = findViewById(R.id.play_again_button)

        playAgainButton.setOnClickListener { initializeGame() }
        initializeGame()
    }

    private fun initializeGame() {
        gameState = GameState()
        turnIndicatorTextView.visibility = View.VISIBLE
        turnIndicatorTextView.text = getString(R.string.play_prompt, gameState.turn)
        playAgainButton.visibility = View.GONE
        resetGrid()
        setButtonClickListeners()
    }

    private fun resetGrid() {
        for (i in buttons.indices) {
            for (j in buttons[i].indices) {
                buttons[i][j].text = ""
                gameState.grid[i][j] = ""
            }
        }
    }

    private fun setButtonClickListeners() {
        for (i in buttons.indices) {
            for (j in buttons[i].indices) {
                buttons[i][j].setOnClickListener {
                    if (gameState.grid[i][j].isEmpty() && gameState.winner == null) {
                        gameState.grid[i][j] = gameState.turn
                        buttons[i][j].text = gameState.turn

                        gameState.winner = gameState.winnerStatus
                        if (gameState.winner != null || gameState.boardFull) {
                            playAgainButton.visibility = View.VISIBLE
                            turnIndicatorTextView.visibility = View.GONE
                            val winner = gameState.winner
                            val message: String = when (winner) {
                                null -> "It's a Tie!"
                                else -> "The winner is $winner!"
                            }

                            Toast.makeText(this,message, Toast.LENGTH_LONG).show()
                        } else {
                            gameState.turn = when (gameState.turn) {
                                "X" -> "O"
                                else -> "X"
                            }
                            turnIndicatorTextView.text = getString(R.string.play_prompt, gameState.turn)
                        }
                    }
                }
            }
        }
    }

    data class GameState(
        var turn: String = "X",
        var winner: String? = null,
        val grid: Array<Array<String>> = Array(3) { Array(3) { "" } }
    ) {
        val boardFull: Boolean
            get() {
                for (row in grid) {
                    for (col in row) {
                        if (col == "") {
                            return false
                        }
                    }
                }
                return true
            }

        val winnerStatus: String?
            get() = checkWinner(grid)

        private fun checkWinner(grid: Array<Array<String>>): String? {
            for (row in grid) {
                if (row[0] == row[1] && row[1] == row[2] && row[0].isNotEmpty()) return row[0]
            }
            for (col in 0 until 3) {
                if (grid[0][col] == grid[1][col] && grid[1][col] == grid[2][col] && grid[0][col].isNotEmpty()) return grid[0][col]
            }
            if (grid[0][0] == grid[1][1] && grid[1][1] == grid[2][2] && grid[0][0].isNotEmpty()) return grid[0][0]
            if (grid[0][2] == grid[1][1] && grid[1][1] == grid[2][0] && grid[0][2].isNotEmpty()) return grid[0][2]
            return null
        }
    }

}
