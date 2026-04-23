package com.lifehack.lifehackorurbanmyth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.lifehack.lifehackorurbanmyth.databinding.ActivityWelcomeBinding

/**
 * WelcomeActivity is the launcher screen of the app.
 * It displays a welcome message, a description of the quiz,
 * and a Start button that navigates to the QuestionActivity.
 */
class WelcomeActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "WelcomeActivity"
    }

    // View Binding instance for accessing layout views
    private lateinit var binding: ActivityWelcomeBinding

    /**
     * Called when the activity is first created.
     * Sets up the UI and click listeners.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate layout using View Binding
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.i(TAG, "WelcomeActivity started — displaying welcome screen")

        // Apply a subtle fade-in animation to the main content
        animateEntrance()

        // Set up the Start button click listener
        setupStartButton()
    }

    /**
     * Animates the entrance of the welcome screen elements with a fade-in effect.
     */
    private fun animateEntrance() {
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in_up)
        binding.welcomeEmoji.startAnimation(fadeIn)
        binding.welcomeTitle.startAnimation(fadeIn)

        Log.d(TAG, "Welcome screen entrance animations applied")
    }

    /**
     * Sets up the Start button to navigate to the QuestionActivity
     * when clicked by the user.
     */
    private fun setupStartButton() {
        binding.startButton.setOnClickListener {
            Log.i(TAG, "Start button pressed — navigating to QuestionActivity")

            // Create an intent to launch the QuestionActivity
            val intent = Intent(this, QuestionActivity::class.java)
            startActivity(intent)

            // Optional: finish this activity so the user can't go back to the welcome screen
            // during the quiz (they can replay from the score screen)
            finish()
        }
    }
}
