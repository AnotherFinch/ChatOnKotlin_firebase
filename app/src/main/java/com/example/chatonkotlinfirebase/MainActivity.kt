package com.example.chatonkotlinfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chatonkotlinfirebase.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        val database = Firebase.database
        val myRef = database.getReference("message")
        //отвечает за отправку текса в fireBase
        binding.buttonSend.setOnClickListener{
            myRef.setValue(binding.edMessage.text.toString())
        }
        //регистрация слушателя changeListener в firebase на узле message
        changeListener(myRef)

    }

    //добавление текста в основной textView
    private fun changeListener(dRef : DatabaseReference){
        dRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.apply {
                    //todo сделать в append имя по заданному (вместо Login)
                    messageTextView.append("\n ")
                    messageTextView.append("Login: ${snapshot.value.toString()}")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}