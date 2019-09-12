package com.example.chatapp.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.R
import com.example.chatapp.adapter.FriendsAdapter
import com.example.chatapp.model.Chat
import com.example.chatapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class MessagesFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var friendsAdapter: FriendsAdapter
    val mUsers = ArrayList<User>()

    lateinit var fuser: FirebaseUser
    private lateinit var reference: DatabaseReference

    val usersList = ArrayList<String>()

    companion object {
        fun newInstance() : MessagesFragment =
            MessagesFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_messages, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.messages_recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)

        fuser = FirebaseAuth.getInstance().currentUser!!

        reference = FirebaseDatabase.getInstance().getReference("Chats")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                usersList.clear()

                for (snapshot: DataSnapshot in p0.children) {
                    val chat: Chat = snapshot.getValue(Chat::class.java)!!

                    if (chat.sender == fuser.uid) {
                        usersList.add(chat.receiver)
                    }
                    if (chat.receiver == fuser.uid) {
                        usersList.add(chat.sender)
                    }
                }

                readChats()
            }
        })
    }

    private fun readChats() {
        reference = FirebaseDatabase.getInstance().getReference("Users")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                mUsers.clear()

                for (snapshot: DataSnapshot in p0.children) {
                    val user: User = snapshot.getValue(User::class.java)!!

                    // display 1 user from chats
                    for (id: String in usersList) {
                        if (user.id == id) {
                            if (mUsers.size != 0) {
                                for (user1: User in mUsers) {
                                    if (user.id != user1.id) {
                                        mUsers.add(user)
                                    }
                                }
                            } else {
                                mUsers.add(user)
                            }
                        }
                    }
                }

                friendsAdapter = FriendsAdapter(context!!, mUsers)
                recyclerView.adapter = friendsAdapter
            }
        })
    }
}
