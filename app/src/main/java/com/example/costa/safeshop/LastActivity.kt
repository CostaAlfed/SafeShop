package com.example.costa.safeshop

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_confirm.*
import kotlinx.android.synthetic.main.activity_last.*
import kotlin.system.exitProcess

class LastActivity : AppCompatActivity() {
    val positiveButtonClick = { dialog: DialogInterface, which: Int ->
        finishAffinity()
        exitProcess(0)
    }
    val positiveButtonClick2 = { dialog: DialogInterface, which: Int ->
        val int= Intent(this,MainActivity::class.java)
        startActivity(int)
    }
    val negativeButtonClick = { dialog: DialogInterface, which: Int -> dialog.cancel()}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_last)
        logout_butt.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            with(builder)
            {
                setTitle("Log Out?")
                setMessage("Are you sure you want to Log Out? \n Press Yes to go back to the Log In page.")
                setPositiveButton("Yes", DialogInterface.OnClickListener(function = positiveButtonClick2))
                setNegativeButton("Cancel", negativeButtonClick)
                show()
            }
        }

        close_app_butt.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            with(builder)
            {
                setTitle("Close App?")
                setMessage("Are you sure you want to close this app?")
                setPositiveButton("Yes", DialogInterface.OnClickListener(function = positiveButtonClick))
                setNegativeButton("Cancel", negativeButtonClick)
                show()
            }
        }
    }
}
