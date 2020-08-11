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
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;




class RegAct : AppCompatActivity() {



        @Throws(NoSuchAlgorithmException::class, InvalidKeySpecException::class)
        fun getEncryptedPassword(password: String, salt: ByteArray): ByteArray {

            val derivedKeyLength = 160 // SHA-1 generates 160 bit hashes, so that's what makes sense here

            val iterations = 20000 // Pick an iteration count that works for you.
            //The NIST recommends at least 1,000 iterations: http://csrc.nist.gov/publications/nistpubs/800-132/nist-sp800-132.pdf

            val spec = PBEKeySpec(password.toCharArray(), salt, iterations, derivedKeyLength)

            // PBKDF2 with SHA-1 as the hashing algorithm. Note that the NIST
            // specifically names SHA-1 as an acceptable hashing algorithm for PBKDF2
            val f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")

            return f.generateSecret(spec).getEncoded()
        }

        @Throws(NoSuchAlgorithmException::class)
        fun generateSalt(): ByteArray {
            // VERY important to use SecureRandom instead of just Random
            val random = SecureRandom.getInstance("SHA1PRNG")

            // Generate a 8 byte (64 bit) salt as recommended by RSA PKCS5
            val salt = ByteArray(8)
            random.nextBytes(salt)

            return salt
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg)

        /*

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
         */

        var tempcounter=0

        reg_butt.setOnClickListener{
            tempcounter++

            var saltbae=generateSalt()
            System.out.println(""+tempcounter+" --saltbae= "+Arrays.toString(saltbae))
            var encpass=getEncryptedPassword(reg_password.text.toString(),saltbae)
            System.out.println(""+tempcounter+" --encpass= "+Arrays.toString(encpass))
            var encpassstr=Arrays.toString(encpass)
            System.out.println(""+tempcounter+" --encpassstr= "+encpassstr)

/*

            /*val signpass= reg_password.text.toString()
            System.out.println("--signpass= "+signpass)*/
            val bytesignpass: ByteArray = reg_password.text.toString().toByteArray(Charsets.UTF_8)
            //System.out.println(""+tempcounter + "--bytesignpass= "+Arrays.toString(bytesignpass))
            val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
            cipher.init(Cipher.ENCRYPT_MODE, key)
            val ciphertext: ByteArray = cipher.doFinal(bytesignpass)
            //System.out.println(""+tempcounter+"--ciphertext= "+Arrays.toString(ciphertext))
            val iv: ByteArray = cipher.iv
            //System.out.println(""+tempcounter+"--iv= "+Arrays.toString(iv))
            val signpassencripted = Arrays.toString(ciphertext)
            //System.out.println(""+tempcounter+"--signpassencripted= "+signpassencripted)
 */

            if (reg_password.text.toString().equals(password_confirm.text.toString())) {
                val ipad:String=getString(R.string.local_ip)
                val url =
                    "http://"+ipad+"/SalesWeb/add_user.php?number=" + reg_number.text.toString() + "&pwhash=" +
                            encpassstr +"&salt="+Arrays.toString(saltbae)+ "&name=" + reg_name.text.toString() +
                            "&address=" + reg_address.text.toString()
                val rq:RequestQueue=Volley.newRequestQueue(this)
                val sr=StringRequest(Request.Method.GET,url,Response.Listener { response ->
                    if (response.equals("0"))
                    {
                        Toast.makeText(this,"Number already used!",Toast.LENGTH_LONG).show()
                        System.out.println(""+tempcounter+"--response= 0")} //DELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETE
                    else {System.out.println(""+tempcounter+"--response= 1") //DELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETE
                        UserInfo.mobile=reg_number.text.toString()
                        val i = Intent(this, MainActivity::class.java)
                        startActivity(i)
                    }
                },Response.ErrorListener { error ->
                    Toast.makeText(this,error.message,Toast.LENGTH_LONG).show()
                    System.out.println(""+tempcounter+"--errormessage= "+error.message)
                })
                rq.add(sr)
            }
            else {
                System.out.println("Passwords don't match!")
                Toast.makeText(this, "Passwords don't match!", Toast.LENGTH_LONG).show()
            }
        }
    }
}
