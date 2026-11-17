package hk.edu.thei.mti.lab1 // 記得改成你實際的 package 名字

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // 簡單模擬賬號密碼檢查
            if (username.isNotEmpty() && password == "123456") {
                // 儲存用戶名到 SharedPreferences
                val sharedPref = getSharedPreferences("GameConfig", Context.MODE_PRIVATE)
                sharedPref.edit().putString("username", username).apply()

                // 跳轉到遊戲頁面
                val intent = Intent(this, GameActivity::class.java)
                startActivity(intent)
                finish() // 關閉登入頁
            } else {
                Toast.makeText(this, "Invalid Username or Password (123456)", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
