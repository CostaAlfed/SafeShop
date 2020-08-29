package com.example.costa.safeshop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_ccp.*
import kotlinx.android.synthetic.main.activity_cib.*
import kotlinx.android.synthetic.main.activity_ticket.*

class CIBAct : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cib)
        val bnb3=intent.getStringExtra("bnb2")
        val meth=intent.getStringExtra("meth")
        val ipad:String=getString(R.string.local_ip)

        val url="http://"+ipad+"/SalesWeb/get_total.php?bill_no="+bnb3
        val rq: RequestQueue = Volley.newRequestQueue(this)
        val sr= StringRequest(Request.Method.GET,url, Response.Listener { response ->
            montant_cib.text=response+" DZD"
        }, Response.ErrorListener { error ->
            Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
        })
        rq.add(sr)

        cib_valider.setOnClickListener {
            val int= Intent(this,TicketAct::class.java)
            int.putExtra("bnb2",bnb3)
            int.putExtra("meth",meth)
            startActivity(int)
        }
    }
}
