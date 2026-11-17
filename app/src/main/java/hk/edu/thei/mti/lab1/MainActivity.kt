package hk.edu.thei.mti.lab1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * Entry point activity handling user login.
 * Validates the username, saves it to SharedPreferences, starts BGM, and navigates to the game screen.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        // Adjust padding to accommodate system status and navigation bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Bind UI input fields and login button
        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        // Handle login button click event
        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim() // Retained for UI completeness

            // Proceed to game if username is not empty
            if (username.isNotEmpty()) {
                // Save player username into SharedPreferences for session persistence
                val sharedPref = getSharedPreferences("GameConfig", Context.MODE_PRIVATE)
                sharedPref.edit().putString("username", username).apply()

                // Start background music based on saved user preference
                BGMManager.startBGM(this)

                // Navigate to GameActivity and close the login screen
                val intent = Intent(this, GameActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // Prompt user if username field is left blank
                Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
