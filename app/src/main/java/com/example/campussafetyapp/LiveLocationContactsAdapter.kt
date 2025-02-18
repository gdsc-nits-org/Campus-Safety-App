package com.example.campussafetyapp

import Contact
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView

class ContactsAdapter(
    private val contacts: MutableList<Contact>,
    private val onContactSelected: (Contact, Boolean) -> Unit
) : RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>() {

    private val selectedContacts = mutableSetOf<Contact>()
    private var displayList: MutableList<Contact> = contacts.toMutableList() // ✅ Use mutable list


    inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivProfile: ShapeableImageView = itemView.findViewById(R.id.ivProfile)
        private val tvContactName: TextView = itemView.findViewById(R.id.tvContactName)
//        private val tvContactNumber: TextView = itemView.findViewById(R.id.tvContactNumber)

        fun bind(contact: Contact) {
            tvContactName.text = contact.name
//            tvContactNumber.text = contact.number

            // Load profile image if available, otherwise set default image
            if (!contact.profilePicUri.isNullOrEmpty()) {
                ivProfile.setImageURI(Uri.parse(contact.profilePicUri))
            } else {
                ivProfile.setImageResource(R.drawable.default_profile)
            }

            // Change background color if the contact is selected
//            itemView.setBackgroundColor(
//                if (selectedContacts.contains(contact)) {
//                    Color.LTGRAY
//                } else {
//                    Color.WHITE
//                }
//            )
            itemView.setBackgroundColor(
                if (selectedContacts.contains(contact)) Color.LTGRAY else Color.WHITE
            )

            // Handle item click
            itemView.setOnClickListener {
                if (selectedContacts.contains(contact)) {
                    selectedContacts.remove(contact)
                } else {
                    selectedContacts.add(contact)
                }
                onContactSelected(contact, selectedContacts.contains(contact))
                notifyItemChanged(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(displayList[position])    }

    override fun getItemCount(): Int {
        return displayList.size // ✅ Use filtered list
    }

    fun deselectContact(contact: Contact) {
        if (selectedContacts.contains(contact)) {
            selectedContacts.remove(contact) // Remove from selected list
            notifyItemChanged(contacts.indexOf(contact)) // Refresh that item
        }
    }

    fun submitList(newList: List<Contact>) {
        displayList = newList.toMutableList() // ✅ Convert to mutable list
        notifyDataSetChanged()
    }

    fun getSelectedContacts(): List<Contact> {
        return selectedContacts.toList() // ✅ Return the full contact object
    }



    fun updateContacts(newContacts: List<Contact>) {
        contacts.clear()
        contacts.addAll(newContacts)
        notifyDataSetChanged() // ✅ Notify RecyclerView of the updated data
    }

}