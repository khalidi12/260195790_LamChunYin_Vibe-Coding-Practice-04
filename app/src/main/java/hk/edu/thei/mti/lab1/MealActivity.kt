package hk.edu.thei.mti.lab1

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MealActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal)

        // 綁定 UI 元件
        val cbCustomer1 = findViewById<CheckBox>(R.id.cbCustomer1)
        val cbCustomer2 = findViewById<CheckBox>(R.id.cbCustomer2)
        val rgMeals = findViewById<RadioGroup>(R.id.rgMeals)
        val btnMealConfirm = findViewById<Button>(R.id.btnMealConfirm)
        val tvMealResult = findViewById<TextView>(R.id.tvMealResult)
        val btnReturn = findViewById<Button>(R.id.btnReturn)

        // Confirm 按鈕點擊事件：獲取選取狀態並顯示
        btnMealConfirm.setOnClickListener {
            // 1. 檢查選取了哪些顧客
            val customers = mutableListOf<String>()
            if (cbCustomer1.isChecked) customers.add("Customer 1")
            if (cbCustomer2.isChecked) customers.add("Customer 2")

            // 2. 檢查選取了哪個餐點
            val selectedRadioId = rgMeals.checkedRadioButtonId
            val meal = if (selectedRadioId != -1) {
                findViewById<RadioButton>(selectedRadioId).text.toString()
            } else {
                ""
            }

            // 3. 根據選取情況組合文字
            if (customers.isEmpty() || meal.isEmpty()) {
                tvMealResult.text = "Please select item:"
            } else {
                // 將顧客列表用逗號連接，例如 "Customer 1, Customer 2 select Breakfast."
                val customerText = customers.joinToString(" and ")
                tvMealResult.text = "$customerText select $meal."
            }
        }

        // Return 按鈕點擊事件：關閉此頁面，自然回到上一頁 (Game UI)
        btnReturn.setOnClickListener {
            finish()
        }
    }
}
