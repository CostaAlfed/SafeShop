package com.example.costa.safeshop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_ccp.*

class CCPAct : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ccp)
        val bnb3=intent.getStringExtra("bnb2")


        ccp_valider.setOnClickListener {
            val int= Intent(this,TicketAct::class.java)
            int.putExtra("bnb2",bnb3)
            startActivity(int)
        }
    }
}
