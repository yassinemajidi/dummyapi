package com.example.dummyapi.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dummyapi.*
import com.example.dummyapi.model.main.Data
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_layout.view.*


class MyAdapter: RecyclerView.Adapter<MyAdapter.MyViewHolder>(){
    // init
    private var mContext: Context? = null
    private var mLayout = R.layout.row_layout
    private var myList = emptyList<Data>()

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(mLayout, parent, false))
    }

    override fun getItemCount(): Int {
        return myList.size
    }


    // update each recyclerView element
    override fun onBindViewHolder(holder: MyViewHolder, i: Int) {
        // title
        holder.itemView.authName.text = String.format("%s. %s %s", myList[i].owner.title, myList[i].owner.firstName, myList[i].owner.lastName )
        // Date
        holder.itemView.Date.text = myList[i].publishDate
        // avatar
        Picasso.get().load(myList[i].owner.picture).into(holder.itemView.avatar);
        // image
        Picasso.get().load(myList[i].image).into(holder.itemView.mainImage);
        // content
        holder.itemView.content.text = myList[i].text
        // tags
        holder.itemView.tag1.text = myList[i].tags[0]
        holder.itemView.tag2.text = myList[i].tags[1]
        holder.itemView.tag3.text = myList[i].tags[2]
        // go to PostActivity
        holder.itemView.setOnClickListener {
            val postId = myList[i].id
            val intent = Intent(mContext, PostActivity::class.java)
            intent.putExtra("post_id", postId)
            mContext?.startActivity(intent)
        }
        // go to DeletePostActivity
        holder.itemView.setOnLongClickListener{
            val postId = myList[i].id
            val intent = Intent(mContext, DeletePostActivity::class.java)
            intent.putExtra("post_id", postId)
            mContext?.startActivity(intent)
            return@setOnLongClickListener true
        }
        // go to posts by tag
        holder.itemView.tag1.setOnClickListener {
            val tag = it.tag1.text
            val intent = Intent(mContext, TagActivity::class.java)
            intent.putExtra("tag", tag)
            mContext?.startActivity(intent)
        }
        holder.itemView.tag2.setOnClickListener {
            val tag = it.tag2.text
            val intent = Intent(mContext, TagActivity::class.java)
            intent.putExtra("tag", tag)
            mContext?.startActivity(intent)
        }
        holder.itemView.tag3.setOnClickListener {
            val tag = it.tag3.text
            val intent = Intent(mContext, TagActivity::class.java)
            intent.putExtra("tag", tag)
            mContext?.startActivity(intent)
        }
    }

    // set and update Data in recycleView
    fun setData(context: Context,layout: Int, newList: List<Data>){
        myList = newList
        mContext = context
        mLayout = layout
        notifyDataSetChanged()
    }


}