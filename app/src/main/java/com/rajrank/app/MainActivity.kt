package com.rajrank.app

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(40, 60, 40, 40)

        val title = TextView(this)
        title.text = "RajRank"
        title.textSize = 32f

        val subtitle = TextView(this)
        subtitle.text = "Rajasthan CET • Mock Test"
        subtitle.textSize = 18f

        layout.addView(title)
        layout.addView(subtitle)

        setContentView(layout)
    }
}
