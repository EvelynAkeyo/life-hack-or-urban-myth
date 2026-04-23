package com.lifehack.lifehackorurbanmyth.data

/**
 * FlashcardData provides the complete list of flashcard questions used in the quiz.
 * All quiz content is stored here to keep data separate from UI logic.
 */
object FlashcardData {

    /**
     * Returns the full list of flashcard questions.
     * Each flashcard contains a statement, whether it's a hack (true) or myth (false),
     * and an explanation for the correct answer.
     *
     * @return A list of Flashcard objects containing all quiz questions.
     */
    fun getFlashcards(): List<Flashcard> {
        return listOf(
            // Question 1 — Myth
            Flashcard(
                statement = "Drinking 8 glasses of water a day is essential for everyone.",
                isHack = false,
                explanation = "Water needs vary by person, activity level, and climate."
            ),
            // Question 2 — Myth
            Flashcard(
                statement = "Putting your phone in rice fixes water damage.",
                isHack = false,
                explanation = "Rice is ineffective; silica gel or professional repair is better."
            ),
            // Question 3 — Myth
            Flashcard(
                statement = "Charging your phone overnight damages the battery.",
                isHack = false,
                explanation = "Modern phones stop charging at 100% automatically."
            ),
            // Question 4 — Myth
            Flashcard(
                statement = "Reading in dim light damages your eyesight permanently.",
                isHack = false,
                explanation = "It causes eye strain but no permanent damage."
            ),
            // Question 5 — Myth
            Flashcard(
                statement = "Cold water boils faster than warm water.",
                isHack = false,
                explanation = "Warm water boils faster; this is known as the Mpemba effect misconception."
            ),
            // Question 6 — Hack (True)
            Flashcard(
                statement = "Sleeping with a fan on dries out your sinuses and throat.",
                isHack = true,
                explanation = "True — fans can cause dryness and congestion."
            ),
            // Question 7 — Hack (True)
            Flashcard(
                statement = "You can use toothpaste to remove scratches from a phone screen.",
                isHack = true,
                explanation = "Mild abrasives in toothpaste can reduce light scratches."
            ),
            // Question 8 — Myth
            Flashcard(
                statement = "Eating carrots improves your night vision significantly.",
                isHack = false,
                explanation = "Only helps if you're vitamin A deficient."
            ),
            // Question 9 — Hack (True)
            Flashcard(
                statement = "Putting a wooden spoon over a boiling pot prevents it from boiling over.",
                isHack = true,
                explanation = "True — wood disrupts surface tension of bubbles."
            ),
            // Question 10 — Myth
            Flashcard(
                statement = "We only use 10% of our brains.",
                isHack = false,
                explanation = "Brain scans show activity in virtually all brain regions."
            ),
            // Bonus Question 11 — Hack (True)
            Flashcard(
                statement = "Placing a damp paper towel around a drink in the freezer cools it faster.",
                isHack = true,
                explanation = "The wet towel increases surface contact and evaporative cooling."
            ),
            // Bonus Question 12 — Myth
            Flashcard(
                statement = "Shaving makes hair grow back thicker.",
                isHack = false,
                explanation = "Shaving cuts hair at the thickest part, creating a blunt tip that feels coarser."
            )
        )
    }

    /**
     * Returns the total number of questions available in the quiz.
     *
     * @return The count of all flashcard questions.
     */
    fun getTotalQuestions(): Int = getFlashcards().size

    /**
     * Returns a shuffled copy of the flashcard list for a new game session.
     * This ensures each playthrough has a different question order.
     *
     * @return A shuffled list of Flashcard objects.
     */
    fun getShuffledFlashcards(): List<Flashcard> = getFlashcards().shuffled()

    /**
     * Returns the appropriate feedback string resource key based on the final score.
     * High score (≥7): Master Hacker
     * Medium score (4–6): Great job
     * Low score (<4): Keep practising
     *
     * @param score The number of correct answers.
     * @return A string describing the score feedback tier.
     */
    fun getScoreFeedback(score: Int): String {
        return when {
            score >= 7 -> "high"
            score in 4..6 -> "medium"
            else -> "low"
        }
    }
}
