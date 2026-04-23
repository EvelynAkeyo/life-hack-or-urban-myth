package com.lifehack.lifehackorurbanmyth.data

import java.io.Serializable

/**
 * Data class representing a single flashcard in the quiz.
 *
 * @property statement The life hack or myth statement displayed to the user.
 * @property isHack True if the statement is a genuine life hack, false if it's an urban myth.
 * @property explanation A brief explanation of why the statement is true or false.
 */
data class Flashcard(
    val statement: String,
    val isHack: Boolean,
    val explanation: String
) : Serializable
