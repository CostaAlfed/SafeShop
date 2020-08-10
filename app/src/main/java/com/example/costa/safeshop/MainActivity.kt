package com.example.costa.safeshop


import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_reg.*
import kotlinx.android.synthetic.main.activity_total.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import java.util.Base64.getEncoder
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import io.card.payment.i18n.StringKey
import java.util.*
import javax.crypto.spec.SecretKeySpec


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        /*
        var logpass = login_password.text.toString()
        //System.out.println("--logpass= "+logpass)
        val plaintext: ByteArray = logpass.toByteArray()
        //System.out.println("--plaintext= "+plaintext)
        val keygen = KeyGenerator.getInstance("AES")
        keygen.init(256)
        val key: SecretKey = keygen.generateKey()
        //System.out.println("--key= "+key)
        val bytekey = key.encoded
        //System.out.println("--bytekey= "+bytekey)
        val StringKey = String(bytekey, Charsets.UTF_8)
        //System.out.println("--StringKey= "+StringKey)
        println("--StringKey_typecheck"+"${StringKey::class.simpleName}"+"___expected: String")*/

    signin_butt.setOnClickListener {
        var StringKeyEq:String=""
        var ipad:String=getString(R.string.local_ip)
        var urlh =
            "http://"+ipad+"/SalesWeb/get_strkey.php?number=" + login_number.text.toString()
        var rqh: RequestQueue = Volley.newRequestQueue(this)
        var srh= StringRequest(Request.Method.GET,urlh, Response.Listener { response ->
            StringKeyEq=response
        }, Response.ErrorListener { error ->
            Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
        })
        rqh.add(srh)

        val ByteKeyEq = Base64.getDecoder().decode(StringKeyEq)
        System.out.println("--byteKeyEq= "+ByteKeyEq)
        val originalKey = SecretKeySpec(ByteKeyEq, 0, ByteKeyEq.size, "AES")
        System.out.println("--originalKey= "+originalKey)


        //--------------------------ENCRYPTION-----------------------------
        val plaintext: ByteArray = login_password.text.toString().toByteArray()
        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.ENCRYPT_MODE, originalKey)
        val ciphertext: ByteArray = cipher.doFinal(plaintext)
        System.out.println("--ciphertext= "+ciphertext)
        val iv: ByteArray = cipher.iv
        System.out.println("--iv= "+iv)
        val logpassencripted = String(ciphertext, Charsets.UTF_8)
        System.out.println("--logpassencripted= "+logpassencripted)

        //-----------------------------------------------------------------
        //var url =
        //            "http://"+ipad+"/SalesWeb/login.php?number=" + login_number.text.toString() + "&password=" +
        //                    login_password.text.toString()
        var url =
            "http://"+ipad+"/SalesWeb/login.php?number=" + login_number.text.toString() + "&password=" + logpassencripted
        var rq: RequestQueue = Volley.newRequestQueue(this)
        var sr= StringRequest(Request.Method.GET,url, Response.Listener { response ->
            if (response.equals("0"))
                Toast.makeText(this,"Sign In Failed", Toast.LENGTH_LONG).show()
            else {
                //var url2 =
                  //  "http://"+ipad+"/SalesWeb/get_user_name.php?number=" + login_number.text.toString()
                UserInfo.mobile=login_number.text.toString()
                var i0 = Intent(this, HomeAct::class.java)
                var uname:String=""               //DELETE THISSSSSS DELETE THISSSSSS DELETE THISSSSSSS DELETE THISSSSSS DELETE THISSSSSS DELETE THISSSSSS
                if (login_number.text.toString().equals("0799123456")) uname="Anis" //DELETE THISSSSSSS DELETE THISSSSSS DELETE THISSSSSS  DELETE THISSSSSS
                if (login_number.text.toString().equals("0559235411")) uname="Smecta" //DELETE THISSSSSS DELETE THISSSSSS DELETE THISSSSS  DELETE THISSSSSS
                if (login_number.text.toString().equals("0799789123")) uname="Costa" //DELETE THISSSSSS DELETE THISSSSSS DELETE THISSSSSS  DELETE THISSSSSS
                if (login_number.text.toString().equals("0669420420")) uname="Jon Doe" //DELETE THISSSSSS DELETE THISSSSSS DELETE THISSSSSS  DELETE THISSSSSS
                if (login_number.text.toString().equals("0555123456")) uname="Dwight" //DELETE THISSSSSS DELETE THISSSSSS DELETE THISSSSSS  DELETE THISSSSSS
                if (login_number.text.toString().equals("0700000000")) uname="Mouh" //DELETE THISSSSSS DELETE THISSSSSS DELETE THISSSSSS  DELETE THISSSSSS

                Toast.makeText(this,"Welcome, "+uname+"!", Toast.LENGTH_LONG).show()      //DELETE THISSSSSS DELETE THISSSSSS DELETE THISSSSSS
                startActivity(i0)

            }
        }, Response.ErrorListener { error ->
            Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
        })
        rq.add(sr)
    }

        signup_butt.setOnClickListener{
            var i=Intent(this,RegAct::class.java)
            //intent.putExtra("secretkey",StringKey)
            //System.out.println("--AES key (StringKey sent in mainact to regact) = "+StringKey)
            startActivity(i)
        }

        contact_us_butt.setOnClickListener {
            if (contact_layout.isVisible)   contact_layout.visibility= View.INVISIBLE
            else contact_layout.visibility= View.VISIBLE
        }
        twitter_butt.setOnClickListener {
            val openURL = Intent(android.content.Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://twitter.com/costaalfed/")
            startActivity(openURL)
        }
        facebook_butt.setOnClickListener {
            val openURL = Intent(android.content.Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://www.facebook.com/costalfed/")
            startActivity(openURL)
        }
        instagram_butt.setOnClickListener {
            val openURL = Intent(android.content.Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://www.instagram.com/costa_alfed/")
            startActivity(openURL)
        }
        reddit_butt.setOnClickListener {
            val openURL = Intent(android.content.Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://www.reddit.com/user/Costaalfed")
            startActivity(openURL)
        }
        linkdin_butt.setOnClickListener {
            val openURL = Intent(android.content.Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://www.linkedin.com/in/costa-alfed-a18047176/")
            startActivity(openURL)
        }
        whatsapp_butt.setOnClickListener {
            val openURL = Intent(android.content.Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://wa.me/213791192226")
            startActivity(openURL)
        }
        email_butt.setOnClickListener {
            val openURL = Intent(android.content.Intent.ACTION_VIEW)
            openURL.data = Uri.parse("mailto:alfedcosta@gmail.com")
            startActivity(openURL)
        }
    }
}
