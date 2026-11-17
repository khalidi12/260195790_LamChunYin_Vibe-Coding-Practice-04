package hk.edu.thei.mti.lab1

import android.content.Context
import android.media.MediaPlayer

/**
 * Singleton manager responsible for controlling background music (BGM) across all activities.
 * Reads audio settings from SharedPreferences to determine playback state.
 */
object BGMManager {
    private var mediaPlayer: MediaPlayer? = null

    /**
     * Starts or resumes looping background music if sound effects are enabled in settings.
     */
    fun startBGM(context: Context) {
        val sharedPref = context.getSharedPreferences("GameConfig", Context.MODE_PRIVATE)
        val isSoundEnabled = sharedPref.getBoolean("sound_effects", true)

        if (isSoundEnabled) {
            if (mediaPlayer == null) {
                // Initialize MediaPlayer with the raw audio resource and enable looping
                mediaPlayer = MediaPlayer.create(context.applicationContext, R.raw.bgm).apply {
                    isLooping = true
                    start()
                }
            } else if (!mediaPlayer!!.isPlaying) {
                mediaPlayer!!.start()
            }
        } else {
            stopBGM()
        }
    }

    /**
     * Pauses background music playback when the app goes into the background.
     */
    fun pauseBGM() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
        }
    }

    /**
     * Stops playback and releases MediaPlayer resources completely.
     */
    fun stopBGM() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
            mediaPlayer = null
        }
    }
}
