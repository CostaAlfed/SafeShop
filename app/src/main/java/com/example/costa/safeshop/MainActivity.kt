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
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
import java.util.*
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import android.app.ProgressDialog
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T


class MainActivity : AppCompatActivity() {
    var saltstr: String =""
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

        val ipad: String = getString(R.string.local_ip)
        var pwhashstr: String = ""

        get_salt_butt.setOnClickListener {
            val url3 ="http://" + ipad + "/SalesWeb/get_salt.php?number=" + login_number.text.toString()
            val rq3: RequestQueue = Volley.newRequestQueue(this)
            val sr3 = JsonArrayRequest(Request.Method.GET, url3,null, Response.Listener { response ->
                saltstr = response.getJSONObject(0).getString("salt")
                Toast.makeText(this, "Got Salt= "+saltstr, Toast.LENGTH_LONG).show()

            }, Response.ErrorListener { error ->
                Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
            })
            rq3.add(sr3)
            System.out.println(" --saltstr outside volley= " + saltstr)
            System.out.println("--")
        }

        get_hash_butt.setOnClickListener {
            val urlh ="http://" + ipad + "/SalesWeb/get_pwhash.php?number=" + login_number.text.toString()
            val rqh: RequestQueue = Volley.newRequestQueue(this)
            val srh = JsonArrayRequest(Request.Method.GET, urlh,null, Response.Listener { response ->
                pwhashstr = response.getJSONObject(0).getString("pwhash")
                Toast.makeText(this, "Got Hash= "+pwhashstr, Toast.LENGTH_LONG).show()
            }, Response.ErrorListener { error ->
                Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
            })
            rqh.add(srh)
            System.out.println(" --pwhashstr outside volley= " + pwhashstr)
            System.out.println("--")

        }

        signin_butt.setOnClickListener {
            //converting pwhashstr and saltstr2 back to BYTE ARRAY
            val pwhash = pwhashstr.toByteArray()
            System.out.println(" --pwhash= " + Arrays.toString(pwhash))
            val salt = saltstr.toByteArray()
            System.out.println(" --salt= " + Arrays.toString(salt))

            if (authenticate(login_password.text.toString(), pwhash, salt).equals(false)) {
                System.out.println(" --authenticate= false")
                System.out.println(" --loginpassword@auth= "+login_password.text.toString())
                System.out.println(" --pwhash@auth= "+Arrays.toString(pwhash))
                System.out.println(" --salt@auth= "+Arrays.toString(salt))
                Toast.makeText(this, "Sign In Failed", Toast.LENGTH_LONG).show()
            } else {
                System.out.println(" --authenticate= true")
                System.out.println(" --loginpassword= "+login_password.text.toString())
                System.out.println(" --pwhash= "+pwhash)
                System.out.println(" --salt= "+salt)
                UserInfo.mobile = login_number.text.toString()
                val i0 = Intent(this, HomeAct::class.java)
                var uname=""//DELETE THISSSSSS DELETE THISSSSSS DELETE THISSSSSSS DELETE THISSSSSS
                if (login_number.text.toString().equals("0791192226")) uname ="Costa"
                if (login_number.text.toString().equals("0790526861")) uname ="Smecta"

                Toast.makeText(this, "Welcome, " + uname + "!", Toast.LENGTH_LONG).show()
                System.out.println("-- \n --")
                startActivity(i0)
            }
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

