package com.lifehack.lifehackorurbanmyth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.lifehack.lifehackorurbanmyth.data.Flashcard
import com.lifehack.lifehackorurbanmyth.data.FlashcardData
import com.lifehack.lifehackorurbanmyth.databinding.ActivityQuestionBinding

/**
 * QuestionActivity displays the flashcard quiz questions one at a time.
 * Users select "Hack" (True) or "Myth" (False) for each statement.
 * After answering, feedback is shown and the user can proceed to the next question.
 * Once all questions are answered, the user is navigated to the ScoreActivity.
 */
class QuestionActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "QuestionActivity"
        // Keys for saving instance state across configuration changes
        private const val KEY_CURRENT_INDEX = "current_index"
        private const val KEY_SCORE = "score"
        private const val KEY_ANSWERED = "answered"
        private const val KEY_USER_ANSWERS = "user_answers"
    }

    // View Binding instance
    private lateinit var binding: ActivityQuestionBinding

    // Shuffled list of flashcard questions for this game session
    private lateinit var flashcards: List<Flashcard>

    // Current question index (loop/index approach, NOT recursion)
    private var currentIndex: Int = 0

    // Running score counter — incremented on each correct answer
    private var score: Int = 0

    // Whether the current question has been answered (prevents double-tap)
    private var answered: Boolean = false

    // Stores user's answers: true = user selected Hack, false = user selected Myth
    private val userAnswers: BooleanArray by lazy { BooleanArray(flashcards.size) }

    /**
     * Called when the activity is first created.
     * Initializes the shuffled question list, restores state if needed, and displays the first question.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate layout using View Binding
        binding = ActivityQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.i(TAG, "QuestionActivity started — initializing quiz")

        // Get shuffled flashcards for this game session
        flashcards = FlashcardData.getShuffledFlashcards()
        Log.d(TAG, "Loaded ${flashcards.size} flashcards (shuffled)")

        // Restore state if activity is being recreated (e.g., screen rotation)
        if (savedInstanceState != null) {
            restoreState(savedInstanceState)
        }

        // Set up button click listeners
        setupButtons()

        // Display the current question
        displayQuestion()
    }

    /**
     * Saves the current quiz state when the activity is being destroyed
     * (e.g., during screen rotation). This ensures the user doesn't lose progress.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_CURRENT_INDEX, currentIndex)
        outState.putInt(KEY_SCORE, score)
        outState.putBoolean(KEY_ANSWERED, answered)
        outState.putBooleanArray(KEY_USER_ANSWERS, userAnswers)

        Log.d(TAG, "Saving instance state: index=$currentIndex, score=$score")
    }

    /**
     * Restores quiz state from a saved instance state bundle.
     *
     * @param savedInstanceState The bundle containing the saved state.
     */
    private fun restoreState(savedInstanceState: Bundle) {
        currentIndex = savedInstanceState.getInt(KEY_CURRENT_INDEX, 0)
        score = savedInstanceState.getInt(KEY_SCORE, 0)
        answered = savedInstanceState.getBoolean(KEY_ANSWERED, false)

        val savedAnswers = savedInstanceState.getBooleanArray(KEY_USER_ANSWERS)
        if (savedAnswers != null) {
            System.arraycopy(savedAnswers, 0, userAnswers, 0, savedAnswers.size)
        }

        Log.d(TAG, "Restored state: index=$currentIndex, score=$score, answered=$answered")
    }

    /**
     * Sets up click listeners for the Hack button, Myth button, and Next button.
     */
    private fun setupButtons() {
        // Hack (True) button — user believes the statement is a real life hack
        binding.hackButton.setOnClickListener {
            if (!answered) {
                Log.d(TAG, "User selected: Hack (True) for question ${currentIndex + 1}")
                processAnswer(userSelectedHack = true)
            }
        }

        // Myth (False) button — user believes the statement is an urban myth
        binding.mythButton.setOnClickListener {
            if (!answered) {
                Log.d(TAG, "User selected: Myth (False) for question ${currentIndex + 1}")
                processAnswer(userSelectedHack = false)
            }
        }

        // Next button — loads the next question or finishes the quiz
        binding.nextButton.setOnClickListener {
            Log.d(TAG, "Next button pressed")
            moveToNextQuestion()
        }
    }

    /**
     * Displays the current flashcard question, updating the statement text,
     * question counter, progress bar, and score label.
     */
    private fun displayQuestion() {
        val flashcard = flashcards[currentIndex]
        val totalQuestions = flashcards.size

        Log.i(TAG, "Displaying question ${currentIndex + 1} of $totalQuestions: \"${flashcard.statement}\"")

        // Set the statement text on the flashcard
        binding.statementText.text = "\"${flashcard.statement}\""

        // Update the question counter (e.g., "Question 3 of 10")
        binding.questionCounter.text = getString(R.string.question_counter, currentIndex + 1, totalQuestions)

        // Update the score label
        binding.scoreLabel.text = getString(R.string.score_label, score)

        // Update the progress bar (percentage completion)
        val progressPercent = ((currentIndex + 1) * 100) / totalQuestions
        binding.quizProgressBar.progress = progressPercent

        // Reset UI state for the new question
        resetQuestionUI()
    }

    /**
     * Resets the UI elements to their default state for a new question.
     * Hides feedback, re-enables answer buttons, hides the Next button.
     */
    private fun resetQuestionUI() {
        // Hide feedback text
        binding.feedbackText.visibility = View.INVISIBLE

        // Re-enable and reset answer button colors
        binding.hackButton.isEnabled = true
        binding.mythButton.isEnabled = true
        binding.hackButton.setBackgroundColor(ContextCompat.getColor(this, R.color.button_hack))
        binding.mythButton.setBackgroundColor(ContextCompat.getColor(this, R.color.button_myth))

        // Hide the Next button until user answers
        binding.nextButton.visibility = View.GONE

        // Reset answered flag
        answered = false
    }

    /**
     * Processes the user's answer selection. Determines if the answer is correct,
     * updates the score, shows visual feedback, and reveals the Next button.
     *
     * @param userSelectedHack True if the user pressed "Hack ✅ (True)", false for "Myth ❌ (False)".
     */
    private fun processAnswer(userSelectedHack: Boolean) {
        // Mark the question as answered to prevent double-tap
        answered = true

        val flashcard = flashcards[currentIndex]

        // Store the user's answer for review later
        userAnswers[currentIndex] = userSelectedHack

        // Determine if the answer is correct
        val isCorrect = (userSelectedHack == flashcard.isHack)

        Log.i(TAG, "Question ${currentIndex + 1}: User answered ${if (userSelectedHack) "Hack" else "Myth"} — " +
                "Correct answer is ${if (flashcard.isHack) "Hack" else "Myth"} — " +
                "Result: ${if (isCorrect) "CORRECT" else "WRONG"}")

        // Increment score if correct
        if (isCorrect) {
            score++
            Log.d(TAG, "Score incremented to $score")
        }

        // Update the score label immediately
        binding.scoreLabel.text = getString(R.string.score_label, score)

        // Show visual feedback on buttons
        showButtonFeedback(userSelectedHack, isCorrect)

        // Show textual feedback with animation
        showFeedbackText(isCorrect, flashcard.isHack)

        // Disable both answer buttons to prevent further input
        binding.hackButton.isEnabled = false
        binding.mythButton.isEnabled = false

        // Show the Next button (or Finish button if last question)
        showNextButton()
    }

    /**
     * Highlights the selected answer button green (correct) or red (incorrect),
     * and shows the correct answer in green if the user was wrong.
     *
     * @param userSelectedHack Whether the user selected the Hack button.
     * @param isCorrect Whether the user's selection was correct.
     */
    private fun showButtonFeedback(userSelectedHack: Boolean, isCorrect: Boolean) {
        if (isCorrect) {
            // Highlight the selected button green
            if (userSelectedHack) {
                binding.hackButton.setBackgroundColor(ContextCompat.getColor(this, R.color.correct_green))
            } else {
                binding.mythButton.setBackgroundColor(ContextCompat.getColor(this, R.color.correct_green))
            }
        } else {
            // Highlight the selected button red
            if (userSelectedHack) {
                binding.hackButton.setBackgroundColor(ContextCompat.getColor(this, R.color.incorrect_red))
                // Also highlight the correct button green
                binding.mythButton.setBackgroundColor(ContextCompat.getColor(this, R.color.correct_green))
            } else {
                binding.mythButton.setBackgroundColor(ContextCompat.getColor(this, R.color.incorrect_red))
                // Also highlight the correct button green
                binding.hackButton.setBackgroundColor(ContextCompat.getColor(this, R.color.correct_green))
            }
        }
    }

    /**
     * Displays feedback text below the flashcard with a fade-in animation.
     * The text varies based on whether the answer was correct and the correct answer type.
     *
     * @param isCorrect Whether the user answered correctly.
     * @param isHack Whether the statement is actually a hack (true) or myth (false).
     */
    private fun showFeedbackText(isCorrect: Boolean, isHack: Boolean) {
        val feedbackText: String
        val feedbackColor: Int

        if (isCorrect) {
            // Correct answer feedback
            feedbackColor = ContextCompat.getColor(this, R.color.correct_green)
            feedbackText = if (isHack) {
                getString(R.string.feedback_correct_hack)
            } else {
                getString(R.string.feedback_correct_myth)
            }
        } else {
            // Wrong answer feedback
            feedbackColor = ContextCompat.getColor(this, R.color.incorrect_red)
            feedbackText = if (isHack) {
                getString(R.string.feedback_wrong_hack)
            } else {
                getString(R.string.feedback_wrong_myth)
            }
        }

        // Set the feedback text and color
        binding.feedbackText.text = feedbackText
        binding.feedbackText.setTextColor(feedbackColor)
        binding.feedbackText.visibility = View.VISIBLE

        // Animate the feedback text with a fade-in-up effect
        val fadeInAnim = AnimationUtils.loadAnimation(this, R.anim.fade_in_up)
        binding.feedbackText.startAnimation(fadeInAnim)

        Log.d(TAG, "Feedback shown: $feedbackText")
    }

    /**
     * Makes the Next button visible. If this is the last question,
     * changes the button text to "Finish →".
     */
    private fun showNextButton() {
        binding.nextButton.visibility = View.VISIBLE

        // Change button text to "Finish" if this is the last question
        if (currentIndex >= flashcards.size - 1) {
            binding.nextButton.text = getString(R.string.button_finish)
            Log.d(TAG, "Last question — Next button shows 'Finish'")
        } else {
            binding.nextButton.text = getString(R.string.button_next)
        }
    }

    /**
     * Moves to the next question or finishes the quiz if all questions are answered.
     * Uses an index-based loop approach (NOT recursion) to iterate through questions.
     */
    private fun moveToNextQuestion() {
        if (currentIndex < flashcards.size - 1) {
            // Move to the next question
            currentIndex++
            Log.i(TAG, "Moving to question ${currentIndex + 1} of ${flashcards.size}")
            displayQuestion()
        } else {
            // All questions answered — navigate to the Score Screen
            Log.i(TAG, "Quiz complete! Final score: $score out of ${flashcards.size}")
            navigateToScoreScreen()
        }
    }

    /**
     * Navigates to the ScoreActivity, passing the final score, total questions,
     * flashcard data, and user answers via Intent extras.
     */
    private fun navigateToScoreScreen() {
        Log.i(TAG, "Navigating to ScoreActivity with score=$score, total=${flashcards.size}")

        val intent = Intent(this, ScoreActivity::class.java).apply {
            // Pass the final score
            putExtra(ScoreActivity.EXTRA_SCORE, score)
            // Pass the total number of questions
            putExtra(ScoreActivity.EXTRA_TOTAL, flashcards.size)
            // Pass the flashcard data as a Serializable ArrayList
            putExtra(ScoreActivity.EXTRA_FLASHCARDS, ArrayList(flashcards))
            // Pass the user's answers
            putExtra(ScoreActivity.EXTRA_USER_ANSWERS, userAnswers)
        }

        startActivity(intent)
        finish() // Prevent going back to the quiz mid-session
    }
}
