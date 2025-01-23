package com.example.campussafetyapp.EmergencyContacts

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campussafetyapp.R
import com.example.campussafetyapp.RoomDB.ContactDTO
import com.google.android.material.imageview.ShapeableImageView
import java.util.Locale

class MyContactActivity : AppCompatActivity() {
    private lateinit var contactAdapter: ContactAdapter
    private lateinit var visibleLetterView: TextView
    private lateinit var alphabetScroller: View

    companion object {
        private const val PERMISSION_REQUEST_READ_CONTACTS = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_contact_page)

        val exitText = findViewById<TextView>(R.id.backText)
        exitText.setOnClickListener {
            finish()
        }

        // Check and request contacts permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestContactsPermission()
        } else {
            setupContacts()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun requestContactsPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
            AlertDialog.Builder(this)
                .setTitle("Permission Required")
                .setMessage("This app needs access to your contacts to display them.")
                .setPositiveButton("OK") { _, _ ->
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.READ_CONTACTS),
                        PERMISSION_REQUEST_READ_CONTACTS
                    )
                }
                .setNegativeButton("Cancel", null)
                .create()
                .show()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CONTACTS),
                PERMISSION_REQUEST_READ_CONTACTS
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_READ_CONTACTS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupContacts()
            } else {
                Toast.makeText(this, "Permission to read contacts was denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupContacts() {
        val contactListRecyclerView: RecyclerView = findViewById(R.id.contact_list)
        contactListRecyclerView.layoutManager = LinearLayoutManager(this)

        val contactList: MutableList<ContactDTO> = getContacts()
        val contactListWithHeaders = addHeaders(contactList)

        contactAdapter = ContactAdapter(contactListWithHeaders, this)
        contactListRecyclerView.adapter = contactAdapter

        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.isIconified = false
        searchView.setOnClickListener {
            searchView.isIconified = false
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                contactAdapter.filter.filter(newText)
                return true
            }
        })

        // Setup Alphabetical Scroller
        alphabetScroller = findViewById(R.id.alphabet_scroller)
        visibleLetterView = findViewById(R.id.visible_letter)

        setupAlphabetScroller(alphabetScroller, contactListRecyclerView, contactListWithHeaders)
    }

    @SuppressLint("Range")
    private fun getContacts(): MutableList<ContactDTO> {
        val contactList: MutableList<ContactDTO> = mutableListOf()
        val contactsCursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null, null, null, null
        )

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
        return contactList
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

    @SuppressLint("ClickableViewAccessibility")
    private fun setupAlphabetScroller(
        scroller: View,
        recyclerView: RecyclerView,
        contactList: List<ContactDTO>
    ) {
        val letters = ('A'..'Z').toList()

        scroller.setOnTouchListener { _, event ->
            val totalHeight = scroller.height
            val letterHeight = totalHeight / letters.size
            val actionY = event.y.toInt()
            val letterIndex = actionY / letterHeight

            when (event.action) {
                MotionEvent.ACTION_MOVE, MotionEvent.ACTION_DOWN -> {
                    if (letterIndex in letters.indices) {
                        val selectedLetter = letters[letterIndex]
                        visibleLetterView.text = selectedLetter.toString()
                        visibleLetterView.visibility = View.VISIBLE

                        // Move the visible letter to follow the touch position
                        visibleLetterView.y = event.rawY - (visibleLetterView.height / 2)

                        // Scroll the RecyclerView to the correct position
                        val position = contactList.indexOfFirst {
                            it.name.startsWith(selectedLetter.toString(), true)
                        }
                        if (position != -1) {
                            recyclerView.scrollToPosition(position)
                        }
                    }
                }
                MotionEvent.ACTION_UP -> {
                    visibleLetterView.visibility = View.GONE
                }
            }
            true
        }
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