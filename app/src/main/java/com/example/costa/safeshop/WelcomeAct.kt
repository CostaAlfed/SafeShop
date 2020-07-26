package com.example.costa.safeshop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_welcome.*
import androidx.core.os.HandlerCompat.postDelayed
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Handler


class WelcomeAct : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        if (supportActionBar != null)
            supportActionBar?.hide()
        Handler().postDelayed(Runnable {
            val startActivity = Intent(this, MainActivity::class.java)
            startActivity(startActivity)
            finish()
        }, 5000)
        /*logo_image.setOnClickListener {
            var i= Intent(this,MainActivity::class.java)
            startActivity(i)
        }*/
    }
}
