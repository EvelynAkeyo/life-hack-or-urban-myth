package com.lifehack.lifehackorurbanmyth.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.lifehack.lifehackorurbanmyth.R
import com.lifehack.lifehackorurbanmyth.data.Flashcard
import com.lifehack.lifehackorurbanmyth.databinding.ItemReviewBinding

/**
 * ReviewAdapter binds flashcard data to RecyclerView items for the review screen.
 * Each item displays the question statement, correct answer, user's answer,
 * whether they got it right, and the explanation.
 *
 * @property flashcards The list of flashcard questions.
 * @property userAnswers An array of booleans representing the user's answer for each question.
 */
class ReviewAdapter(
    private val flashcards: List<Flashcard>,
    private val userAnswers: BooleanArray
) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    /**
     * ViewHolder that holds the views for a single review item.
     * Uses View Binding for type-safe view access.
     */
    class ReviewViewHolder(val binding: ItemReviewBinding) : RecyclerView.ViewHolder(binding.root)

    /**
     * Creates a new ViewHolder by inflating the item_review layout.
     *
     * @param parent The parent ViewGroup.
     * @param viewType The type of the new View (unused, single type).
     * @return A new ReviewViewHolder instance.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ItemReviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ReviewViewHolder(binding)
    }

    /**
     * Binds flashcard data to the ViewHolder at the given position.
     * Sets the question number, statement, correct answer, user's answer,
     * result badge, and explanation.
     *
     * @param holder The ViewHolder to bind data to.
     * @param position The position of the item in the list.
     */
    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val flashcard = flashcards[position]
        val userSelectedHack = userAnswers[position]
        val isCorrect = (userSelectedHack == flashcard.isHack)
        val context = holder.binding.root.context

        // Set the question number label (e.g., "Q1")
        holder.binding.questionNumber.text = context.getString(
            R.string.review_question_number, position + 1
        )

        // Set the statement text
        holder.binding.reviewStatement.text = "\"${flashcard.statement}\""

        // Set the correct answer label
        val correctAnswerLabel = if (flashcard.isHack) {
            context.getString(R.string.review_hack_label)
        } else {
            context.getString(R.string.review_myth_label)
        }
        holder.binding.reviewCorrectAnswer.text = context.getString(
            R.string.review_correct_answer, correctAnswerLabel
        )

        // Set the user's answer label
        val userAnswerLabel = if (userSelectedHack) {
            context.getString(R.string.review_hack_label)
        } else {
            context.getString(R.string.review_myth_label)
        }
        holder.binding.reviewYourAnswer.text = context.getString(
            R.string.review_your_answer, userAnswerLabel
        )

        // Set the result badge (correct or incorrect)
        if (isCorrect) {
            holder.binding.resultBadge.text = context.getString(R.string.review_correct_badge)
            holder.binding.resultBadge.setTextColor(
                ContextCompat.getColor(context, R.color.correct_green)
            )
        } else {
            holder.binding.resultBadge.text = context.getString(R.string.review_incorrect_badge)
            holder.binding.resultBadge.setTextColor(
                ContextCompat.getColor(context, R.color.incorrect_red)
            )
        }

        // Set the explanation text
        holder.binding.reviewExplanation.text = context.getString(
            R.string.review_explanation, flashcard.explanation
        )
    }

    /**
     * Returns the total number of items in the adapter.
     *
     * @return The number of flashcard items.
     */
    override fun getItemCount(): Int = flashcards.size
}
