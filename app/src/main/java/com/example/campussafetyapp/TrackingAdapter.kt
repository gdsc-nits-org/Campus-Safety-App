package com.example.campussafetyapp

import Contact
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView

class TrackingAdapter(
    private val selectedContacts: MutableList<Contact>,
    private val onRemoveContact: (Contact) -> Unit
) : RecyclerView.Adapter<TrackingAdapter.TrackingViewHolder>() {

    inner class TrackingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivProfile: ShapeableImageView = itemView.findViewById(R.id.ivProfile)
        private val tvContactName: TextView = itemView.findViewById(R.id.tvContactName)
//        private val tvContactNumber: TextView = itemView.findViewById(R.id.tvContactNumber)
        private val btnRemoveContact: ImageView = itemView.findViewById(R.id.btnRemoveContact)

        fun bind(contact: Contact) {
            tvContactName.text = contact.name
//            tvContactNumber.text = contact.number

            if (!contact.profilePicUri.isNullOrEmpty()) {
                ivProfile.setImageURI(Uri.parse(contact.profilePicUri))
            } else {
                ivProfile.setImageResource(R.drawable.default_profile)
            }

            btnRemoveContact.setOnClickListener {
                onRemoveContact(contact)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_selected_contact, parent, false)
        return TrackingViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackingViewHolder, position: Int) {
        holder.bind(selectedContacts[position])
    }

    override fun getItemCount(): Int {
        return selectedContacts.size
    }

    fun updateContacts(newContacts: List<Contact>) {
        selectedContacts.clear()
        selectedContacts.addAll(newContacts)
        notifyDataSetChanged()
    }
}
