package com.example.costa.safeshop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_books.*

class BooksAct : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_books)

        var cat:String=intent.getStringExtra("cat")
        var ipad:String=getString(R.string.local_ip)
        var url="http://"+ipad+"/SalesWeb/get_books.php?category="+cat
        var list=ArrayList<Book>()

        var rq:RequestQueue=Volley.newRequestQueue(this)
        var jar=JsonArrayRequest(Request.Method.GET,url,null,Response.Listener { response ->

            for (x in 0..response.length()-1)
                list.add(Book(response.getJSONObject(x).getInt("id"),
                    response.getJSONObject(x).getString("title"),
                    response.getJSONObject(x).getString("author"),
                    response.getJSONObject(x).getInt("price"),
                    response.getJSONObject(x).getString("photo")))

            var adp=BookAdapter(this,list)
            rv_books.layoutManager=LinearLayoutManager(this)
            rv_books.adapter=adp

        },Response.ErrorListener { error ->
            Toast.makeText(this,error.message,Toast.LENGTH_LONG).show()
        })
        rq.add(jar)

    }
}
