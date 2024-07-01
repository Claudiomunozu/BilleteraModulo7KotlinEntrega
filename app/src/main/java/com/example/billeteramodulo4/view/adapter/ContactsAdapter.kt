package com.example.billeteramodulo4.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.billeteramodulo4.databinding.ItemContactsBinding
import com.example.billeteramodulo4.model.local.entities.Contacts

class ContactsAdapter(private val contactsList: List<Contacts>) :
    RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val binding =
            ItemContactsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        val contactsitem: Contacts = contactsList[position]
        holder.bind(contactsitem)
    }

    override fun getItemCount(): Int = contactsList.size


    inner class ContactsViewHolder(private val binding: ItemContactsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(contacts: Contacts) {

            binding.ivfotoContacts.setImageResource(contacts.photo)
            binding.tvnombreContacts.text = contacts.name
            binding.tvdateTransaction.text = contacts.date
            binding.tvvalueTransaction.text = contacts.import
        }
    }
}
