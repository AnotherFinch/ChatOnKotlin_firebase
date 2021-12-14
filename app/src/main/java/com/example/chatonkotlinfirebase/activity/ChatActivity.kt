package com.example.chatonkotlinfirebase.activity

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatonkotlinfirebase.R
import com.example.chatonkotlinfirebase.data.User
import com.example.chatonkotlinfirebase.adapter.UserAdapter
import com.example.chatonkotlinfirebase.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class ChatActivity : AppCompatActivity() {
    lateinit var binding: ActivityChatBinding
    lateinit var auth: FirebaseAuth
    lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        auth = Firebase.auth
        setUpActionBar()
        setContentView(binding.root)
        val database = Firebase.database
        val myRef = database.getReference("message")
        //отвечает за отправку тектса в fireBase
        binding.buttonSend.setOnClickListener {
            myRef.child(myRef.push().key ?: "Отсебятина")
                .setValue(User(auth.currentUser?.displayName, binding.edMessage.text.toString()))
        }
        changeListener(myRef)
        initRecycleView()
    }

    private fun initRecycleView() = with(binding) {
        adapter = UserAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this@ChatActivity)
        recyclerView.adapter = adapter
    }


    //инициализация меню экнш бара
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //нажатие на кнопку меню sign_out
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.sign_out) {
            auth.signOut()
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    //добавление текста в основной textView
    private fun changeListener(dRef: DatabaseReference) {
        dRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<User>()
                for (s in snapshot.children) {
                    val user = s.getValue(User::class.java)
                    if (user != null) list.add(user)
                }
                adapter.submitList(list)
                //перелистывание вниз
                //todo разобраться как перелистывать , когда выезжает клавиатура
                //todo  при нуле ловит ошибку
                if (list.size != 0){
                    binding.recyclerView.smoothScrollToPosition(
                        binding.recyclerView
                            .adapter?.itemCount!!.toInt() - 1
                    )
                }
                hideMyKeyboard()
                deleteMessageText()
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    //прячет клавиатуру после нажатия
    private fun hideMyKeyboard(){
        val view = this.currentFocus
        if (view != null) {
            val hideMy = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            hideMy.hideSoftInputFromWindow(view.windowToken, 0)
        } else {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        }
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
            println("////")
            println(auth.currentUser?.photoUrl)
            val drawableIcon = BitmapDrawable(resources, bitmap)
            //запуск на основном потоке
            runOnUiThread {
                actionBar?.setDisplayHomeAsUpEnabled(true)
               actionBar?.setHomeAsUpIndicator(drawableIcon)
                actionBar?.title = auth.currentUser?.displayName
            }
        }.start()

    }
    //функция удаления текста после нажатия
    private fun deleteMessageText(){
        binding.edMessage.setText("")
    }
}