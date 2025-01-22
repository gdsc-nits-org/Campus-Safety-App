package com.example.campussafetyapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.campussafetyapp.databinding.ItemDeselectBinding


class SelectedContactsAdapter(
    //private val selectedContacts: List<Contact>,
    private val onContactRemoved: (Contact) -> Unit
) : RecyclerView.Adapter<SelectedContactsAdapter.ViewHolder>() {

    private var selectedContacts = listOf<Contact>()

    // Submit a new list of selected contacts and refresh the adapter
    fun submitList(contactList: List<Contact>) {
        selectedContacts = contactList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDeselectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(selectedContacts[position])
    }

    override fun getItemCount() = selectedContacts.size

    inner class ViewHolder(private val binding: ItemDeselectBinding
                          // private val onContactRemoved: (Contact) -> Unit
        ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(contact: Contact) {
            binding.tvSelectedContactName.text = contact.name

            // Handle the "cross" button to remove the contact
            binding.btnRemoveSelectedContact.setOnClickListener {
                onContactRemoved(contact)
            }
        }
    }
}
