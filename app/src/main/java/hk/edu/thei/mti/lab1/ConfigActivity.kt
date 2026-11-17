package hk.edu.thei.mti.lab1

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat

/**
 * Configuration activity allowing players to toggle background sound effects.
 * Persists audio preferences using SharedPreferences and updates BGM playback immediately.
 */
class ConfigActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

        // Bind UI switch and save button
        val switchSound = findViewById<SwitchCompat>(R.id.switchSound)
        val btnSaveConfig = findViewById<Button>(R.id.btnSaveConfig)

        // Load existing sound preference from SharedPreferences (defaults to true)
        val sharedPref = getSharedPreferences("GameConfig", Context.MODE_PRIVATE)
        val isSoundEnabled = sharedPref.getBoolean("sound_effects", true)
        switchSound.isChecked = isSoundEnabled

        // Save updated preference and apply audio changes immediately
        btnSaveConfig.setOnClickListener {
            val isChecked = switchSound.isChecked

            // Persist toggle state to SharedPreferences
            sharedPref.edit().putBoolean("sound_effects", isChecked).apply()

            // Start or stop global background music based on user choice
            if (isChecked) {
                BGMManager.startBGM(this)
            } else {
                BGMManager.stopBGM()
            }

            Toast.makeText(this, "Settings Saved!", Toast.LENGTH_SHORT).show()
            finish() // Return to GameActivity
        }
    }
}
