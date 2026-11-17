package hk.edu.thei.mti.lab1 // ⚠️ 請改成你新專案實際的 package 名稱

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * SQLiteOpenHelper class for managing the Tic Tac Toe game history database.
 * Stores Username, Round Number, Game Result (adapted from Bingo numbers requirement), and System Time.
 */
class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        // Database configuration constants
        private const val DATABASE_NAME = "TicTacToeHistory.db"
        private const val DATABASE_VERSION = 1

        // Table and column names matching assignment attributes
        const val TABLE_NAME = "game_records"
        const val COL_ID = "id"
        const val COL_USERNAME = "username"           // Attribute i: Username
        const val COL_ROUND = "round_number"          // Attribute ii: Round number of the game
        const val COL_RESULT = "game_result"          // Attribute iii: Game outcome/winning line (adapted for Tic Tac Toe)
        const val COL_TIME = "system_time"            // Attribute iv: System Time
    }

    /**
     * Called when the database is created for the first time.
     * Executes the SQL statement to create the game_records table.
     */
    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_USERNAME TEXT,
                $COL_ROUND INTEGER,
                $COL_RESULT TEXT,
                $COL_TIME TEXT
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    /**
     * Called when the database needs to be upgraded to a new version.
     */
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    /**
     * Inserts a new game record into the SQLite database upon game completion.
     *
     * @param username The logged-in player's name
     * @param roundNumber The current round number played
     * @param gameResult The winner/draw outcome and winning positions
     * @param systemTime The timestamp when the round ended
     * @return True if insertion succeeded, False otherwise
     */
    fun insertRecord(username: String, roundNumber: Int, gameResult: String, systemTime: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_USERNAME, username)
            put(COL_ROUND, roundNumber)
            put(COL_RESULT, gameResult)
            put(COL_TIME, systemTime)
        }

        val result = db.insert(TABLE_NAME, null, values)
        db.close()
        return result != -1L
    }

    /**
     * Retrieves all saved game records formatted as a readable string for display.
     *
     * @return A formatted string containing all historical game records
     */
    fun getAllRecords(): String {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME ORDER BY $COL_ID DESC", null)
        val stringBuilder = StringBuilder()

        if (cursor.moveToFirst()) {
            do {
                val user = cursor.getString(cursor.getColumnIndexOrThrow(COL_USERNAME))
                val round = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ROUND))
                val res = cursor.getString(cursor.getColumnIndexOrThrow(COL_RESULT))
                val time = cursor.getString(cursor.getColumnIndexOrThrow(COL_TIME))

                stringBuilder.append("Round #$round | Player: $user\n")
                stringBuilder.append("Outcome: $res\n")
                stringBuilder.append("Time: $time\n")
                stringBuilder.append("----------------------------------------\n")
            } while (cursor.moveToNext())
        } else {
            stringBuilder.append("No game records found yet.")
        }

        cursor.close()
        db.close()
        return stringBuilder.toString()
    }
}
