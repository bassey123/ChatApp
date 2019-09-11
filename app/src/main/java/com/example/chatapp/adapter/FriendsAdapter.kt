package com.example.chatapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapp.ChatsActivity
import com.example.chatapp.R
import com.example.chatapp.model.User
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.friends_list_item.view.*

class FriendsAdapter(private val mContext: Context, private val mUsers: List<User>) :
    RecyclerView.Adapter<FriendsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.friends_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return mUsers.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = mUsers[position]
        holder.username.text = user.userName
        if (user.imageURL == "default") {
            holder.image.setImageResource(R.mipmap.ic_launcher)
        } else {
            Glide.with(mContext)
                .load(user.imageURL)
                .into(holder.image)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(mContext, ChatsActivity::class.java)
            intent.putExtra("userid", user.id)
            mContext.startActivity(intent)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val username: TextView = itemView.username
        val image: CircleImageView = itemView.profile_image
    }

}