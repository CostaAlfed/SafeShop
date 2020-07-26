package com.example.costa.safeshop


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_reg.*
import javax.crypto.Cipher
import java.util.Base64.getDecoder
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import java.util.*
import javax.crypto.spec.SecretKeySpec


class RegAct : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg)

        val encodedKey2=intent.getStringExtra("AES key")
        System.out.println("encodedKey2= "+encodedKey2)
        val signpass= reg_password.text.toString()
        System.out.println("signpass= "+signpass)
        val plaintext: ByteArray = signpass.toByteArray()
        System.out.println("plaintext= "+plaintext)
        val decodedKey = getDecoder().decode(encodedKey2)
        System.out.println("decodedKey= "+decodedKey)
        // rebuild key using SecretKeySpec
        val originalKey = SecretKeySpec(decodedKey, 0, decodedKey.size, "AES")
        System.out.println("originalKey= "+originalKey)

        reg_butt.setOnClickListener{
            val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
            cipher.init(Cipher.ENCRYPT_MODE, originalKey)
            val ciphertext: ByteArray = cipher.doFinal(plaintext)
            val iv: ByteArray = cipher.iv
            val logpassencripted = String(ciphertext)
            if (reg_password.text.toString().equals(password_confirm.text.toString())) {
                var ipad:String=getString(R.string.local_ip)
                var url =
                    "http://"+ipad+"/SalesWeb/add_user.php?number=" + reg_number.text.toString() + "&password=" +
                            logpassencripted + "&name=" + reg_name.text.toString() +
                            "&address=" + reg_address.text.toString()
                var rq:RequestQueue=Volley.newRequestQueue(this)
                var sr=StringRequest(Request.Method.GET,url,Response.Listener { response ->
                    if (response.equals("0"))
                        Toast.makeText(this,"Number already used!",Toast.LENGTH_LONG).show()
                    else {
                        UserInfo.mobile=reg_number.text.toString()
                        var i = Intent(this, MainActivity::class.java)
                        startActivity(i)
                    }
                },Response.ErrorListener { error ->
                    Toast.makeText(this,error.message,Toast.LENGTH_LONG).show()
                })
                rq.add(sr)
            }
            else
                Toast.makeText(this,"Passwords don't match!",Toast.LENGTH_LONG).show()

        }
    }
}
