package com.example.costa.safeshop

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_row.view.*

class BookAdapter (var context:Context, var list:ArrayList<Book>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var v:View=LayoutInflater.from(context).inflate(R.layout.item_row,parent,false)
        return BookHolder(v)
    }

    override fun getItemCount(): Int {return list.size}

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as BookHolder)
            .bind(list[position].title,
                list[position].author,
                list[position].price,
                list[position].photo,
                list[position].id)}

    class BookHolder(itemView: View):RecyclerView.ViewHolder(itemView)
    {
        fun bind(t:String,a:String,p:Int,ph:String,book_id:Int)
        {
            itemView.book_title.text=t
            itemView.book_author.text="by: "+a
            itemView.book_price.text=p.toString()+" DZD"
            var ipad:String=itemView.book_price.context.getString(R.string.local_ip)
            var web:String="http://"+ipad+"/SalesWeb/images/"+ph
            web=web.replace(" ","%20")
            Picasso.get().load(web).into(itemView.book_photo)

            itemView.book_add_icon.setOnClickListener {
                UserInfo.bookId=book_id
                var popup=QtyFragment()
                var manager=(itemView.context as FragmentActivity).supportFragmentManager
                popup.show(manager,"Qty")}

            itemView.book_photo.setOnClickListener {
                UserInfo.bookTitle4blurb=t
                //Toast.makeText(this,"Normally this shows POPUP", Toast.LENGTH_LONG).show()
                var popup2=BlurbFragment()
                var manager2=(itemView.context as FragmentActivity).supportFragmentManager
                popup2.show(manager2,"Blurb") }
        }
    }
}