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
import android.os.Handler
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.safetynet.SafetyNet
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import java.util.concurrent.Executor

public class MainActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

            val ipad: String = getString(R.string.local_ip)
            var saltstr: String =""
            var pwhashstr: String = ""

            fun get_salt(){
                val url3 ="http://" + ipad + "/SalesWeb/get_salt.php?number=" + login_number.text.toString()
                val rq3: RequestQueue = Volley.newRequestQueue(this)
                val sr3 = JsonArrayRequest(Request.Method.GET, url3,null, Response.Listener { response ->
                    saltstr = response.getJSONObject(0).getString("salt")
                }, Response.ErrorListener { error ->
                    Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
                })
                rq3.add(sr3)
                System.out.println(" --saltstr: SALT from db outside volley = " + saltstr)
            }

            fun get_hash(){
            val urlh ="http://" + ipad + "/SalesWeb/get_pwhash.php?number=" + login_number.text.toString()
            val rqh: RequestQueue = Volley.newRequestQueue(this)
            val srh = JsonArrayRequest(Request.Method.GET, urlh,null, Response.Listener { response ->
                pwhashstr = response.getJSONObject(0).getString("pwhash")
            }, Response.ErrorListener { error ->
                Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
            })
            rqh.add(srh)
            System.out.println(" --pwhashstr: HASH from db outside volley= " + pwhashstr)
        }

            signin_butt.setOnClickListener {
                if (login_number.text.toString().isBlank()
                    || login_password.text.toString().isBlank()
                )
                    Toast.makeText(this,
                        "Please enter both \n the number and password", Toast.LENGTH_LONG).show()
                else{
                get_salt()
                get_hash()
                var handler = Handler()
                Toast.makeText(this, "Authenticating. Please Wait...", Toast.LENGTH_LONG).show()
                handler.postDelayed({
                    //converting pwhashstr and saltstr back to BYTE ARRAY
                    val pwhash = Base64.getDecoder().decode(pwhashstr)
                    System.out.println(
                        " --salt: SALT from db IN BYTES right before AUTH new= "
                                + Base64.getEncoder().encodeToString(pwhash)
                    )
                    val salt = Base64.getDecoder().decode(saltstr)
                    System.out.println(
                        " --salt: SALT from db IN BYTES right before AUTH new= "
                                + Base64.getEncoder().encodeToString(salt)
                    )

                    if (RegAct.authenticate(login_password.text.toString(), pwhash, salt).equals(
                            false
                        )
                    ) {
                        System.out.println(" --authenticate= false")
                        Toast.makeText(this, "Sign In Failed", Toast.LENGTH_LONG).show()
                    } else {
                        System.out.println(" --authenticate= true")
                        UserInfo.mobile = login_number.text.toString()
                        val i0 = Intent(this, HomeAct::class.java)
                        var uname =
                            ""//DELETE THISSSSSS DELETE THISSSSSS DELETE THISSSSSSS DELETE THISSSSSS
                        if (login_number.text.toString().equals("0791192226")) uname = ", Costa"
                        if (login_number.text.toString().equals("0790526861")) uname = ", Smecta"

                        Toast.makeText(
                            this,
                            "Authentication succesful, \n Welcome" + uname + "!",
                            Toast.LENGTH_LONG
                        ).show()
                        startActivity(i0)
                    }
                }, 5000)
                }
            }

            signup_butt.setOnClickListener {
                val i = Intent(this, RegAct::class.java)
                startActivity(i) }

            contact_us_butt.setOnClickListener {
                if (contact_layout.isVisible) contact_layout.visibility = View.INVISIBLE
                else contact_layout.visibility = View.VISIBLE }

            twitter_butt.setOnClickListener {
                val openURL = Intent(android.content.Intent.ACTION_VIEW)
                openURL.data = Uri.parse("https://twitter.com/costaalfed/")
                startActivity(openURL) }

            facebook_butt.setOnClickListener {
                val openURL = Intent(android.content.Intent.ACTION_VIEW)
                openURL.data = Uri.parse("https://www.facebook.com/costalfed/")
                startActivity(openURL) }

            instagram_butt.setOnClickListener {
                val openURL = Intent(android.content.Intent.ACTION_VIEW)
                openURL.data = Uri.parse("https://www.instagram.com/costa_alfed/")
                startActivity(openURL) }

            reddit_butt.setOnClickListener {
                val openURL = Intent(android.content.Intent.ACTION_VIEW)
                openURL.data = Uri.parse("https://www.reddit.com/user/Costaalfed")
                startActivity(openURL) }

            linkdin_butt.setOnClickListener {
                val openURL = Intent(android.content.Intent.ACTION_VIEW)
                openURL.data = Uri.parse("https://www.linkedin.com/in/costa-alfed-a18047176/")
                startActivity(openURL) }

            whatsapp_butt.setOnClickListener {
                val openURL = Intent(android.content.Intent.ACTION_VIEW)
                openURL.data = Uri.parse("https://wa.me/213791192226")
                startActivity(openURL) }

            email_butt.setOnClickListener {
                val openURL = Intent(android.content.Intent.ACTION_VIEW)
                openURL.data = Uri.parse("mailto:alfedcosta@gmail.com")
                startActivity(openURL) }
        }
}

