package com.example.campussafetyapp.EmergencyContacts

import androidx.recyclerview.widget.RecyclerView
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.campussafetyapp.RoomDB.AppDatabase
import com.example.campussafetyapp.R
import com.example.campussafetyapp.RoomDB.Contact
import com.example.campussafetyapp.databinding.ActivityAddContactsBinding



class AddContactsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddContactsBinding
    private lateinit var adapter: PhoneContactsAdapter
    private lateinit var database: AppDatabase

    // for selected contacts( to see and remove if we want below search box and above contact list)
    private val selectedContacts = mutableListOf<Contact>() // To hold the selected contacts
    private lateinit var selectedContactsAdapter: SelectedContactsAdapter

    private val REQUEST_CODE_PERMISSION = 100
    private val contactsList = mutableListOf<Contact>() // Main list of contacts

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // Initialize ViewBinding
        binding = ActivityAddContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize database and adapter
        database = AppDatabase.getDatabase(this)

        // Initialize adapters
        adapter = PhoneContactsAdapter(
            onContactSelected = { contact ->
                // Update the horizontal RecyclerView when a contact is selected
                selectedContactsAdapter.submitList(adapter.getSelectedContacts())
            },
            onContactDeselected = { contact ->
                // Update the horizontal RecyclerView when a contact is deselected
                selectedContactsAdapter.submitList(adapter.getSelectedContacts())
            }
        )
        // Initialize the horizontal RecyclerView for selected contacts
        selectedContactsAdapter = SelectedContactsAdapter { contact ->
            // When a contact is removed from the horizontal RecyclerView
            adapter.deselectContact(contact) // Deselect it in the main list
            selectedContactsAdapter.submitList(adapter.getSelectedContacts()) // Update the horizontal RecyclerView
        }

        // Set up RecyclerView with ViewBinding
        binding.rvPhoneContacts.layoutManager = LinearLayoutManager(this)
        binding.rvPhoneContacts.adapter = adapter

        binding.rvSelectedContacts.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvSelectedContacts.adapter = selectedContactsAdapter

        val exitText = findViewById<TextView>(R.id.goBack)
        exitText.setOnClickListener {
//            // Redirect to EmergencyContactsActivity and refresh the data
//            val intent = Intent(this, EmergencyContactsActivity::class.java)
//            intent.putExtra("refresh_contacts", true)
//            startActivity(intent)
            finish()
        }
        // Check for permissions and load contacts
        if (checkPermission()) {
            loadPhoneContacts()
        } else {
            requestPermission()
        }

        setupAlphabetScroller(
            scroller = binding.alphabetScroller,
            visibleLetterView = binding.visibleLetter,
            scrollBar = binding.scrollBar,
            recyclerView = binding.rvPhoneContacts,
            contactList = contactsList
        )


        // Search functionality
        binding.searchViewContacts.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterContacts(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterContacts(newText)
                return true
            }
        })

        binding.btnAddSelectedContacts.setOnClickListener {
            val selectedContacts = adapter.getSelectedContacts()
            if (selectedContacts.isNotEmpty()) {
                // Filter out contacts that already exist in the database
                val existingContacts = database.contactDao().getAllContacts()
                val existingPhoneNumbers = existingContacts.map { it.phoneNumber }.toSet()

                val newContacts = selectedContacts.filter { it.phoneNumber !in existingPhoneNumbers }

                if (newContacts.isNotEmpty()) {
                    database.contactDao().insertContact(newContacts)
                    Toast.makeText(this, "Contacts added successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "No new contacts to add!", Toast.LENGTH_SHORT).show()
                }

//                // Redirect to EmergencyContactsActivity and refresh the data
//                val intent = Intent(this, EmergencyContactsActivity::class.java)
//                intent.putExtra("refresh_contacts", true)
//                startActivity(intent)

                // Use setResult to notify the parent activity
                setResult(RESULT_OK) // RESULT_OK indicates success
                finish()
            } else {
                Toast.makeText(this, "No contacts selected!", Toast.LENGTH_SHORT).show()
            }
        }

    }


    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this, android.Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(android.Manifest.permission.READ_CONTACTS), REQUEST_CODE_PERMISSION
        )
    }

    @SuppressLint("Range")
    private fun loadPhoneContacts() {
        //val contactsList = mutableListOf<Contact>()
        val tempContacts = mutableListOf<Contact>()
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null, null, null, null
        )
        cursor?.use {
            while (it.moveToNext()) {
                val name = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val phone = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val photoUri = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))
                tempContacts.add(Contact(name = name, phoneNumber = phone, photoUri = photoUri))
            }
            // Remove duplicates based on phone number
            contactsList.clear()
            contactsList.addAll(tempContacts.distinctBy { it.phoneNumber })
        }
        // Sort the contacts alphabetically by name
        contactsList.sortBy { it.name }

        // Submit the sorted list to the adapter
        adapter.submitList(contactsList)
    }

    private fun filterContacts(query: String?) {
        val filteredList = if (query.isNullOrEmpty()) {
            contactsList // Show the full list if no query is entered
        } else {
            contactsList.filter { it.name.contains(query, ignoreCase = true) }
        }
        adapter.submitList(filteredList)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadPhoneContacts()
        } else {
            Toast.makeText(this, "Permission denied to read your contacts", Toast.LENGTH_SHORT).show()
        }
    }

    // Update to add the contact to the horizontal RecyclerView
    private fun addSelectedContact(contact: Contact) {
        if (selectedContacts.contains(contact)) {
            Toast.makeText(this, "${contact.name} is already selected!", Toast.LENGTH_SHORT).show()
            return
        }
        selectedContacts.add(contact)

        // Update the horizontal RecyclerView
        selectedContactsAdapter.notifyItemInserted(selectedContacts.size - 1)
    }

    // Remove a selected contact from the horizontal RecyclerView
    private fun removeSelectedContact(contact: Contact) {
        val position = selectedContacts.indexOf(contact)
        if (position != -1) {
            selectedContacts.removeAt(position)
            selectedContactsAdapter.notifyItemRemoved(position)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupAlphabetScroller(
        scroller: LinearLayout,
        visibleLetterView: TextView,
        scrollBar: View,
        recyclerView: RecyclerView,
        contactList: List<Contact>
    ) {
        val letters = ('A'..'Z').toList()

        // Populate the scroller with TextViews for each letter
        letters.forEach { letter ->
            val letterView = TextView(this).apply {
                text = letter.toString()
                textSize = 14f
                setPadding(8, 4, 8, 4)
                setTextColor(ContextCompat.getColor(this@AddContactsActivity, android.R.color.black))
            }
            scroller.addView(letterView)
        }

        val layoutManager = recyclerView.layoutManager as LinearLayoutManager

        // Listen to RecyclerView scroll events
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // Calculate the scrollbar position
                val totalItems = layoutManager.itemCount
                val visibleItems = layoutManager.childCount
                val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
                val totalScrollableItems = totalItems - visibleItems

                // Adjust the scrollbar position
                val scrollRatio = if (totalScrollableItems > 0) {
                    firstVisiblePosition.toFloat() / totalScrollableItems
                } else {
                    0f
                }
                val containerHeight = scroller.height
                val scrollBarHeight = scrollBar.height
                val scrollBarY = (scrollRatio * (containerHeight - scrollBarHeight)).coerceIn(0f, (containerHeight - scrollBarHeight).toFloat())
                scrollBar.translationY = scrollBarY
            }
        })

        // Touch listener for Alphabet Scroller
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

                        // Scroll RecyclerView to the correct position
                        val position = contactList.indexOfFirst {
                            it.name.startsWith(selectedLetter.toString(), true)
                        }
                        if (position != -1) {
                            recyclerView.scrollToPosition(position)
                        }

                        // Adjust scrollbar position
                        val containerHeight = scroller.height
                        val scrollBarHeight = scrollBar.height
                        val scrollBarY = (letterIndex.toFloat() / letters.size) * (containerHeight - scrollBarHeight)
                        scrollBar.translationY = scrollBarY.coerceIn(0f, (containerHeight - scrollBarHeight).toFloat())
                    }
                }
                MotionEvent.ACTION_UP -> {
                    visibleLetterView.visibility = View.GONE
                }
            }
            true
        }
    }

}
