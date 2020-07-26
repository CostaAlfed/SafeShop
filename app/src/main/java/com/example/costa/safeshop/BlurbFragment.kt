package com.example.costa.safeshop

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_blurb.*
import kotlinx.android.synthetic.main.item_row.view.*

class BlurbFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var v=inflater!!.inflate(R.layout.fragment_blurb, container, false)

        var ipad:String=getString(R.string.local_ip)
        var url:String="http://"+ipad+"/SalesWeb/get_blurb.php?title="+UserInfo.bookTitle4blurb
        var rq: RequestQueue = Volley.newRequestQueue(activity)
        var jar= JsonArrayRequest(Request.Method.GET,url,null, Response.Listener { response ->

            Book_Title_blurb.text=response.getJSONObject(0).getString("title")
            Book_Author_blurb.text=response.getJSONObject(0).getString("author")
            var web:String="http://"+ipad+"/SalesWeb/images/"+response.getJSONObject(0).getString("photo")
            web=web.replace(" ","%20")
            Picasso.get().load(web).into(Book_Photo_Blurb)
            Book_Summary.text=response.getJSONObject(0).getString("blurb")
        }, Response.ErrorListener { error ->
            Toast.makeText(activity,error.message, Toast.LENGTH_LONG).show()
        })
        rq.add(jar)

        ok_butt_blurb.setOnClickListener {
            var i=Intent(activity,BooksAct::class.java)
            startActivity(i)
        }

        return v
    }
    fun onBackPressed() {
        fragmentManager!!.popBackStack()
    }


}
