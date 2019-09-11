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
import com.example.chatapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_friends.view.*

class FriendsFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var friendsAdapter: FriendsAdapter
    val mUsers = ArrayList<User>()

    companion object {
        fun newInstance() : FriendsFragment =
            FriendsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friends, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.recyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)

        readUsers()
    }

    private fun readUsers() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                mUsers.clear()
                for (snapshot: DataSnapshot in p0.children) {
                    val user = snapshot.getValue(User::class.java)

                    if (user!!.id != firebaseUser!!.uid) {
                        mUsers.add(user)
                    }
                }

                friendsAdapter = FriendsAdapter(context!!, mUsers)
                recyclerView.adapter = friendsAdapter
            }
        })
    }
}
