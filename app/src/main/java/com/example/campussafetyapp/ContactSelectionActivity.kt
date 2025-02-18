package com.example.campussafetyapp

import Contact
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.campussafetyapp.databinding.ActivityContactSelectionBinding
import android.Manifest
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView


class ContactSelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactSelectionBinding
    private lateinit var contactsAdapter: ContactsAdapter

    private val contactsList = mutableListOf<Contact>()  //

    private lateinit var selectedContactAdapter: SelectedContactAdapter
    private val selectedContacts = mutableListOf<Contact>()


    private val contactPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            loadContacts() // Load contacts if permission is granted
        } else {
            Toast.makeText(this, "Permission Denied. Cannot access contacts.", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerViews()

        binding.rvContacts.adapter = contactsAdapter
        binding.rvContacts.layoutManager = LinearLayoutManager(this)

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

        val exitText = findViewById<TextView>(R.id.goBack)
        exitText.setOnClickListener {
            finish()
        }

        // Request permission if not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            contactPermissionRequest.launch(Manifest.permission.READ_CONTACTS)
        } else {
            loadContacts() // Load contacts if already granted
        }

        setupAlphabetScroller(
            scroller = binding.alphabetScroller,
            visibleLetterView = binding.visibleLetter,
            scrollBar = binding.scrollBar,
            recyclerView = binding.rvContacts,
            contactList = contactsList
        )

        binding.btnConfirmSelection.setOnClickListener {
            val selectedContacts = contactsAdapter.getSelectedContacts() // ✅ Fetch full contact details
            val resultIntent = Intent()
            resultIntent.putParcelableArrayListExtra("selectedContacts", ArrayList(selectedContacts)) // ✅ Send complete data
            setResult(RESULT_OK, resultIntent)
            finish()
        }


    }

    private fun setupRecyclerViews() {
        // Setup horizontal RecyclerView for selected contacts
        selectedContactAdapter = SelectedContactAdapter(selectedContacts) { contact ->
            removeContact(contact)
        }
        binding.rvSelectedContacts.apply {
            layoutManager = LinearLayoutManager(this@ContactSelectionActivity, RecyclerView.HORIZONTAL, false)
            adapter = selectedContactAdapter
        }

        // Setup vertical RecyclerView for full contact list
        contactsAdapter = ContactsAdapter(contactsList, { contact, isSelected ->
            if (isSelected) addContact(contact) else removeContact(contact)
        })
        binding.rvContacts.apply {
            layoutManager = LinearLayoutManager(this@ContactSelectionActivity)
            adapter = contactsAdapter
        }
    }
    private fun addContact(contact: Contact) {
        if (!selectedContacts.contains(contact)) {
            selectedContacts.add(contact)
            selectedContactAdapter.notifyItemInserted(selectedContacts.size - 1)
            binding.rvSelectedContacts.visibility = View.VISIBLE  // Ensure visibility
        }
    }

    private fun removeContact(contact: Contact) {
        val position = selectedContacts.indexOf(contact)
        if (position != -1) {
//            selectedContacts.removeAt(position)
//            selectedContactAdapter.notifyItemRemoved(position)
//            if (selectedContacts.isEmpty()) {
//                binding.rvSelectedContacts.visibility = View.GONE  // Hide if empty
//            }
            selectedContacts.removeAt(position)
            selectedContactAdapter.notifyItemRemoved(position)

            // Update main list adapter to unhighlight the deselected contact
            val indexInMainList = contactsList.indexOf(contact)
            if (indexInMainList != -1) {
                contactsAdapter.deselectContact(contact) // ✅ Unhighlight contact in main list
            }

            if (selectedContacts.isEmpty()) {
                binding.rvSelectedContacts.visibility = View.GONE  // Hide if empty
            }
        }
    }



    private fun loadContacts() {
        contactsList.clear()
        contactsList.addAll(fetchContacts())
        contactsAdapter.submitList(contactsList) // ✅ Properly update the adapter with full contact list
    }


    private fun filterContacts(query: String?) {
        val filteredList = if (query.isNullOrEmpty()) {
            contactsList // Show all contacts if search is empty
        } else {
            contactsList.filter { it.name.contains(query, ignoreCase = true) }
        }
        contactsAdapter.submitList(filteredList) // ✅ Properly update the adapter
    }


    @SuppressLint("Range")
    private fun fetchContacts(): List<Contact> {
        val contactsSet = mutableSetOf<Contact>()
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        cursor?.use {
            while (it.moveToNext()) {
                val name = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val number = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                // ✅ Fetch contact's profile photo URI
                val photoUri = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))

                contactsSet.add(Contact(name, number, photoUri))
            }
        }
        return contactsSet.sortedBy { it.name.lowercase() } // ✅ Sort alphabetically
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
                setTextColor(ContextCompat.getColor(this@ContactSelectionActivity, android.R.color.black))
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