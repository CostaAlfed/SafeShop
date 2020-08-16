package com.example.costa.safeshop


import android.content.Context
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
import android.net.ConnectivityManager
import androidx.core.text.isDigitsOnly
import java.nio.charset.Charset
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


    companion object {
        @Throws(NoSuchAlgorithmException::class, InvalidKeySpecException::class)
        fun getEncryptedPassword(password: String, salt: ByteArray): ByteArray {
            val derivedKeyLength =
                160 // SHA-1 generates 160 bit hashes, so that's what makes sense here

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

        @Throws(NoSuchAlgorithmException::class, InvalidKeySpecException::class)
        fun authenticate(attemptedPassword: String, encryptedPassword: ByteArray, salt: ByteArray): Boolean {
            // Encrypt the clear-text password using the same salt that was used to encrypt the original password
            val encryptedAttemptedPassword = getEncryptedPassword(attemptedPassword, salt)
            System.out.println(" --encryptedAttemptedPassword: Attempted PASS HASHED by AUTH= " + Base64.getEncoder().encodeToString(encryptedAttemptedPassword))

            // Authentication succeeds if encrypted password that the user entered is equal to the stored hash
            return Arrays.equals(encryptedPassword, encryptedAttemptedPassword)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg)

         reg_butt.setOnClickListener{
             var saltbae=generateSalt()
             System.out.println("--saltbae= "+Base64.getEncoder().encodeToString(saltbae))

             var saltbaestr = Base64.getEncoder().encodeToString(saltbae)
             System.out.println("--saltbaestr= "+saltbaestr)

             var encpass=getEncryptedPassword(reg_password.text.toString(),saltbae)
             System.out.println("--encpass= "+Base64.getEncoder().encodeToString(encpass))

             var encpassstr=Base64.getEncoder().encodeToString(encpass)
             System.out.println("--encpassstr= "+encpassstr)

             if ((!reg_number.text.toString().isDigitsOnly()) || reg_number.text.toString().length!=10 )
                Toast.makeText(this, "Invalid Phone Number", Toast.LENGTH_LONG).show()
             else {
                 if (reg_password.text.toString().equals(password_confirm.text.toString())) {
                     val ipad:String=getString(R.string.local_ip)
                     val url =
                        "http://"+ipad+"/SalesWeb/add_user.php?number=" + reg_number.text.toString() + "&pwhash=" +
                                encpassstr +"&salt="+saltbaestr+ "&name=" + reg_name.text.toString() +
                                "&address=" + reg_address.text.toString()
                    val rq:RequestQueue=Volley.newRequestQueue(this)
                    val sr=StringRequest(Request.Method.GET,url,Response.Listener { response ->
                        if (response.equals("0"))
                        {
                            Toast.makeText(this,"Number already used!",Toast.LENGTH_LONG).show()
                            System.out.println("--response= 0")} //DELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETE
                        else {System.out.println("--response= 1") //DELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETE
                            UserInfo.mobile=reg_number.text.toString()
                            val i = Intent(this, MainActivity::class.java)
                            startActivity(i)
                        }
                    },Response.ErrorListener { error ->
                        Toast.makeText(this,error.message,Toast.LENGTH_LONG).show()
                        System.out.println("--errormessage= "+error.message)
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
}
