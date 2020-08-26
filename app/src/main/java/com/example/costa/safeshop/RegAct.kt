package com.example.costa.safeshop


import android.annotation.SuppressLint
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
import android.os.AsyncTask
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.core.text.isDigitsOnly
import com.galleonsoft.safetynetrecaptcha.ApiPostHelper
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.safetynet.SafetyNet
import com.google.android.gms.safetynet.SafetyNetApi
import org.json.JSONException
import org.json.JSONObject
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
import java.util.regex.Matcher
import java.util.regex.Pattern

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;




class RegAct : AppCompatActivity() {

    val recaptchaSiteKey="6LcckMMZAAAAAJe4DNipgurwbJ7eB1ViPVEKfQ5j"

    companion object {
        val TAG = "reCAPTCHA_Activity"
        val SAFETY_NET_API_KEY = "6LcckMMZAAAAAJe4DNipgurwbJ7eB1ViPVEKfQ5j"
        val VERIFY_ON_API_URL_SERVER = "http://" + R.string.local_ip + "/SalesWeb/safetynet-recaptcha-verfication.php"

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
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)// make the application icon clickable to add back button

        reg_butt.setOnClickListener {
            CheckSafetynetreCAPTCHA()
            var saltbae = generateSalt()
            System.out.println("--saltbae= " + Base64.getEncoder().encodeToString(saltbae))

            var saltbaestr = Base64.getEncoder().encodeToString(saltbae)
            System.out.println("--saltbaestr= " + saltbaestr)

            var encpass = getEncryptedPassword(reg_password.text.toString(), saltbae)
            System.out.println("--encpass= " + Base64.getEncoder().encodeToString(encpass))

            var encpassstr = Base64.getEncoder().encodeToString(encpass)
            System.out.println("--encpassstr= " + encpassstr)

            fun isValidPassword(password:String):Boolean {
                var pattern: Pattern
                var matcher: Matcher

                var PASSWORD_PATTERN:String = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$"

                pattern = Pattern.compile(PASSWORD_PATTERN)
                matcher = pattern.matcher(password)

                return matcher.matches()
            }

            if (reg_name.text.toString().isBlank()
                || reg_address.text.toString().isBlank()
                || !captcha_checkbox.isChecked
            )
                Toast.makeText(this, "Please Fill Out All Fields..", Toast.LENGTH_LONG).show()
            else {
                if ((!reg_number.text.toString().isDigitsOnly())
                    || reg_number.text.toString().length != 10
                    || (!reg_number.text.toString().startsWith("07")
                            && !reg_number.text.toString().startsWith("05")
                            && !reg_number.text.toString().startsWith("06")))
                    Toast.makeText(this, "Invalid Phone Number", Toast.LENGTH_LONG).show()
                else {
                    if (reg_password.text.toString().length < 8
                        || !isValidPassword(reg_password.text.toString()))
                        Toast.makeText(this, "Password must contain: \n" +
                                "8 Character or more, \n" +
                                "at least one digit (0-9), \n" +
                                "at least one UPPER case letter (A-Z), \n" +
                                "at least one lower case letter (a-z), \n" +
                                "at least one special character( [@#%^&+=] ), \n" +
                                "NO White Spaces, \n", Toast.LENGTH_LONG).show()
                    else {
                        if (reg_password.text.toString().equals(password_confirm.text.toString())) {
                            val ipad: String = getString(R.string.local_ip)
                            val url =
                                "http://" + ipad + "/SalesWeb/add_user.php?number="+
                                        reg_number.text.toString() + "&pwhash=" +encpassstr +
                                        "&salt=" + saltbaestr + "&name=" + reg_name.text.toString()+
                                        "&address=" + reg_address.text.toString()
                            val rq: RequestQueue = Volley.newRequestQueue(this)
                            val sr =
                                StringRequest(Request.Method.GET, url, Response.Listener { response ->
                                    if (response.equals("0")) {
                                        Toast.makeText(this,
                                            "Number already used!", Toast.LENGTH_LONG).show()
                                        System.out.println("--response= 0")
                                    } //DELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETE
                                    else {
                                        System.out.println("--response= 1") //DELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETEDELETE
                                        UserInfo.mobile = reg_number.text.toString()
                                        val i = Intent(this, MainActivity::class.java)
                                        startActivity(i)
                                    }
                                }, Response.ErrorListener { error ->
                                    Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
                                    System.out.println("--errormessage= " + error.message)
                                })
                            rq.add(sr)
                        } else {
                            System.out.println("Passwords don't match!")
                            Toast.makeText(this, "Passwords don't match!", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    fun CheckSafetynetreCAPTCHA() {
        // Showing SafetyNet reCAPTCHA dialog
        SafetyNet.getClient(this).verifyWithRecaptcha(recaptchaSiteKey)
            .addOnSuccessListener(this) { response ->
                Log.d(TAG, "onSuccess")

                if (!response.tokenResult.isEmpty()) {

                    // Received reCaptcha token and This token still needs to be validated on
                    // the server using the SECRET key api.
                    verifyTokenFromServer(response.tokenResult).execute()
                    Log.i(TAG, "onSuccess: " + response.tokenResult)
                }
            }
            .addOnFailureListener(this) { e ->
                if (e is ApiException) {
                    Log.d(TAG, "SafetyNet Error: " + CommonStatusCodes.getStatusCodeString(e.statusCode))
                } else {
                    Log.d(TAG, "Unknown SafetyNet error: " + e.message)
                }
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {finish()
                return true}}
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }
    /**
     * Verifying the captcha token on the server
     * Server makes call to https://www.google.com/recaptcha/api/siteverify
     * with SECRET Key and SafetyNet token.
     */
    @SuppressLint("StaticFieldLeak")
    class verifyTokenFromServer(internal var sToken: String) : AsyncTask<String, String, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg args: String): String {

            // object to hold the information, which is sent to the server
            val hashMap = HashMap<String, String>()
            hashMap["recaptcha-response"] = sToken
            // Optional params you can use like this
            // hashMap.put("feedback-message", msg)

            // Send the recaptcha response token and receive a Result in return
            return ApiPostHelper.SendParams(VERIFY_ON_API_URL_SERVER, hashMap)
        }

        override fun onPostExecute(result: String?) {

            if (result == null)
                return

            Log.i("onPost::: ", result)
            try {
                val jsonObject = JSONObject(result)
                val success = jsonObject.getBoolean("success")
                val message = jsonObject.getString("message")

                if (success) {
                    // reCaptcha verified successfully.
                    //linearLayoutForm.visibility = View.GONE
                    //Tv_Message.visibility = View.VISIBLE
                } else {
                    //Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                }

            } catch (e: JSONException) {
                e.printStackTrace()
                Log.i("Error: ", e.message)
            }

        }
    }

}
