package com.frojo.moy4.androi.anim

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View

class Animator {
    companion object {
        private const val MIN_SIZE = 0f
        private const val STANDARD_SIZE = 1f
        private const val MAX_SIZE = 2f

        private const val SCALE_X = "scaleX"
        private const val SCALE_Y = "scaleY"

        private const val SLOW_DURATION = 500L
        private const val MEDIUM_DURATION = 350L
        private const val FAST_DURATION = 300L

        fun animatorScale(view: View): AnimatorSet {
            val scaleUp = AnimatorSet().apply {
                playTogether(
                    ObjectAnimator.ofFloat(view, SCALE_X, MIN_SIZE, MAX_SIZE),
                    ObjectAnimator.ofFloat(view, SCALE_Y, MIN_SIZE, MAX_SIZE)
                )
            }
            val scaleDown = AnimatorSet().apply {
                playTogether(
                    ObjectAnimator.ofFloat(view, SCALE_X, MAX_SIZE, MIN_SIZE),
                    ObjectAnimator.ofFloat(view, SCALE_Y, MAX_SIZE, MIN_SIZE)
                )
            }
            return AnimatorSet().apply {
                duration = MEDIUM_DURATION
                play(scaleUp).before(scaleDown)
            }
        }

        fun animatorReveal(view: View): AnimatorSet {
            view.visibility = View.VISIBLE
            return AnimatorSet().apply {
                playTogether(
                    ObjectAnimator.ofFloat(view, SCALE_X, MIN_SIZE, STANDARD_SIZE),
                    ObjectAnimator.ofFloat(view, SCALE_Y, MIN_SIZE, STANDARD_SIZE)
                )
                duration = FAST_DURATION
            }
        }
    }
}