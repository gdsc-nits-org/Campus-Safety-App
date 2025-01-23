package com.example.campussafetyapp.EmergencyContacts

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.campussafetyapp.R
import com.example.campussafetyapp.RoomDB.Contact
import com.example.campussafetyapp.databinding.ItemPhoneContactBinding


class PhoneContactsAdapter(
    private val onContactSelected: (Contact) -> Unit,
    private val onContactDeselected: (Contact) -> Unit // Callback for when a contact is deselected
) : RecyclerView.Adapter<PhoneContactsAdapter.ViewHolder>() {
    private var contacts = listOf<Contact>()
    private val selectedContacts = mutableListOf<Contact>() // Track selected contacts

    fun submitList(contactList: List<Contact>) {
        contacts = contactList
        notifyDataSetChanged()
    }
//    fun submitList(contactList: List<Contact>) {
//        // Remove duplicates based on phone number (or other unique identifier)
//        contacts = contactList.distinctBy { it.phoneNumber } // Or use `it.name` or both `it.name to it.phoneNumber`
//        notifyDataSetChanged()
//    }  //no need for this to show only unique nos, since already taken care in

    fun getSelectedContacts(): List<Contact> {
        return selectedContacts
    }

    fun deselectContact(contact: Contact) {
        if (selectedContacts.contains(contact)) {
            selectedContacts.remove(contact)
            notifyItemChanged(contacts.indexOf(contact)) // Refresh the UI for this contact
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPhoneContactBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding, onContactSelected,onContactDeselected)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(contacts[position])
    }

    override fun getItemCount() = contacts.size

    inner class ViewHolder(
        private val binding: ItemPhoneContactBinding,
        private val onContactSelected: (Contact) -> Unit,
        private val onContactDeselected: (Contact) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(contact: Contact) {
            binding.tvPhoneContactName.text = contact.name
            //binding.tvPhoneContactNumber.text = contact.phoneNumber

            // Load profile image or set default
            if (contact.photoUri != null) {
                binding.ivProfile.setImageURI(Uri.parse(contact.photoUri))
            } else {
                binding.ivProfile.setImageResource(R.drawable.default_profile) // Use your default profile image here
            }

            // Update the UI to reflect selection state
            val isSelected = selectedContacts.contains(contact)
            binding.root.setBackgroundColor(
                if (isSelected) 0xFFE0E0E0.toInt() // Grey background if selected
                else 0xFFF5F0FF.toInt() // White background if not selected
            )

            // Handle selection toggle
            binding.root.setOnClickListener {
                if (isSelected) {
                    selectedContacts.remove(contact)
                    onContactDeselected(contact) // Notify horizontal RecyclerView
                } else {
                    selectedContacts.add(contact)
                    onContactSelected(contact) // Notify horizontal RecyclerView
                }
                // Notify adapter to refresh UI
                notifyItemChanged(adapterPosition)
            }

        }
    }
}
