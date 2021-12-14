package com.example.chatonkotlinfirebase.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatonkotlinfirebase.R
import com.example.chatonkotlinfirebase.adapter.FriendAdapter
import com.example.chatonkotlinfirebase.data.Friend
import com.example.chatonkotlinfirebase.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private lateinit var button : Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter:FriendAdapter
    private lateinit var auth: FirebaseAuth

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        //todo тут будет список пользователей
/////////////////проверка кнопки
        auth = Firebase.auth

        val database = Firebase.database
        val myRef = database.getReference("message")

        recyclerView = root.findViewById(R.id.recyclerView2)

        adapter = FriendAdapter()
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        changeListener(myRef)
/////
        button = root.findViewById(R.id.buttonButton)
        button.setOnClickListener{
        }

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    ///
//    private fun initRecycleView(){
//        adapter = FriendAdapter()
//        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
//
//        recyclerView.adapter = adapter
//    }

    private fun changeListener(dRef: DatabaseReference) {
        dRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<Friend>()
                for (s in snapshot.children) {
                    val friend = s.getValue(Friend::class.java)
                    if (friend!= null) list.add(friend)
                }

                adapter.submitList(list)
                //перелистывание вниз
                //todo разобраться как перелистывать , когда выезжает клавиатура
                //todo  при нуле ловит ошибку
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}