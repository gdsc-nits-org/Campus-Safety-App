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
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campussafetyapp.MyContactPage.ContactAdapter
import com.google.android.material.imageview.ShapeableImageView
import java.util.Locale

class MyContactPage : AppCompatActivity() {
    private lateinit var contactAdapter: ContactAdapter

    @SuppressLint("Range", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_my_contact_page)
        val exitText = findViewById<TextView>(R.id.backText)

        // Handle TextView click to exit app
        exitText.setOnClickListener {
            finish() // Close the app when the text is clicked
        }

        val contactListRecyclerView: RecyclerView = findViewById(R.id.contact_list)
        contactListRecyclerView.layoutManager = LinearLayoutManager(this)

        // Prepare list of contacts
        val contactList: MutableList<ContactDTO> = mutableListOf()
        val contactsCursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null, null, null, null
        )

        // Read the contacts
        contactsCursor?.let {
            while (it.moveToNext()) {
                val name = it.getString(
                    it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                )
                val contact = ContactDTO()
                contact.name = name

                val photoUri = it.getString(
                    it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI)
                )
                if (photoUri != null) {
                    contact.photoUri = photoUri
                    contact.image = MediaStore.Images.Media.getBitmap(contentResolver, Uri.parse(photoUri))
                }
                contactList.add(contact)
            }
            it.close()

            if (contactList.isNotEmpty()) {
                contactList.sortBy { contact -> contact.name.lowercase(Locale.ROOT) }
            }
        }

        // Add headers to contact list
        val contactListWithHeaders = addHeaders(contactList)

        // Initialize adapter and set it to RecyclerView
        contactAdapter = ContactAdapter(contactListWithHeaders, this)
        contactListRecyclerView.adapter = contactAdapter

        // Set up SearchView for filtering
        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                contactAdapter.filter.filter(newText)
                return true
            }
        })

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun addHeaders(contactList: List<ContactDTO>): List<ContactDTO> {
        val contactListWithHeaders = mutableListOf<ContactDTO>()
        var lastChar: Char? = null

        for (contact in contactList) {
            val firstChar = contact.name.firstOrNull()?.uppercaseChar()
            if (firstChar != lastChar) {
                val header = ContactDTO(name = firstChar.toString(), isHeader = true)
                contactListWithHeaders.add(header)
                lastChar = firstChar
            }
            contactListWithHeaders.add(contact)
        }
        return contactListWithHeaders
    }

    class ContactAdapter(private val items: List<ContactDTO>, private val context: Context) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

        private val originalList: List<ContactDTO> = items
        private var filteredList: MutableList<ContactDTO> = items.toMutableList()
        private val ITEM_TYPE_CONTACT = 0
        private val ITEM_TYPE_HEADER = 1

        override fun getItemCount(): Int {
            return filteredList.size
        }

        override fun getItemViewType(position: Int): Int {
            return if (filteredList[position].isHeader) ITEM_TYPE_HEADER else ITEM_TYPE_CONTACT
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (holder is ContactViewHolder) {
                holder.name.text = filteredList[position].name
                if (filteredList[position].image != null) {
                    holder.profile.setImageBitmap(filteredList[position].image)
                } else {
                    holder.profile.setImageDrawable(
                        ContextCompat.getDrawable(context, R.drawable.default_profile_image)
                    )
                }
            } else if (holder is HeaderViewHolder) {
                holder.header.text = filteredList[position].name
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return if (viewType == ITEM_TYPE_CONTACT) {
                val view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
                ContactViewHolder(view)
            } else {
                val view = LayoutInflater.from(context).inflate(R.layout.header_item, parent, false)
                HeaderViewHolder(view)
            }
        }

        class ContactViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            val name: TextView = v.findViewById(R.id.tv_name)
            val profile: ShapeableImageView = v.findViewById(R.id.iv_profile)
        }

        class HeaderViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            val header: TextView = v.findViewById(R.id.tv_header)
        }

        override fun getFilter(): Filter {
            return object : Filter() {
                override fun performFiltering(constraint: CharSequence?): FilterResults {
                    val query = constraint?.toString()?.lowercase(Locale.ROOT) ?: ""
                    val result = FilterResults()

                    // Perform filtering on contacts only, not on headers
                    result.values = if (query.isEmpty()) {
                        originalList
                    } else {
                        originalList.filter {
                            !it.isHeader && it.name.lowercase(Locale.ROOT).contains(query)
                        }
                    }
                    return result
                }

                @Suppress("UNCHECKED_CAST")
                override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                    filteredList = (results?.values as List<ContactDTO>).toMutableList()
                    notifyDataSetChanged()
                }
            }
        }
    }
}