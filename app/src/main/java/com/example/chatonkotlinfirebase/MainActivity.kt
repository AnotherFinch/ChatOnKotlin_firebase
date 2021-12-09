package com.example.chatonkotlinfirebase

import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.chatonkotlinfirebase.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        auth = Firebase.auth
        setUpActionBar()

        setContentView(binding.root)
        val database = Firebase.database
        val myRef = database.getReference("message")
        //отвечает за отправку текса в fireBase
        binding.buttonSend.setOnClickListener {
            myRef.setValue(binding.edMessage.text.toString())
        }
        //регистрация слушателя changeListener в firebase на узле message
        changeListener(myRef)

    }

    //инициализация меню экнш бара
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //нажатие на кнопку меню sign_out
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.sign_out){
            auth.signOut()
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    //добавление текста в основной textView
    private fun changeListener(dRef: DatabaseReference) {
        dRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.apply {
                    //todo сделать в append имя по заданному (вместо Login),
                    // а также отправку одинаковых сообщений(сейчас обязательно должны отличаться)
                    messageTextView.append("\n ")
                    messageTextView.append(auth.currentUser?.displayName + ": ${snapshot.value.toString()}")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    //определение эшнбара (меню в верхней части, которое может иметь иконку, кнопки наза, выход и прочее
    private fun setUpActionBar() {
        val actionBar = supportActionBar
        // инициализируется новый поток, чтобы прогрузка(конвертация картинки) не занимала время основного потока
        // картинка лого пользователя достается из гуглсервиса и уже конвертируется
        // все элементы должны быть на основном потоке
        Thread {
            // ссылка на картинку из авторизации
            val bitmap = Picasso.get().load(auth.currentUser?.photoUrl).get()
            val drawableIcon = BitmapDrawable(resources, bitmap)
            //запуск на основном потоке
            runOnUiThread{
                actionBar?.setDisplayHomeAsUpEnabled(true)
                actionBar?.setHomeAsUpIndicator(drawableIcon)
                actionBar?.title = auth.currentUser?.displayName
            }
        }.start()

    }

}