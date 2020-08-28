package com.example.costa.safeshop

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_ticket.*
import kotlinx.android.synthetic.main.activity_total.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class TicketAct : AppCompatActivity() {
    var amount:Double=0.0
    var last_id:String=""

    companion object {private val ALLOWED_CHARACTERS = "0123456789AZERTYUIOPQSDFGHJKLMWXCVBN"}

    private fun getTransactionCode(sizeOfRandomString: Int): String {
        val random = Random()
        val sb = StringBuilder(sizeOfRandomString)
        for (i in 0 until sizeOfRandomString)
            sb.append(ALLOWED_CHARACTERS[random.nextInt(ALLOWED_CHARACTERS.length)])
        return sb.toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket)

        val bnb3=intent.getStringExtra("bnb2")
        var total:Int=0
        val ipad:String=getString(R.string.local_ip)
        val url="http://"+ipad+"/SalesWeb/get_total.php?bill_no="+ bnb3
        val rq: RequestQueue = Volley.newRequestQueue(this)
        val sr= StringRequest(Request.Method.GET,url, Response.Listener { response ->
            total=response.toInt()
            total_tv2.text=response
        }, Response.ErrorListener { error ->
            Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
        })
        rq.add(sr)

        val url2="http://"+ipad+"/SalesWeb/get_bill_date.php"
        val rq2: RequestQueue = Volley.newRequestQueue(this)
        val sr2= StringRequest(Request.Method.GET,url2,Response.Listener { response ->
            date2.text=response
        }, Response.ErrorListener { error ->
            Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
        })
        rq2.add(sr2)

        val url3="http://"+ipad+"/SalesWeb/get_last_bill.php"
        val rq3: RequestQueue = Volley.newRequestQueue(this)
        val sr3= StringRequest(Request.Method.GET,url3,Response.Listener { response ->
            order_no2.text=response
        }, Response.ErrorListener { error ->
            Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
        })
        rq3.add(sr3)


        val url4="http://"+ipad+"/SalesWeb/get_max_rate.php"
        val rq4: RequestQueue = Volley.newRequestQueue(this)
        val sr4= StringRequest(Request.Method.GET,url4,Response.Listener { response ->
            last_id=response
            var exchange_rate:Double=0.0000000
            val url5="http://"+ipad+"/SalesWeb/get_exchange_rate.php?id="+last_id
            val rq5: RequestQueue = Volley.newRequestQueue(this)
            val sr5= StringRequest(Request.Method.GET,url5,Response.Listener { response ->
                exchange_rate=response.toDouble()
                val df = DecimalFormat("0.00")
                val total_in_eur1=total*exchange_rate
                val total_in_eur2=df.format(total_in_eur1)
                total_eur2.text=total_in_eur2.toString()
            }, Response.ErrorListener { error ->
                Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
            })
            rq5.add(sr5)
        }, Response.ErrorListener { error ->
            Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
        })
        rq4.add(sr4)

        val url6 ="http://"+ipad+"/SalesWeb/get_address.php?number=" + UserInfo.mobile
        val rq6: RequestQueue = Volley.newRequestQueue(this)
        val sr6 = JsonArrayRequest(Request.Method.GET, url6,null, Response.Listener { response ->
            addr.text = response.getJSONObject(0).getString("address")
        }, Response.ErrorListener { error ->
            Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
        })
        rq6.add(sr6)

        trans_code.text=getTransactionCode(12)

        ticket_butt_done.setOnClickListener {
            var int= Intent(this,confirmAct::class.java)
            startActivity(int)
        }

        ticket_butt_save.setOnClickListener {Toast.makeText(this,
            "Ticket Saved in: "+saveImage(generateBimap(ticketView)),Toast.LENGTH_LONG).show()}

    }



    private fun generateBimap(view: View): Bitmap {
        // Create a bitmap with same dimensions as view
        val bitmap: Bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888)
        // Create a canvas using bitmap
        val canvas: Canvas = Canvas(bitmap)
        // We need to check if view as backround image.
        /*val baground:Drawable = view.getBackground()
        if (baground != null) baground.draw(canvas) else {canvas.drawColor(Color.WHITE)}*/
        // draw the view on the canvas
        view.draw(canvas)
        // final bitmap
        return bitmap
    }

    private fun saveImage(bitmap: Bitmap): String {
        // Create Destination folder in external storage. This will require EXTERNAL STORAGE permission
        val imgDir:String  = Environment.getExternalStorageDirectory().toString()+"/DzBookStore"
        // Generate a random file name for image
        val imageName:String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date()) + ".jpeg"
        val localFile= File(imgDir, imageName)
        localFile.renameTo(localFile)
        val path:String = "file:/" + imgDir

        try {
            val fos= FileOutputStream(localFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
            sendBroadcast((Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(
                File(path)
            ))))
        } catch (e:Exception)  {e.printStackTrace()}
        // Local path to be shown to User to tell where the Image has been saved.
        return path
    }
}
