package com.example.costa.safeshop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_order.*

class OrderAct : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        var ipad:String=getString(R.string.local_ip)
        var url="http://"+ipad+"/SalesWeb/get_temp.php?number="+UserInfo.mobile
        var list=ArrayList<String>()
        var rq: RequestQueue = Volley.newRequestQueue(this)
        var jar= JsonArrayRequest(Request.Method.GET,url,null, Response.Listener { response ->
            for (x in 0 .. response.length()-1){
                var Otitle:String=response.getJSONObject(x).getString("title")
                var Oauthor:String=response.getJSONObject(x).getString("author")
                var Oprice:String=response.getJSONObject(x).getString("price")
                var Oqty:String=response.getJSONObject(x).getString("qty")
                var totqty:String
                if (Oqty.toInt()==1)
                    totqty=""
                else
                    totqty="                Total:  "+Oprice.toInt()*Oqty.toInt()+" DZD"

                list.add("Title:     "+Otitle+ "\n" //"        by: "+Oauthor+ "
                        +"Author:   "+Oauthor+ "\n"
                        +"Price:     "+Oprice+ " DZD \n"
                        +"Quantity: "+Oqty+totqty)}
            var adp= ArrayAdapter(this, R.layout.my_list_item,list)
            orders_list.adapter=adp
        }, Response.ErrorListener { error ->
            Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
        })
        rq.add(jar)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item?.itemId==R.id.item_menu){
            var i=Intent(this,HomeAct::class.java)
            startActivity(i)
        }

        if (item.itemId==R.id.item_cancel)
        {
            var ipad:String=getString(R.string.local_ip)
            var url2="http://"+ipad+"/SalesWeb/delete_order_by_number.php?number="+UserInfo.mobile
            var rq:RequestQueue=Volley.newRequestQueue(this)
            var sr= StringRequest(Request.Method.GET,url2,Response.Listener { response ->

                var i2=Intent(this,HomeAct::class.java)
                startActivity(i2)

            },Response.ErrorListener { error ->
                Toast.makeText(this,error.message,Toast.LENGTH_LONG).show()
            })

            rq.add(sr)
        }

        if (item.itemId==R.id.item_confirm){
            var ipad2:String=getString(R.string.local_ip)
            var url3="http://"+ipad2+"/SalesWeb/confirm_order.php?number="+UserInfo.mobile
            var rq:RequestQueue=Volley.newRequestQueue(this)
            var sr= StringRequest(Request.Method.GET,url3,Response.Listener { response ->

                var i2=Intent(this,TotalAct::class.java)
                i2.putExtra("bnb",response)
                startActivity(i2)

            },Response.ErrorListener { error ->
                Toast.makeText(this,error.message,Toast.LENGTH_LONG).show()
            })

            rq.add(sr)

        }

        return super.onOptionsItemSelected(item)
    }

}




