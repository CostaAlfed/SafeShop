package com.example.costa.safeshop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_home.*

class HomeAct : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        var ipad:String=getString(R.string.local_ip)
        var url="http://"+ipad+"/SalesWeb/get_cat.php"
        var list=ArrayList<String>()
        var rq:RequestQueue=Volley.newRequestQueue(this)
        var jar=JsonArrayRequest(Request.Method.GET,url,null,Response.Listener { response ->
            for (x in 0..response.length()-1)
                list.add(response.getJSONObject(x).getString("category"))   //+" Books"
            var adp=ArrayAdapter(this, R.layout.my_textview,list)
            home_cats.adapter=adp
        },Response.ErrorListener { error ->
            Toast.makeText(this,error.message,Toast.LENGTH_LONG).show()
        })
    rq.add(jar)
    home_cats.setOnItemClickListener { adapterView, view, i, l ->
        var cat:String=list[i]
        var obj=Intent(this,BooksAct::class.java)
        obj.putExtra("cat",cat)
        startActivity(obj)
    }
    }
}
