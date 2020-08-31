package com.example.costa.safeshop

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_blurb.*
import kotlinx.android.synthetic.main.item_row.view.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class BlurbFragment : DialogFragment() {
    private var mContext: Context? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onDetach() {
        super.onDetach()
        mContext = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var v=inflater!!.inflate(R.layout.fragment_blurb, container, false)

        var iv=v.findViewById<ImageView>(R.id.qrcode)
        val qrtip=v.findViewById<ImageView>(R.id.qr_tip)
        var butt=v.findViewById<Button>(R.id.ok_butt_blurb)

        var ipad:String=getString(R.string.local_ip)
        var url:String="http://"+ipad+"/SalesWeb/get_blurb.php?title="+UserInfo.bookTitle4blurb
        var rq: RequestQueue = Volley.newRequestQueue(activity)
        var jar= JsonArrayRequest(Request.Method.GET,url,null, Response.Listener { response ->

            Book_Title_blurb.text=response.getJSONObject(0).getString("title")
            Book_Author_blurb.text=response.getJSONObject(0).getString("author")
            var web:String="http://"+ipad+"/SalesWeb/images/"+response.getJSONObject(0).getString("photo")
            web=web.replace(" ","%20")
            Picasso.get().load(web).into(Book_Photo_Blurb)
            Book_Summary.text=response.getJSONObject(0).getString("blurb")
            if (UserInfo.bookTitle4blurb.equals("Insurgent")) Picasso.get().load("http://"+ipad+"/SalesWeb/images/qr26.png")
        }, Response.ErrorListener { error ->
            Toast.makeText(activity,error.message, Toast.LENGTH_LONG).show()
        })
        rq.add(jar)

        iv.setOnClickListener{
            val genbitmap:Bitmap=generateBimap(iv)
            val saveimage:String=saveImage(genbitmap)
            Toast.makeText(activity,"QR Code Saved on your device storage!",Toast.LENGTH_LONG).show()
        }

        butt.setOnClickListener { activity?.finish()}

        qrtip.setOnClickListener { Toast.makeText(activity,
            "   Click on the QR Code \n to save it on your device.",Toast.LENGTH_LONG).show() }

        return v
    }
    fun onBackPressed() {fragmentManager!!.popBackStack()}

    private fun generateBimap(view:View):Bitmap {
        // Create a bitmap with same dimensions as view
        val bitmap:Bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888)
        // Create a canvas using bitmap
        val canvas:Canvas = Canvas(bitmap)
        // We need to check if view as backround image.
        /*val baground:Drawable = view.getBackground()
        if (baground != null) baground.draw(canvas) else {canvas.drawColor(Color.WHITE)}*/
        // draw the view on the canvas
        view.draw(canvas)
        // final bitmap
        return bitmap
    }

    private fun saveImage(bitmap:Bitmap): String {
        // Create Destination folder in external storage. This will require EXTERNAL STORAGE permission
        val imgDir:String  = Environment.getExternalStorageDirectory().toString() + "/DzBookStore"
        // Generate a random file name for image
        val imageName:String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date()) + ".jpeg"
        val localFile= File(imgDir, imageName)
        localFile.renameTo(localFile)
        val path:String = "file://" + imgDir

        try {
            val fos= FileOutputStream(localFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
            getActivity()?.sendBroadcast((Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(File(path)))))
        } catch (e:Exception)  {e.printStackTrace()}
        // Local path to be shown to User to tell where the Image has been saved.
        return path
    }

    private fun share(bitmap:Bitmap) {
        try {
            if(mContext != null) {
                val shareUri: Uri = getImageUri(requireContext(), bitmap)
                val localIntent: Intent = Intent()
                localIntent.setAction("android.intent.action.SEND")
                localIntent.putExtra("android.intent.extra.STREAM", shareUri)
                localIntent.setType("image/jpg")
                localIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                this.startActivity(localIntent)
            }
        } catch (e:Exception) {
            Toast.makeText(activity, "" + e, Toast.LENGTH_LONG).show()
        }
    }

    private fun getImageUri(context:Context, bitmap:Bitmap):Uri {
        var bytes:ByteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        var path:String= MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null)
        return Uri.parse(path)
    }

}
