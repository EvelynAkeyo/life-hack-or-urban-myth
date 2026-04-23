package com.lifehack.lifehackorurbanmyth

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lifehack.lifehackorurbanmyth.adapters.ReviewAdapter
import com.lifehack.lifehackorurbanmyth.data.Flashcard
import com.lifehack.lifehackorurbanmyth.databinding.ActivityReviewBinding

/**
 * ReviewActivity displays a scrollable list of all quiz questions
 * along with the correct answers, user's answers, and explanations.
 * Uses a RecyclerView with ReviewAdapter for efficient list rendering.
 */
class ReviewActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "ReviewActivity"

        // Intent extra keys for receiving data from ScoreActivity
        const val EXTRA_FLASHCARDS = "extra_flashcards"
        const val EXTRA_USER_ANSWERS = "extra_user_answers"
    }

    // View Binding instance
    private lateinit var binding: ActivityReviewBinding

    /**
     * Called when the activity is first created.
     * Retrieves quiz data and sets up the RecyclerView.
     */
    @Suppress("DEPRECATION", "UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate layout using View Binding
        binding = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.i(TAG, "ReviewActivity started — displaying answer review")

        // Retrieve flashcard data and user answers from the intent
        val flashcards = intent.getSerializableExtra(EXTRA_FLASHCARDS) as? ArrayList<Flashcard>
            ?: arrayListOf()
        val userAnswers = intent.getBooleanArrayExtra(EXTRA_USER_ANSWERS) ?: BooleanArray(0)

        Log.d(TAG, "Received ${flashcards.size} flashcards and ${userAnswers.size} user answers for review")

        // Set up the RecyclerView with a linear layout manager
        setupRecyclerView(flashcards, userAnswers)

        // Set up the back button
        setupBackButton()
    }

    /**
     * Configures the RecyclerView with the ReviewAdapter to display
     * all questions, correct answers, user answers, and explanations.
     *
     * @param flashcards The list of flashcard questions from the quiz.
     * @param userAnswers Array of booleans representing the user's answer for each question.
     */
    private fun setupRecyclerView(flashcards: ArrayList<Flashcard>, userAnswers: BooleanArray) {
        binding.reviewRecyclerView.apply {
            // Use a vertical linear layout
            layoutManager = LinearLayoutManager(this@ReviewActivity)

            // Set the adapter with flashcard data and user answers
            adapter = ReviewAdapter(flashcards, userAnswers)

            Log.d(TAG, "RecyclerView configured with ${flashcards.size} review items")
        }
    }

    /**
     * Sets up the back button to finish this activity and return to the ScoreActivity.
     */
    private fun setupBackButton() {
        binding.backButton.setOnClickListener {
            Log.i(TAG, "Back button pressed — returning to ScoreActivity")
            finish()
        }
    }
}
