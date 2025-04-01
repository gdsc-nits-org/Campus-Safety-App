package com.example.campussafetyapp.EmergencyContacts

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.campussafetyapp.R
import com.example.campussafetyapp.RoomDB.Contact
import com.example.campussafetyapp.databinding.ItemContactInfoBinding
import com.example.campussafetyapp.databinding.ItemEmergencyContactBinding


class EmergencyContactsAdapter(private val onContactDelete: (Contact) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var contacts = listOf<Contact>()
    private var expandedPosition: Int = RecyclerView.NO_POSITION

    // Define view types for collapsed and expanded states
    private val VIEW_TYPE_COLLAPSED = 0
    private val VIEW_TYPE_EXPANDED = 1

    fun submitList(contactList: List<Contact>) {
        contacts = contactList
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == expandedPosition) VIEW_TYPE_EXPANDED else VIEW_TYPE_COLLAPSED
    }

//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val binding = ItemEmergencyContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return ViewHolder(binding, onContactDelete)
//    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_EXPANDED) {
            val binding = ItemContactInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ExpandedViewHolder(binding)
        } else {
            val binding = ItemEmergencyContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            CollapsedViewHolder(binding)
        }
    }

//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(contacts[position])
//    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CollapsedViewHolder) {
            holder.bind(contacts[position])
        } else if (holder is ExpandedViewHolder) {
            holder.bind(contacts[position])
        }
    }

    override fun getItemCount() = contacts.size

    inner class CollapsedViewHolder(
        private val binding: ItemEmergencyContactBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(contact: Contact) {
            binding.tvContactName.text = contact.name

            // Profile Image
            if (contact.photoUri != null) {
                binding.ivProfile.setImageURI(Uri.parse(contact.photoUri))
            } else {
                binding.ivProfile.setImageResource(R.drawable.default_profile)
            }

            // Toggle expanded view on click
            binding.root.setOnClickListener {
                val previousExpandedPosition = expandedPosition
                expandedPosition = if (expandedPosition == adapterPosition) {
                    RecyclerView.NO_POSITION
                } else {
                    adapterPosition
                }
                notifyItemChanged(previousExpandedPosition)
                notifyItemChanged(expandedPosition)
            }
        }
    }

    //below functions when we go back to Emergency contact activity, so to deselect any selected contact
    fun clearExpandedState() {
        val previousExpandedPosition = expandedPosition
        expandedPosition = RecyclerView.NO_POSITION
        notifyItemChanged(previousExpandedPosition) // Notify adapter to refresh the expanded item
    }
    fun clearContacts() {
        contacts = emptyList()
        notifyDataSetChanged()
    }


    inner class ExpandedViewHolder(
        private val binding: ItemContactInfoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(contact: Contact) {
            binding.tvContactName.text = contact.name
            binding.tvContactNumber.text = contact.phoneNumber

            // Profile Image
            if (contact.photoUri != null) {
                binding.ivProfile.setImageURI(Uri.parse(contact.photoUri))
            } else {
                binding.ivProfile.setImageResource(R.drawable.default_profile)
            }

            // Call button
            binding.btnCall.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:${contact.phoneNumber}")
                }
                binding.root.context.startActivity(intent)
            }

            // Remove button
            binding.btnRemove.setOnClickListener {
                onContactDelete(contact)
                expandedPosition = RecyclerView.NO_POSITION
                notifyItemRemoved(adapterPosition)
            }

            // Collapse the expanded view on click
            binding.root.setOnClickListener {
                val previousExpandedPosition = expandedPosition
                expandedPosition = RecyclerView.NO_POSITION
                notifyItemChanged(previousExpandedPosition)
                notifyItemChanged(adapterPosition)
            }
        }
    }
//    class ViewHolder(
//        private val binding: ItemEmergencyContactBinding,
//        private val onContactDelete: (Contact) -> Unit
//    ) : RecyclerView.ViewHolder(binding.root) {
//
//        fun bind(contact: Contact) {
//            binding.tvContactName.text = contact.name
//            binding.tvContactNumber.text = contact.phoneNumber
//
//            // Load profile image or set default
//            if (contact.photoUri != null) {
//                binding.ivProfile.setImageURI(Uri.parse(contact.photoUri))
//            } else {
//                binding.ivProfile.setImageResource(R.drawable.default_profile) // Use your default profile image here
//            }
//
//            // Set up the delete button click listener
//            binding.btnDeleteContact.setOnClickListener {
//                onContactDelete(contact)
//            }
//        }
//    }
}

