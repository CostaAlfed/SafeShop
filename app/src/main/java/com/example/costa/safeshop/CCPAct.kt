package com.example.costa.safeshop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_ccp.*
import kotlinx.android.synthetic.main.activity_cib.*
import java.util.*

class CCPAct : AppCompatActivity() {

    companion object {private val ALLOWED_CHARACTERS = "0123456789"}

    private fun getCommandeNumber(): String {
        val random = Random()
        val sb = StringBuilder(8)
        for (i in 0 until 8)
            sb.append(ALLOWED_CHARACTERS[random.nextInt(ALLOWED_CHARACTERS.length)])
        return sb.toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ccp)
        val bnb3=intent.getStringExtra("bnb2")
        val meth=intent.getStringExtra("meth")
        var num_comm="0001"+getCommandeNumber()

        val ipad:String=getString(R.string.local_ip)

        val url="http://"+ipad+"/SalesWeb/get_total.php?bill_no="+bnb3
        val rq: RequestQueue = Volley.newRequestQueue(this)
        val sr= StringRequest(Request.Method.GET,url, Response.Listener { response ->
            montant_ccp.text=response+" DZD"
        }, Response.ErrorListener { error ->
            Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
        })
        rq.add(sr)

        numero_commande_ccp.text=num_comm

        ccp_valider.setOnClickListener {
            val int= Intent(this,TicketAct::class.java)
            int.putExtra("bnb2",bnb3)
            int.putExtra("meth",meth)
            startActivity(int)
        }
        ccp_annuler.setOnClickListener { this.finish()}
        ccp_reinit.setOnClickListener {
            num_carte.text=null
            ccp_name.text=null
            ccp_cvv2.text=null
            spinnerday.setSelection(0)
            spinnermonth.setSelection(0)
        }

        num_carte.doAfterTextChanged {
            if (num_carte.text.length == 4 ||
                num_carte.text.length == 9 ||
                num_carte.text.length ==14)     num_carte.text.append('-')
        }

    }
}
