package com.example.costa.safeshop

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.drawable.LayerDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_confirm.*
import android.view.View
import androidx.appcompat.app.AlertDialog
import kotlin.system.exitProcess


class confirmAct : AppCompatActivity() {

    var thankspu=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm)

        var uname:String=""
        val ipad: String = getString(R.string.local_ip)
        val url1 ="http://"+ipad+"/SalesWeb/get_user_name.php?number=" + UserInfo.mobile
        val rq1: RequestQueue = Volley.newRequestQueue(this)
        val sr1 = JsonArrayRequest(Request.Method.GET, url1,null, Response.Listener {response ->
            uname = response.getJSONObject(0).getString("name")
            thankspu="Thank You "+uname+",\n For Using Our Service."
            thanks.text=thankspu
        }, Response.ErrorListener {error ->
            Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
        })
        rq1.add(sr1)


        thanks.setTypeface(Typeface.DEFAULT_BOLD)
        thanks.setFilters(arrayOf<InputFilter>(InputFilter.AllCaps()))

        val stars = ratingBar.getProgressDrawable() as LayerDrawable
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP)

        submit_rating_butt.setOnClickListener {
            Toast.makeText(this, "Your Rating: "+ratingBar.rating.toInt()+"/5 Stars!",Toast.LENGTH_LONG).show()
            val int= Intent(this,LastActivity::class.java)
            startActivity(int)
        }
    }
}
