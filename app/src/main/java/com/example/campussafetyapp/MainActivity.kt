package com.example.campussafetyapp

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RemoteViews.RemoteCollectionItems
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.imageview.ShapeableImageView
import java.util.Locale

class MainActivity : AppCompatActivity() {

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Initialize RecyclerView
        val contactListRecyclerView: RecyclerView = findViewById(R.id.contact_list)
        contactListRecyclerView.layoutManager = LinearLayoutManager(this)

        // Prepare list of contacts
        val contactList: MutableList<ContactDTO> = ArrayList()
        val contactsCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)

        // Read the contacts
        if (contactsCursor != null) {
            while (contactsCursor.moveToNext()) {
                val name = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val contact = ContactDTO()
                contact.name = name

                // Corrected photo URI access
                val photoUri = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))
                if (photoUri != null) {
                    contact.photoUri = photoUri
                    contact.image = MediaStore.Images.Media.getBitmap(contentResolver, Uri.parse(photoUri))
                }
                contactList.add(contact)
            }
            contactsCursor.close()
            if (contactList.isNotEmpty()) {
                contactList.sortBy { it.name.lowercase(Locale.ROOT) }
            }

            // Set the adapter for RecyclerView
            val contactAdapter = ContactAdapter(contactList, this)
            contactListRecyclerView.adapter = contactAdapter
        }

        // Set window insets listener for edge-to-edge UI
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Contact Adapter for RecyclerView
    class ContactAdapter(items: List<ContactDTO>, ctx: Context) : RecyclerView.Adapter<ContactAdapter.ViewHolder>() {
        private val list: List<ContactDTO> = items
        private var context: Context = ctx

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.name.text = list[position].name
            if (list[position].image != null) {
                holder.profile.setImageBitmap(list[position].image)  // Corrected method call
            } else {
                // If no image, set the default image from the drawable
                holder.profile.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.default_profile_image))
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
            return ViewHolder(view)
        }

        class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            val name: TextView = v.findViewById(R.id.tv_name)
            val profile = v.findViewById<ShapeableImageView>(R.id.iv_profile) // ImageView for profile picture
        }
    }
}
