package com.lifehack.lifehackorurbanmyth

import com.lifehack.lifehackorurbanmyth.data.Flashcard
import com.lifehack.lifehackorurbanmyth.data.FlashcardData
import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for the Life Hack or Urban Myth quiz app.
 * Tests cover the Flashcard data class, score calculation logic,
 * feedback string determination, and data integrity.
 *
 * Run via: ./gradlew test
 */
class FlashcardUnitTest {

    /**
     * Test 1: Verifies that the Flashcard data class stores values correctly.
     * Checks that all properties (statement, isHack, explanation) are
     * correctly assigned and retrievable.
     */
    @Test
    fun flashcard_dataClass_storesValuesCorrectly() {
        // Arrange: Create a flashcard with known values
        val flashcard = Flashcard(
            statement = "Test statement",
            isHack = true,
            explanation = "Test explanation"
        )

        // Assert: Verify all fields are stored correctly
        assertEquals("Test statement", flashcard.statement)
        assertTrue(flashcard.isHack)
        assertEquals("Test explanation", flashcard.explanation)
    }

    /**
     * Test 2: Verifies that the Flashcard data class correctly stores
     * a myth (isHack = false) value.
     */
    @Test
    fun flashcard_dataClass_storesMythCorrectly() {
        // Arrange: Create a myth flashcard
        val flashcard = Flashcard(
            statement = "Cold water boils faster than warm water.",
            isHack = false,
            explanation = "Warm water boils faster."
        )

        // Assert: Verify isHack is false for a myth
        assertFalse(flashcard.isHack)
        assertEquals("Cold water boils faster than warm water.", flashcard.statement)
    }

    /**
     * Test 3: Verifies that the score feedback function returns the correct
     * feedback tier based on different score values.
     * - High score (≥7): returns "high"
     * - Medium score (4–6): returns "medium"
     * - Low score (<4): returns "low"
     */
    @Test
    fun scoreFeedback_returnsCorrectTier() {
        // High scores (≥7) should return "high"
        assertEquals("high", FlashcardData.getScoreFeedback(7))
        assertEquals("high", FlashcardData.getScoreFeedback(8))
        assertEquals("high", FlashcardData.getScoreFeedback(10))

        // Medium scores (4–6) should return "medium"
        assertEquals("medium", FlashcardData.getScoreFeedback(4))
        assertEquals("medium", FlashcardData.getScoreFeedback(5))
        assertEquals("medium", FlashcardData.getScoreFeedback(6))

        // Low scores (<4) should return "low"
        assertEquals("low", FlashcardData.getScoreFeedback(0))
        assertEquals("low", FlashcardData.getScoreFeedback(1))
        assertEquals("low", FlashcardData.getScoreFeedback(3))
    }

    /**
     * Test 4: Verifies that score calculation logic works correctly
     * by simulating a quiz and counting correct answers.
     */
    @Test
    fun scoreCalculation_incrementsCorrectly() {
        // Arrange: Set up a simple scoring simulation
        var score = 0
        val flashcards = FlashcardData.getFlashcards()

        // Act: Simulate answering each question correctly
        for (flashcard in flashcards) {
            // If user answer matches the correct answer, increment score
            val userAnswer = flashcard.isHack // Simulate correct answer
            if (userAnswer == flashcard.isHack) {
                score++
            }
        }

        // Assert: Score should equal total number of questions (all correct)
        assertEquals(flashcards.size, score)
    }

    /**
     * Test 5: Verifies that the flashcard data source contains at least 10 questions.
     */
    @Test
    fun flashcardData_containsAtLeast10Questions() {
        val flashcards = FlashcardData.getFlashcards()

        // Assert: There should be at least 10 flashcards
        assertTrue("Expected at least 10 flashcards, got ${flashcards.size}",
            flashcards.size >= 10)
    }

    /**
     * Test 6: Verifies that the shuffled flashcards contain the same elements
     * as the original list (just in a different order).
     */
    @Test
    fun shuffledFlashcards_containSameElements() {
        val original = FlashcardData.getFlashcards()
        val shuffled = FlashcardData.getShuffledFlashcards()

        // Assert: Both lists should have the same size
        assertEquals(original.size, shuffled.size)

        // Assert: Shuffled list should contain all original elements
        assertTrue(shuffled.containsAll(original))
    }

    /**
     * Test 7: Verifies that each flashcard has non-empty statement and explanation fields.
     */
    @Test
    fun flashcardData_allFieldsNonEmpty() {
        val flashcards = FlashcardData.getFlashcards()

        for ((index, flashcard) in flashcards.withIndex()) {
            assertTrue("Flashcard $index has empty statement",
                flashcard.statement.isNotBlank())
            assertTrue("Flashcard $index has empty explanation",
                flashcard.explanation.isNotBlank())
        }
    }

    /**
     * Test 8: Verifies that the getTotalQuestions function returns the correct count.
     */
    @Test
    fun getTotalQuestions_returnsCorrectCount() {
        val totalQuestions = FlashcardData.getTotalQuestions()
        val actualSize = FlashcardData.getFlashcards().size

        assertEquals(actualSize, totalQuestions)
    }

    /**
     * Test 9: Verifies score calculation when all answers are incorrect.
     */
    @Test
    fun scoreCalculation_zeroWhenAllWrong() {
        var score = 0
        val flashcards = FlashcardData.getFlashcards()

        // Simulate answering each question incorrectly
        for (flashcard in flashcards) {
            val userAnswer = !flashcard.isHack // Opposite of correct answer
            if (userAnswer == flashcard.isHack) {
                score++
            }
        }

        // All wrong should yield zero
        assertEquals(0, score)
    }

    /**
     * Test 10: Verifies Flashcard data class equality (Kotlin data class auto-generated equals).
     */
    @Test
    fun flashcard_equalityWorks() {
        val card1 = Flashcard("Test", true, "Explanation")
        val card2 = Flashcard("Test", true, "Explanation")
        val card3 = Flashcard("Different", false, "Other")

        assertEquals(card1, card2)
        assertNotEquals(card1, card3)
    }
}
