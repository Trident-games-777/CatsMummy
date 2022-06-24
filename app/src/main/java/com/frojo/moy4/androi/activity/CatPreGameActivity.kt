package com.frojo.moy4.androi.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.frojo.moy4.androi.R

class CatPreGameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cat_pre_game)
        val images = listOf(
            R.drawable.icon1,
            R.drawable.icon2,
            R.drawable.icon3,
            R.drawable.icon4,
            R.drawable.icon5,
        )
        val img = findViewById<ImageView>(R.id.imgCatch)
        val button = findViewById<Button>(R.id.btnStart)
        val drawable: Int = images.random()
        img.setImageResource(drawable)
        button.setOnClickListener {
            val intent = Intent(this, CatGameActivity::class.java)
            intent.putExtra(EXTRA_ICON, drawable)
            startActivity(intent)
            finish()
        }
    }

    companion object {
        const val EXTRA_ICON = "icon"
    }
}