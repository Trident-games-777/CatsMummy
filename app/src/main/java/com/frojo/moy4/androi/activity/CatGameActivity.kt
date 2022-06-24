package com.frojo.moy4.androi.activity

import android.animation.AnimatorSet
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnPause
import com.frojo.moy4.androi.R
import com.frojo.moy4.androi.anim.Animator

class CatGameActivity : AppCompatActivity() {
    private lateinit var images: List<ImageView>
    private lateinit var btnStartAgain: Button
    private var toCatch: Int = -1
    private var stop = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cat_game)
        toCatch = intent.getIntExtra(CatPreGameActivity.EXTRA_ICON, -1)
        images = listOf<ImageView>(
            findViewById(R.id.imageView1),
            findViewById(R.id.imageView2),
            findViewById(R.id.imageView3),
            findViewById(R.id.imageView4),
            findViewById(R.id.imageView5),
            findViewById(R.id.imageView6),
            findViewById(R.id.imageView7),
            findViewById(R.id.imageView8),
            findViewById(R.id.imageView9),
            findViewById(R.id.imageView10),
            findViewById(R.id.imageView11),
            findViewById(R.id.imageView12),
            findViewById(R.id.imageView13),
            findViewById(R.id.imageView14),
            findViewById(R.id.imageView15),
            findViewById(R.id.imageView16),
            findViewById(R.id.imageView17),
            findViewById(R.id.imageView18),
            findViewById(R.id.imageView19),
            findViewById(R.id.imageView20),
            findViewById(R.id.imageView21),
            findViewById(R.id.imageView22),
            findViewById(R.id.imageView23),
            findViewById(R.id.imageView24),
            findViewById(R.id.imageView25),
            findViewById(R.id.imageView26),
            findViewById(R.id.imageView27),
            findViewById(R.id.imageView28),
            findViewById(R.id.imageView29),
            findViewById(R.id.imageView30)
        )
        btnStartAgain = findViewById(R.id.btnStartAgain)
        btnStartAgain.setOnClickListener {
            startActivity(Intent(this, CatPreGameActivity::class.java))
            finish()
        }

        animateView()
    }

    private fun animateView(): AnimatorSet {
        val currentImageView = images.random()
        currentImageView.setImageResource(toCatch)

        showImages()
        hideImages(exception = currentImageView)

        val scale = Animator.animatorScale(currentImageView)

        currentImageView.setOnClickListener {
            stop = true
            scale.pause()
        }

        scale.doOnEnd {
            if (!stop) {
                animateView()
            }
        }
        scale.doOnPause {
            Animator.animatorReveal(btnStartAgain)
        }
        scale.start()
        return scale
    }

    private fun showImages() {
        images.forEach { it.visibility = View.VISIBLE }
    }

    private fun hideImages(exception: ImageView) {
        images.forEach { if (it != exception) it.visibility = View.INVISIBLE }
    }
}