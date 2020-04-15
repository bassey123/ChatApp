package com.example.chatapp.fragments


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.R
import com.example.chatapp.adapter.UsersAdapter
import com.example.chatapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_users.*
import kotlinx.android.synthetic.main.fragment_users.view.*

class UsersFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var usersAdapter: UsersAdapter
    val mUsers = ArrayList<User>()

    companion object {
        fun newInstance() : UsersFragment =
            UsersFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.recyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)

        readUsers()

        search_friends.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchFriends(s.toString().toLowerCase())
            }
        })
    }

    private fun searchFriends(s: String) {
        val fuser = FirebaseAuth.getInstance().currentUser
        val query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("search")
            .startAt(s)
            .endAt(s+"\uf8ff")

        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                mUsers.clear()
                for (snapshot: DataSnapshot in p0.children) {
                    val user = snapshot.getValue(User::class.java)

                    if (user!!.id != fuser!!.uid) {
                        mUsers.add(user)
                    }
                }

                usersAdapter = UsersAdapter(requireContext(), mUsers, false)
                recyclerView.adapter = usersAdapter
            }
        })
    }

    private fun readUsers() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (search_friends.text.toString() == "") {
                    mUsers.clear()
                    for (snapshot: DataSnapshot in p0.children) {
                        val user = snapshot.getValue(User::class.java)

                        if (user!!.id != firebaseUser!!.uid) {
                            mUsers.add(user)
                        }
                    }

                    usersAdapter = UsersAdapter(requireContext(), mUsers, false)
                    recyclerView.adapter = usersAdapter
                }
            }
        })
    }
}
