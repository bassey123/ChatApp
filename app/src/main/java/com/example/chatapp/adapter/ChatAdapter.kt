package com.example.chatapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapp.R
import com.example.chatapp.model.Chat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.chat_item_left.view.*

class ChatAdapter(private val mContext: Context, private val mChat: List<Chat>, private val imageurl: String) :
    RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    private val MSG_TYPE_LEFT = 0
    private val MSG_TYPE_RIGHT = 1
    private lateinit var fuser: FirebaseUser

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == MSG_TYPE_RIGHT) {
            val v =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.chat_item_right, parent, false)
            return ViewHolder(v)
        } else {
            val v =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.chat_item_left, parent, false)
            return ViewHolder(v)
        }
    }

    override fun getItemCount(): Int {
        return mChat.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val chat: Chat = mChat[position]
        holder.showMessage.text = chat.message

        if (imageurl == "default") {
            holder.image.setImageResource(R.mipmap.ic_launcher)
        } else {
            Glide.with(mContext)
                .load(imageurl)
                .into(holder.image)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val showMessage: TextView = itemView.show_message
        val image: CircleImageView = itemView.profile_image
    }

    override fun getItemViewType(position: Int): Int {
        fuser = FirebaseAuth.getInstance().currentUser!!
        if (mChat[position].sender == fuser.uid) {
            return MSG_TYPE_RIGHT
        } else {
            return MSG_TYPE_LEFT
        }
    }
}