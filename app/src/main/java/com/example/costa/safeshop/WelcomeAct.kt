package com.example.costa.safeshop
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_welcome.*
import android.os.Handler


class WelcomeAct : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        val AppVersion:String="1.3"             //CHANGE THIS TO CURRENT APP VERSION
        app_ver_tv.text="app_ver: "+AppVersion
        if (supportActionBar != null)
            supportActionBar?.hide()
        Handler().postDelayed(Runnable {
            val startActivity = Intent(this, MainActivity::class.java)
            startActivity(startActivity)
            finish()
        }, 5000)
    }
}
