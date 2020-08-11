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
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
import java.util.*
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec


class MainActivity : AppCompatActivity() {
    @Throws(NoSuchAlgorithmException::class, InvalidKeySpecException::class)
    fun getEncryptedPassword(password: String, salt: ByteArray): ByteArray {
        val spec = PBEKeySpec(password.toCharArray(), salt, 20000, 160)
        val f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
        return f.generateSecret(spec).getEncoded()
    }

    @Throws(NoSuchAlgorithmException::class, InvalidKeySpecException::class)
    fun authenticate(attemptedPassword: String, encryptedPassword: ByteArray, salt: ByteArray): Boolean {
        // Encrypt the clear-text password using the same salt that was used to encrypt the original password
        val encryptedAttemptedPassword = getEncryptedPassword(attemptedPassword, salt)

        // Authentication succeeds if encrypted password that the user entered is equal to the stored hash
        return Arrays.equals(encryptedPassword, encryptedAttemptedPassword)
    }

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
        println("--StringKey_typecheck"+"${StringKey::class.simpleName}"+"___expected: String")
*/
        val ipad: String = getString(R.string.local_ip)
        val url3 ="http://" + ipad + "/SalesWeb/get_salt.php?number=" + login_number.text.toString()
        val urlh ="http://" + ipad + "/SalesWeb/get_pwhash.php?number=" + login_number.text.toString()
        var waiter1=0
        var waiter2=0

        var pwhashstr: String = ""
        signin_butt.setOnClickListener {
            var saltstr2: String ="initial string of salt."
            val rq3: RequestQueue = Volley.newRequestQueue(this)
            System.out.println("--bang2")
            val sr3 = StringRequest(Request.Method.GET, url3, Response.Listener { response ->
                var saltstr = response
                saltstr2=saltstr
                System.out.println(" --saltstr2= " + saltstr2)
                waiter1++
            }, Response.ErrorListener { error ->
                Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
            })
            rq3.add(sr3)

            val rqh: RequestQueue = Volley.newRequestQueue(this)
            val srh = StringRequest(Request.Method.GET, urlh, Response.Listener { response ->
                pwhashstr = response.toString()
                System.out.println(" --pwhashstr= " + pwhashstr)
                waiter2++
            }, Response.ErrorListener { error ->
                Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
            })
            rqh.add(srh)
            var waittimes=0
            while (waiter1==0 && waiter2==0) {
                System.out.println("--waiter1= "+waiter1)
                System.out.println("--waiter2= "+waiter2)
                System.out.println("--wait times= "+waittimes)
                Thread.sleep(3000)
                System.out.println("")
            }

            //converting pwhashstr and saltstr2 back to BYTE ARRAY
            val pwhash = pwhashstr.toByteArray(Charsets.UTF_8)
            System.out.println(" --pwhash= " + Arrays.toString(pwhash))
            val salt = saltstr2.toByteArray(Charsets.UTF_8)
            System.out.println(" --salt= " + Arrays.toString(salt))

/*
        val ByteKeyEq = Base64.getDecoder().decode(StringKeyEq)
        System.out.println("--byteKeyEq= "+ByteKeyEq)
        val originalKey = SecretKeySpec(ByteKeyEq, 0, ByteKeyEq.size, "AES")
        System.out.println("--originalKey= "+originalKey)
*/


            /*/--------------------------ENCRYPTION-----------------------------
        val plaintext: ByteArray = login_password.text.toString().toByteArray()
        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.ENCRYPT_MODE, originalKey)
        val ciphertext: ByteArray = cipher.doFinal(plaintext)
        System.out.println("--ciphertext= "+ciphertext)
        val iv: ByteArray = cipher.iv
        System.out.println("--iv= "+iv)
        val logpassencripted = String(ciphertext, Charsets.UTF_8)
        System.out.println("--logpassencripted= "+logpassencripted)

        //-----------------------------------------------------------------*/
            //var url =
            //            "http://"+ipad+"/SalesWeb/login.php?number=" + login_number.text.toString() + "&password=" +
            //                    login_password.text.toString()

            if (authenticate(login_password.text.toString(), pwhash, salt).equals(false)) {
                System.out.println(" --authenticate= false")
/*
        var url =
            "http://"+ipad+"/SalesWeb/login.php?number=" + login_number.text.toString() + "&pwhash=" + encpass
        var rq: RequestQueue = Volley.newRequestQueue(this)
        var sr= StringRequest(Request.Method.GET,url, Response.Listener { response ->
            if (response.equals("0"))

 */
                Toast.makeText(this, "Sign In Failed", Toast.LENGTH_LONG).show()
            } else {
                System.out.println(" --authenticate= true")
                UserInfo.mobile = login_number.text.toString()
                val i0 = Intent(this, HomeAct::class.java)
                var uname: String =
                    ""               //DELETE THISSSSSS DELETE THISSSSSS DELETE THISSSSSSS DELETE THISSSSSS DELETE THISSSSSS DELETE THISSSSSS
                if (login_number.text.toString().equals("0799123456")) uname =
                    "Anis" //DELETE THISSSSSSS DELETE THISSSSSS DELETE THISSSSSS  DELETE THISSSSSS
                if (login_number.text.toString().equals("0559235411")) uname =
                    "Smecta" //DELETE THISSSSSS DELETE THISSSSSS DELETE THISSSSS  DELETE THISSSSSS
                if (login_number.text.toString().equals("0799789123")) uname =
                    "Costa" //DELETE THISSSSSS DELETE THISSSSSS DELETE THISSSSSS  DELETE THISSSSSS
                if (login_number.text.toString().equals("0669420420")) uname =
                    "Jon Doe" //DELETE THISSSSSS DELETE THISSSSSS DELETE THISSSSSS  DELETE THISSSSSS
                if (login_number.text.toString().equals("0555123456")) uname =
                    "Dwight" //DELETE THISSSSSS DELETE THISSSSSS DELETE THISSSSSS  DELETE THISSSSSS
                if (login_number.text.toString().equals("0700000000")) uname =
                    "Mouh" //DELETE THISSSSSS DELETE THISSSSSS DELETE THISSSSSS  DELETE THISSSSSS

                Toast.makeText(this, "Welcome, " + uname + "!", Toast.LENGTH_LONG)
                    .show()      //DELETE THISSSSSS DELETE THISSSSSS DELETE THISSSSSS
                startActivity(i0)

            }
/*        }, Response.ErrorListener { error ->
            Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
        })
        rq.add(sr)
    }
*/
        }
            signup_butt.setOnClickListener {
                val i = Intent(this, RegAct::class.java)
                //intent.putExtra("secretkey",StringKey)
                //System.out.println("--AES key (StringKey sent in mainact to regact) = "+StringKey)
                startActivity(i)
            }

            contact_us_butt.setOnClickListener {
                if (contact_layout.isVisible) contact_layout.visibility = View.INVISIBLE
                else contact_layout.visibility = View.VISIBLE
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

