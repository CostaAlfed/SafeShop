package com.example.costa.safeshop

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.paypal.android.sdk.payments.PayPalConfiguration
import com.paypal.android.sdk.payments.PayPalPayment
import com.paypal.android.sdk.payments.PayPalService
import com.paypal.android.sdk.payments.PaymentActivity
import kotlinx.android.synthetic.main.activity_total.*
import java.math.BigDecimal
import java.math.RoundingMode
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import java.text.DecimalFormat


class TotalAct : AppCompatActivity() {

    var config:PayPalConfiguration?=null
    var amount:Double=0.0
    var last_id:String=""
    var bnb2:String=""
    var meth:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_total)
        bnb2=intent.getStringExtra("bnb")
        var total:Int=0
        var ipad:String=getString(R.string.local_ip)
        var url="http://"+ipad+"/SalesWeb/get_total.php?bill_no="+ bnb2
        var rq: RequestQueue = Volley.newRequestQueue(this)
        var sr= StringRequest(Request.Method.GET,url, Response.Listener { response ->
            total=response.toInt()
            total_tv.text=response

        }, Response.ErrorListener { error ->
            Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
        })

        rq.add(sr)

        var url2="http://"+ipad+"/SalesWeb/get_bill_date.php"
        var rq2: RequestQueue = Volley.newRequestQueue(this)
        var sr2= StringRequest(Request.Method.GET,url2,Response.Listener { response ->
            date.text=response
        }, Response.ErrorListener { error ->
            Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
        })

        rq2.add(sr2)

        var url3="http://"+ipad+"/SalesWeb/get_last_bill.php"
        var rq3: RequestQueue = Volley.newRequestQueue(this)
        var sr3= StringRequest(Request.Method.GET,url3,Response.Listener { response ->
            order_no.text=response
        }, Response.ErrorListener { error ->
            Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
        })
        rq3.add(sr3)


        var url4="http://"+ipad+"/SalesWeb/get_max_rate.php"
        var rq4: RequestQueue = Volley.newRequestQueue(this)
        var sr4= StringRequest(Request.Method.GET,url4,Response.Listener { response ->
            last_id=response
            var exchange_rate:Double=0.0000000
            var url5="http://"+ipad+"/SalesWeb/get_exchange_rate.php?id="+last_id
            var rq5: RequestQueue = Volley.newRequestQueue(this)
            var sr5= StringRequest(Request.Method.GET,url5,Response.Listener { response ->
                exchange_rate=response.toDouble()
                val df = DecimalFormat("0.00")
                var total_in_eur1=total*exchange_rate
                var total_in_eur2=df.format(total_in_eur1)
                total_eur.text=total_in_eur2.toString()
            }, Response.ErrorListener { error ->
                Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
            })
            rq5.add(sr5)
        }, Response.ErrorListener { error ->
            Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
        })
        rq4.add(sr4)

        //var bigD=exchange_rate.toBigDecimal().setScale(2, RoundingMode.HALF_EVEN)

        show_tip_1.setOnClickListener {
            if (tip_eur.isVisible)   tip_eur.visibility= View.INVISIBLE
            else tip_eur.visibility= View.VISIBLE

        }

        config=PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(UserInfo.client_id)
        var i=Intent(this,PayPalService::class.java)
        i.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config)
        startService(i)

        paypal_butt.setOnClickListener {
            amount=total_eur.text.toString().toDouble()
            var payment=PayPalPayment(BigDecimal.valueOf(amount),"EUR","DzBookStore", PayPalPayment.PAYMENT_INTENT_SALE)
            var intent=Intent(this,PaymentActivity::class.java)
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config)
            intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payment)
            startActivityForResult(intent,123)
            /*var int=Intent(this,TicketAct::class.java)
            int.putExtra("bnb2",bnb2)
            startActivity(int)*/
        }
        ccp_butt.setOnClickListener {
            val int=Intent(this,CCPAct::class.java)
            int.putExtra("bnb2",bnb2)
            int.putExtra("meth","CCP")
            startActivity(int)
        }
        cib_butt.setOnClickListener {
            val int=Intent(this,CIBAct::class.java)
            int.putExtra("bnb2",bnb2)
            int.putExtra("meth","CiB")
            startActivity(int)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        bnb2=intent.getStringExtra("bnb")
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==123){
            if (resultCode==Activity.RESULT_OK){
                var obj=Intent(this,TicketAct::class.java)
                obj.putExtra("bnb2",bnb2)
                obj.putExtra("meth","Paypal")
                startActivity(obj)
            }
        }
    }

    override fun onDestroy() {
        stopService(Intent(this,PayPalService::class.java))
        super.onDestroy()
    }
}
