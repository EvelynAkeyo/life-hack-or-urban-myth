package com.lifehack.lifehackorurbanmyth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.lifehack.lifehackorurbanmyth.data.Flashcard
import com.lifehack.lifehackorurbanmyth.databinding.ActivityScoreBinding

/**
 * ScoreActivity displays the user's final quiz results.
 * It shows the score, a personalized feedback message based on performance,
 * and provides buttons to review answers or play again.
 */
class ScoreActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "ScoreActivity"

        // Intent extra keys for receiving data from QuestionActivity
        const val EXTRA_SCORE = "extra_score"
        const val EXTRA_TOTAL = "extra_total"
        const val EXTRA_FLASHCARDS = "extra_flashcards"
        const val EXTRA_USER_ANSWERS = "extra_user_answers"
    }

    // View Binding instance
    private lateinit var binding: ActivityScoreBinding

    // Quiz results received from QuestionActivity
    private var score: Int = 0
    private var totalQuestions: Int = 0
    private lateinit var flashcards: ArrayList<Flashcard>
    private lateinit var userAnswers: BooleanArray

    /**
     * Called when the activity is first created.
     * Retrieves quiz data from the intent and displays the results.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate layout using View Binding
        binding = ActivityScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.i(TAG, "ScoreActivity started — displaying results")

        // Retrieve quiz results from the intent
        retrieveIntentData()

        // Display the score and feedback
        displayResults()

        // Set up button click listeners
        setupButtons()
    }

    /**
     * Retrieves the score, total questions, flashcard data, and user answers
     * from the Intent extras passed by QuestionActivity.
     */
    @Suppress("DEPRECATION", "UNCHECKED_CAST")
    private fun retrieveIntentData() {
        score = intent.getIntExtra(EXTRA_SCORE, 0)
        totalQuestions = intent.getIntExtra(EXTRA_TOTAL, 0)

        // Retrieve flashcard list (Serializable)
        flashcards = intent.getSerializableExtra(EXTRA_FLASHCARDS) as? ArrayList<Flashcard>
            ?: arrayListOf()

        // Retrieve user answers
        userAnswers = intent.getBooleanArrayExtra(EXTRA_USER_ANSWERS) ?: BooleanArray(0)

        Log.i(TAG, "Received results: score=$score, total=$totalQuestions, " +
                "flashcards=${flashcards.size}, userAnswers=${userAnswers.size}")
    }

    /**
     * Displays the final score, percentage, and personalized feedback message.
     * The feedback tier is determined by the score:
     * - High (≥7): "Master Hacker! 🏆"
     * - Medium (4–6): "Great job! Keep it up! 👍"
     * - Low (<4): "Stay Safe Online! 📚 Keep practising!"
     */
    private fun displayResults() {
        // Calculate percentage
        val percentage = if (totalQuestions > 0) (score * 100) / totalQuestions else 0

        // Display score percentage in the circle
        binding.scorePercentage.text = getString(R.string.score_percentage, percentage)

        // Display score fraction (e.g., "You got 7 out of 10!")
        binding.scoreResult.text = getString(R.string.score_result, score, totalQuestions)

        // Determine and display personalized feedback
        val feedbackMessage: String
        val emoji: String

        when {
            score >= 7 -> {
                feedbackMessage = getString(R.string.feedback_high)
                emoji = "🏆"
                Log.i(TAG, "Score tier: HIGH ($score/$totalQuestions)")
            }
            score in 4..6 -> {
                feedbackMessage = getString(R.string.feedback_medium)
                emoji = "👍"
                Log.i(TAG, "Score tier: MEDIUM ($score/$totalQuestions)")
            }
            else -> {
                feedbackMessage = getString(R.string.feedback_low)
                emoji = "📚"
                Log.i(TAG, "Score tier: LOW ($score/$totalQuestions)")
            }
        }

        binding.feedbackMessage.text = feedbackMessage
        binding.resultEmoji.text = emoji

        Log.i(TAG, "Final score displayed: $score/$totalQuestions ($percentage%)")
    }

    /**
     * Sets up click listeners for the Review Answers and Play Again buttons.
     */
    private fun setupButtons() {
        // Review Answers button — navigates to ReviewActivity
        binding.reviewButton.setOnClickListener {
            Log.i(TAG, "Review Answers button pressed — navigating to ReviewActivity")
            navigateToReview()
        }

        // Play Again button — restarts the game from the Welcome Screen
        binding.playAgainButton.setOnClickListener {
            Log.i(TAG, "Play Again button pressed — restarting from WelcomeActivity")
            navigateToWelcome()
        }
    }

    /**
     * Navigates to the ReviewActivity, passing the flashcard data and user answers.
     */
    private fun navigateToReview() {
        val intent = Intent(this, ReviewActivity::class.java).apply {
            putExtra(ReviewActivity.EXTRA_FLASHCARDS, flashcards)
            putExtra(ReviewActivity.EXTRA_USER_ANSWERS, userAnswers)
        }
        startActivity(intent)
    }

    /**
     * Navigates back to the WelcomeActivity to restart the quiz.
     * Clears the activity back stack so the user starts fresh.
     */
    private fun navigateToWelcome() {
        val intent = Intent(this, WelcomeActivity::class.java).apply {
            // Clear all previous activities in the back stack
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}
