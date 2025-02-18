package com.example.campussafetyapp

import Contact
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SelectedContactAdapter(
    private val selectedContacts: MutableList<Contact>,
    private val onRemoveClick: (Contact) -> Unit
) : RecyclerView.Adapter<SelectedContactAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvContactName: TextView = view.findViewById(R.id.tvSelectedContactName)
        val btnRemove: ImageView = view.findViewById(R.id.btnRemoveSelectedContact)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_deselect, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = selectedContacts[position]
        holder.tvContactName.text = contact.name

        holder.btnRemove.setOnClickListener {
            onRemoveClick(contact)
        }
    }

    override fun getItemCount(): Int = selectedContacts.size

//    fun updateList(newList: List<Contact>) {
//        selectedContacts.clear()
//        selectedContacts.addAll(newList)
//        notifyDataSetChanged() // Ensures UI updates
//    }
fun refreshList(newList: List<Contact>) {
    selectedContacts.clear()
    selectedContacts.addAll(newList)
    notifyDataSetChanged()
}


}
