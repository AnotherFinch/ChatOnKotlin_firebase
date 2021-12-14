package com.example.chatonkotlinfirebase.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chatonkotlinfirebase.data.Friend
import com.example.chatonkotlinfirebase.data.User
import com.example.chatonkotlinfirebase.databinding.UserListItemBinding
import com.google.firebase.auth.FirebaseAuth

//класс для работы с recycleView и заполнением его item (итемами)
//todo разобраться до конца как этот класс работает
class FriendAdapter : ListAdapter<Friend, FriendAdapter.ItemHolder>(ItemComparator()) {

    class ItemHolder (private val binding: UserListItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(friend: Friend) = with(binding){
            message.text = friend.message
            userName.text = friend.name
            val userName : String = friend?.name.toString()
            val userRealName : String = FirebaseAuth.getInstance().currentUser?.displayName.toString()
            if (userRealName!=userName){
                //todo найти подходящий цвет или понять как передавать значение int
                //todo понять как убрать текст вправо
                //todo сейчас при переносе слов на другу строку меняется отступ у сообщения
                binding.listLinearLayout.setBackgroundColor(0x79FF0266)

            }
        }

        companion object{
            fun create(parent : ViewGroup) : ItemHolder {
                return ItemHolder(UserListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
        }
    }
    //сравнивает итемы и если одинковые , то не дополняет новые
    class ItemComparator : DiffUtil.ItemCallback<Friend>() {
        override fun areItemsTheSame(oldItem: Friend, newItem: Friend): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Friend, newItem: Friend): Boolean {
            return oldItem == newItem
        }
    }
    //каждый раз создает итем на новой позиции
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(getItem(position))
    }


}