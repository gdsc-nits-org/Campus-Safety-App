package com.example.campussafetyapp.EmergencyContacts

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campussafetyapp.RoomDB.AppDatabase
import com.example.campussafetyapp.R
import com.example.campussafetyapp.RoomDB.Contact
import com.example.campussafetyapp.databinding.ActivityEmergencyContactsBinding


class EmergencyContactsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEmergencyContactsBinding
    private lateinit var adapter: EmergencyContactsAdapter
    private lateinit var database: AppDatabase
    private lateinit var visibleLetterView: TextView
    private lateinit var alphabetScroller: View

    private val contactsList = mutableListOf<Contact>() // Main list of contacts

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmergencyContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getDatabase(this)
        adapter = EmergencyContactsAdapter { contact ->
            // When delete icon is clicked, call the delete function
            deleteContact(contact)
        }

        binding.rvEmergencyContacts.layoutManager = LinearLayoutManager(this)
        binding.rvEmergencyContacts.adapter = adapter

        //Set recycler View
        setupRecyclerView()
        // Fetch contacts from the database
        loadEmergencyContacts()



        val exitText = findViewById<TextView>(R.id.goBack)
        exitText.setOnClickListener {
            finish()
        }

//        binding.btnAddContact.setOnClickListener {
//            val intent = Intent(this, AddContactsActivity::class.java)
//            startActivity(intent)
//        }
        // for fresh and updated UI
        binding.btnAddContact.setOnClickListener {
            val intent = Intent(this, AddContactsActivity::class.java)
            addContactsLauncher.launch(intent)
        }
        // Search functionality
        binding.searchEmergencyContacts.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterContacts(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterContacts(newText)
                return true
            }
        })

        setupAlphabetScroller(
            scroller = binding.alphabetScroller,
            visibleLetterView = binding.visibleLetter,
            scrollBar = binding.scrollBar,
            recyclerView = binding.rvEmergencyContacts,
            contactList = contactsList
        )
    }

    private val addContactsLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Refresh the contact list
                refreshContacts()
            }
        }

    private fun refreshContacts() {
        val contacts = database.contactDao().getAllContacts()
        adapter.submitList(contacts)
    }


    private fun setupRecyclerView() {
        adapter = EmergencyContactsAdapter { contact ->
            database.contactDao().deleteContact(contact)
            loadEmergencyContacts()
        }

        binding.rvEmergencyContacts.layoutManager = LinearLayoutManager(this)
        binding.rvEmergencyContacts.adapter = adapter
    }

    private fun loadEmergencyContacts() {
        val contacts = database.contactDao().getAllContacts()
        contactsList.clear()
        contactsList.addAll(contacts)
//        // Sort the contacts alphabetically by name
//        val sortedContacts = contacts.sortedBy { it.name }
//        // Submit the sorted list to the adapter
//        adapter.submitList(sortedContacts)
//        adapter.submitList(contacts)   //since in Dao we've defined to select in alphabetically order

        // Submit the full list to the adapter
        adapter.submitList(contactsList.toList())

    }

    private fun filterContacts(query: String?) {
        val filteredList = if (query.isNullOrEmpty()) {
            contactsList // Show the full list if no query is entered
        } else {
            contactsList.filter { it.name.contains(query, ignoreCase = true) }
        }
        adapter.submitList(filteredList)
    }

    private fun deleteContact(contact: Contact) {
        // Delete the contact from the database
        Thread {
            database.contactDao().deleteContact(contact) // Assuming deleteContact() method exists in your DAO
            runOnUiThread {
                // Refresh the contact list after deletion
                loadEmergencyContacts()
            }
        }.start()
    }

    override fun onResume() {
        super.onResume()

        // Clear the SearchView text
        binding.searchEmergencyContacts.setQuery("", false)
        binding.searchEmergencyContacts.clearFocus() // Remove focus from the search box

        // Clear adapter state
        adapter.clearExpandedState()
        adapter.clearContacts()

        // Reload the contact list from the database
        loadEmergencyContacts() // Fetch and display contacts

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
                setTextColor(ContextCompat.getColor(this@EmergencyContactsActivity, android.R.color.black))
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
