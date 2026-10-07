package com.rajrank.app

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        root.gravity = Gravity.CENTER
        root.setPadding(50, 40, 50, 40)
        root.setBackgroundColor(Color.WHITE)

        val title = TextView(this)
        title.text = "RajRank"
        title.textSize = 32f
        title.setTextColor(Color.rgb(23, 70, 162))
        title.gravity = Gravity.CENTER

        val subtitle = TextView(this)
        subtitle.text = "Rajasthan CET • Mock Test"
        subtitle.textSize = 17f
        subtitle.setTextColor(Color.DKGRAY)
        subtitle.gravity = Gravity.CENTER

        val mobile = EditText(this)
        mobile.hint = "Mobile Number"
        mobile.inputType = 2

        val password = EditText(this)
        password.hint = "Password"
        password.inputType = 129

        val loginButton = Button(this)
        loginButton.text = "LOGIN"

        val registerButton = Button(this)
        registerButton.text = "CREATE ACCOUNT"

        root.addView(title)
        root.addView(subtitle)

        root.addView(
            mobile,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )

        root.addView(
            password,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )

        root.addView(loginButton)
        root.addView(registerButton)

        setContentView(root)
    }
}
