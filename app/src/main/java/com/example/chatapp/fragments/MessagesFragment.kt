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
import com.example.chatapp.model.Chatlist
import com.example.chatapp.model.User
import com.example.chatapp.notifications.Token
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceId

class MessagesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    lateinit var friendsAdapter: FriendsAdapter
    val mUsers = ArrayList<User>()

    private lateinit var fuser: FirebaseUser
    private lateinit var reference: DatabaseReference

    val usersList = ArrayList<Chatlist>()

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

        reference = FirebaseDatabase.getInstance().getReference("Chatlist").child(fuser.uid)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                usersList.clear()
                for (snapshot: DataSnapshot in p0.children) {
                    val chatlist = snapshot.getValue(Chatlist::class.java)
                    usersList.add(chatlist!!)
                }

                chatList()
            }
        })

        updateToken(FirebaseInstanceId.getInstance().token.toString())

    }

    private fun updateToken(token: String) {
        val reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Tokens")
        val token1 = Token(token)
        reference.child(fuser.uid).setValue(token1)
    }

    private fun chatList() {
        reference = FirebaseDatabase.getInstance().getReference("Users")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                mUsers.clear()
                for (snapshot: DataSnapshot in p0.children) {
                    val user = snapshot.getValue(User::class.java)
                    for (chatlist: Chatlist in usersList) {
                        if (user!!.id == chatlist.id) {
                            mUsers.add(user)
                        }
                    }
                }
                friendsAdapter = FriendsAdapter(requireContext(), mUsers, true)
                recyclerView.adapter = friendsAdapter
            }
        })
    }
}
