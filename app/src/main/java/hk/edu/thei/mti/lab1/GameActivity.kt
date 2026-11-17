package hk.edu.thei.mti.lab1 // ⚠️ 請改成你新專案實際的 package 名稱

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Main gameplay activity for Tic Tac Toe.
 * Manages turn logic, round tracking, background music, and saving game results to SQLite.
 */
class GameActivity : AppCompatActivity() {

    private var isXTurn = true
    private var gameOver = false
    private var roundNumber = 1 // Tracks the current round number of the game
    private var currentUsername = "Player"

    private val board = IntArray(9) // 0 = Empty, 1 = Player X, 2 = Player O
    private lateinit var buttons: List<Button>
    private lateinit var tvStatus: TextView
    private lateinit var tvRound: TextView
    private lateinit var dbHelper: DatabaseHelper // SQLite database helper instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // Initialize SQLite Database Helper
        dbHelper = DatabaseHelper(this)

        // Bind UI components
        val tvWelcome = findViewById<TextView>(R.id.tvWelcome)
        tvRound = findViewById<TextView>(R.id.tvRound)
        tvStatus = findViewById(R.id.tvStatus)
        val btnReset = findViewById<Button>(R.id.btnReset)
        val btnGoToConfig = findViewById<Button>(R.id.btnGoToConfig)
        val btnHistory = findViewById<Button>(R.id.btnHistory)

        // Retrieve logged-in username from SharedPreferences
        val sharedPref = getSharedPreferences("GameConfig", Context.MODE_PRIVATE)
        currentUsername = sharedPref.getString("username", "Player") ?: "Player"
        tvWelcome.text = "Welcome, $currentUsername!"
        tvRound.text = "Round: $roundNumber"

        // Map the 9 grid buttons
        buttons = listOf(
            findViewById(R.id.b0), findViewById(R.id.b1), findViewById(R.id.b2),
            findViewById(R.id.b3), findViewById(R.id.b4), findViewById(R.id.b5),
            findViewById(R.id.b6), findViewById(R.id.b7), findViewById(R.id.b8)
        )

        // Set click listeners for each cell on the 3x3 board
        for (i in 0..8) {
            buttons[i].setOnClickListener {
                if (!gameOver && board[i] == 0) {
                    if (isXTurn) {
                        board[i] = 1
                        buttons[i].text = "X"
                        tvStatus.text = "Player O's Turn"
                    } else {
                        board[i] = 2
                        buttons[i].text = "O"
                        tvStatus.text = "Player X's Turn"
                    }
                    isXTurn = !isXTurn
                    checkWinner()
                }
            }
        }

        // Reset button starts a new round
        btnReset.setOnClickListener { resetGame() }

        // Navigate to Configuration screen
        btnGoToConfig.setOnClickListener {
            startActivity(Intent(this, ConfigActivity::class.java))
        }

        // Display saved SQLite records in an AlertDialog
        btnHistory.setOnClickListener {
            showHistoryDialog()
        }
    }

    /**
     * Ensures BGM plays according to user settings when returning to this activity.
     */
    override fun onResume() {
        super.onResume()
        BGMManager.startBGM(this)
    }

    /**
     * Checks all 8 winning combinations or a draw state.
     * Automatically saves the match outcome to SQLite when the round ends.
     */
    private fun checkWinner() {
        val winPositions = listOf(
            listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8), // Horizontal rows
            listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8), // Vertical columns
            listOf(0, 4, 8), listOf(2, 4, 6)                   // Diagonals
        )

        for (pos in winPositions) {
            if (board[pos[0]] != 0 && board[pos[0]] == board[pos[1]] && board[pos[1]] == board[pos[2]]) {
                val winner = if (board[pos[0]] == 1) "X" else "O"
                val winMessage = "Player $winner Wins! 🎉"
                tvStatus.text = winMessage
                gameOver = true

                // Save winning result and winning grid indices to SQLite
                val resultDetail = "Player $winner Wins (Cells: ${pos[0]},${pos[1]},${pos[2]})"
                saveRoundToDatabase(resultDetail)
                return
            }
        }

        // Check for a draw if all cells are filled without a winner
        if (!board.contains(0)) {
            tvStatus.text = "It's a Draw! 🤝"
            gameOver = true
            saveRoundToDatabase("Draw (Board Full)")
        }
    }

    /**
     * Captures the current system time and inserts the completed round into SQLite.
     */
    private fun saveRoundToDatabase(outcome: String) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val currentTime = dateFormat.format(Date())

        val isSaved = dbHelper.insertRecord(
            username = currentUsername,
            roundNumber = roundNumber,
            gameResult = outcome,
            systemTime = currentTime
        )

        if (isSaved) {
            Toast.makeText(this, "Round $roundNumber saved to database!", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Resets the board for a new game and increments the round counter.
     */
    private fun resetGame() {
        if (gameOver) {
            roundNumber++ // Increment round number only after a completed game
            tvRound.text = "Round: $roundNumber"
        }
        isXTurn = true
        gameOver = false
        board.fill(0)
        for (btn in buttons) {
            btn.text = ""
        }
        tvStatus.text = "Player X's Turn"
    }

    /**
     * Fetches all records from SQLite and shows them in a scrollable dialog.
     */
    private fun showHistoryDialog() {
        val historyText = dbHelper.getAllRecords()
        AlertDialog.Builder(this)
            .setTitle("SQLite Game Records")
            .setMessage(historyText)
            .setPositiveButton("Close", null)
            .show()
    }
}
