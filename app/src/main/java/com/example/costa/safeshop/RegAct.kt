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
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec
import java.util.Base64.getEncoder


class RegAct : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg)

        val keygen = KeyGenerator.getInstance("AES")
        keygen.init(256)
        val key: SecretKey = keygen.generateKey()
        //System.out.println("--key= "+key)
        val StringKey2 = Base64.getEncoder().encodeToString(key.encoded)
        //System.out.println("--StringKey2= "+StringKey2)
        //val bytekey = key.encoded
        //System.out.println("--bytekey= "+bytekey)
        //val StringKey = String(bytekey, Charsets.UTF_8)

        //var bundle :Bundle ?=intent.extras
        //var StringKey2 = bundle!!.getString("secretkey") // 1

        //var StringKey2=getIntent().getStringExtra("secretkey") //?: "" //remove the ?: ""
        //System.out.println("--StringKey2= "+StringKey2)

        //val byteKey2 = getDecoder().decode(StringKey2)
        //System.out.println("--byteKey2= "+byteKey2)
        // rebuild key using SecretKeySpec
        var tempcounter=0

        reg_butt.setOnClickListener{
            tempcounter++
            /*val signpass= reg_password.text.toString()
            System.out.println("--signpass= "+signpass)*/
            val bytesignpass: ByteArray = reg_password.text.toString().toByteArray(Charsets.UTF_8)
            System.out.println(""+tempcounter + "--bytesignpass= "+Arrays.toString(bytesignpass))
            val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
            cipher.init(Cipher.ENCRYPT_MODE, key)
            val ciphertext: ByteArray = cipher.doFinal(bytesignpass)
            System.out.println(""+tempcounter+"--ciphertext= "+Arrays.toString(ciphertext))
            val iv: ByteArray = cipher.iv
            System.out.println(""+tempcounter+"--iv= "+Arrays.toString(iv))
            val signpassencripted = Arrays.toString(ciphertext)
            System.out.println(""+tempcounter+"--signpassencripted= "+signpassencripted)

            if (reg_password.text.toString().equals(password_confirm.text.toString())) {
                var ipad:String=getString(R.string.local_ip)
                var url =
                    "http://"+ipad+"/SalesWeb/add_user.php?number=" + reg_number.text.toString() + "&password=" +
                            signpassencripted +"strkey="+StringKey2+ "&name=" + reg_name.text.toString() +
                            "&address=" + reg_address.text.toString()
                var rq:RequestQueue=Volley.newRequestQueue(this)
                var sr=StringRequest(Request.Method.GET,url,Response.Listener { response ->
                    if (response.equals("0"))
                    {
                        Toast.makeText(this,"Number already used!",Toast.LENGTH_LONG).show()
                        System.out.println(""+tempcounter+"--response= 0")} //DELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETE
                    else {System.out.println(""+tempcounter+"--response= 1") //DELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETE
                        UserInfo.mobile=reg_number.text.toString()
                        var i = Intent(this, MainActivity::class.java)
                        startActivity(i)
                    }
                },Response.ErrorListener { error ->
                    Toast.makeText(this,error.message,Toast.LENGTH_LONG).show()
                    System.out.println(""+tempcounter+"--errormessage= "+error.message)
                })
                rq.add(sr)
            }
            else
                Toast.makeText(this,"Passwords don't match!",Toast.LENGTH_LONG).show()

        }
    }
}
