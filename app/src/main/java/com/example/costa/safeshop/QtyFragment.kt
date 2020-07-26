package com.example.costa.safeshop

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
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
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class QtyFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var v=inflater!!.inflate(R.layout.fragment_qty, container, false)

        var et=v.findViewById<EditText>(R.id.qty_text)
        var butt=v.findViewById<Button>(R.id.qty_butt)

        butt.setOnClickListener{

            var ipad:String=getString(R.string.local_ip)
            var url="http://"+ipad+"/SalesWeb/add_temp.php?number="+UserInfo.mobile+"&bookid="+UserInfo.bookId+"&qty="+et.text.toString()
            var rq:RequestQueue=Volley.newRequestQueue(activity)
            var sr=StringRequest(Request.Method.GET,url,Response.Listener { response ->
                //Toast.makeText(activity,"normalement this takes u to Order list",Toast.LENGTH_LONG).show()
                var i2=Intent(activity,OrderAct::class.java)
                startActivity(i2)

            },Response.ErrorListener { error ->
                Toast.makeText(activity,error.message,Toast.LENGTH_LONG).show()
            })

            rq.add(sr)
        }

        return v
    }


}
