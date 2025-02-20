package com.example.campussafetyapp

import ResourceAdapter
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.campussafetyapp.databinding.ActivityResourceBinding

class ResourceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResourceBinding
    private lateinit var adapter: ResourceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResourceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val branch = resources.getStringArray(R.array.branches)
        val branchAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, branch)
        binding.branchBox.setAdapter(branchAdapter)

        val semester = resources.getStringArray(R.array.semesters)
        val semesterAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, semester)
        binding.semesterBox.setAdapter(semesterAdapter)

        // RecyclerView Setup
        adapter = ResourceAdapter { link -> openResource(link) }
        binding.resourceRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.resourceRecyclerView.adapter = adapter

        // Fetch resources on selection change
        binding.branchBox.setOnItemClickListener { _, _, _, _ -> updateResourceList() }
        binding.semesterBox.setOnItemClickListener { _, _, _, _ -> updateResourceList() }
    }

    private fun updateResourceList() {
        val branch = binding.branchBox.text.toString()
        val semester = binding.semesterBox.text.toString()

        if (branch.isNotEmpty() && semester.isNotEmpty()) {
            val resourcesList = getResourcesForSelection(branch, semester)
            adapter.submitList(resourcesList)
        }
    }

    private fun getResourcesForSelection(branch: String, semester: String): List<ResourceItem> {

        if(semester == "First Semester"){
            return listOf(
                ResourceItem("CH 101", "Chemistry", "https://example.com/subject"),
                ResourceItem("MA 101", "Mathematics 1", "https://example.com/subject"),
                ResourceItem("CS 101", "Introduction to Programming", "https://example.com/subject"),
                ResourceItem("EC 101", "Basic Electronics", "https://example.com/subject"),
                ResourceItem("CE 102", "Environmental Science & Engineering", "https://example.com/subject"),
                ResourceItem("CH 111", "Chemistry Lab", "https://example.com/subject"),
                ResourceItem("CS 111", "Programming Lab", "https://example.com/subject"),
                ResourceItem("EC 111", "Basic Electronics Lab", "https://example.com/subject"),
                ResourceItem("ME 111", "Workshop Practice", "https://example.com/subject")
            )
        }
        else if(semester == "Second Semester"){
            return listOf(
                ResourceItem("PH 101", "Physics", "https://example.com/subject"),
                ResourceItem("MA 102", "Mathematics 2", "https://example.com/subject"),
                ResourceItem("ME 101", "Engineering Mechanics", "https://example.com/subject"),
                ResourceItem("EE 101", "Basic Electrical Engineering", "https://example.com/subject"),
                ResourceItem("HS 101", "Communicative English", "https://example.com/subject"),
                ResourceItem("CE 101", "Engineering Graphics & Design", "https://example.com/subject"),
                ResourceItem("PH 111", "Physics Lab", "https://example.com/subject"),
                ResourceItem("EE 111", "Basic Electrical Engineering Lab", "https://example.com/subject"),
                ResourceItem("HS 111", "Language Lab", "https://example.com/subject")
            )
        }
        // Civil Engineering
        else if (branch == "Civil Engineering" && semester == "Third Semester") {
            return listOf(
                ResourceItem("CE 201", "Mechanics of Materials", "https://example.com/subject"),
                ResourceItem("MA 201", "Mathematics III", "https://example.com/subject"),
                ResourceItem("CE 202", "Civil Engineering Material, Testing and Evaluation", "https://example.com/subject"),
                ResourceItem("CE 203", "Introduction to Geo Sciences", "https://example.com/subject"),
                ResourceItem("CE 204", "Surveying & Geomatics", "https://example.com/subject"),
                ResourceItem("CE 205", "Fluid Mechanics", "https://example.com/subject"),
                ResourceItem("CE 211", "Surveying & Geomatics Laboratory", "https://example.com/subject"),
                ResourceItem("CE 212", "Civil Engineering Materials, Testing and Evaluation Laboratory", "https://example.com/subject"),
                ResourceItem("CE 213", "Civil Engineering Drawing Laboratory", "https://example.com/subject")
            )
        }
        else if (branch == "Civil Engineering" && semester == "Fourth Semester") {
            return listOf(
                ResourceItem("CE 206", "Structural Analysis -I", "https://example.com/subject"),
                ResourceItem("CE 207", "Hydraulics", "https://example.com/subject"),
                ResourceItem("CE 208", "Design of Concrete Structures-I", "https://example.com/subject"),
                ResourceItem("CE 209", "Transportation Engineering", "https://example.com/subject"),
                ResourceItem("CE 210", "Geotechnical Engineering", "https://example.com/subject"),
                ResourceItem("CE 214", "Hydraulics Laboratory", "https://example.com/subject"),
                ResourceItem("CE 215", "Concrete Laboratory", "https://example.com/subject"),
                ResourceItem("CE 216", "Geotechnical Engineering Laboratory", "https://example.com/subject"),
                ResourceItem("CE 217", "Geo Science Laboratory", "https://example.com/subject")
            )
        }
        else if (branch == "Civil Engineering" && semester == "Fifth Semester") {
            return listOf(
                ResourceItem("CE 301", "Design of Concrete Structures -II", "https://example.com/subject"),
                ResourceItem("CE 302", "Foundation Engineering", "https://example.com/subject"),
                ResourceItem("CE 303", "Structural Analysis-II", "https://example.com/subject"),
                ResourceItem("CE 304", "Surface and Ground Water Hydrology", "https://example.com/subject"),
                ResourceItem("CE 305", "Water Supply Engineering", "https://example.com/subject"),
                ResourceItem("CE 311", "Detailing of Civil Engineering Structures", "https://example.com/subject"),
                ResourceItem("CE 312", "Foundation Engineering Laboratory", "https://example.com/subject"),
                ResourceItem("CE 313", "Transportation Engineering Laboratory", "https://example.com/subject"),
                ResourceItem("CE 314", "Water Resources Engineering Laboratory", "https://example.com/subject")
            )
        }
        else if (branch == "Civil Engineering" && semester == "Sixth Semester") {
            return listOf(
                ResourceItem("CE 306", "Civil Engineering Estimation", "https://example.com/subject"),
                ResourceItem("CE 307", "Design of Steel Structures", "https://example.com/subject"),
                ResourceItem("CE 308", "Sewage Treatment And Disposal", "https://example.com/subject"),
                ResourceItem("CE 309", "Structural Analysis-III", "https://example.com/subject"),
                ResourceItem("CE 3XX", "Professional Core Elective- I", "https://example.com/subject"),
                ResourceItem("CE 3XX", "Open Elective- I", "https://example.com/subject"),
                ResourceItem("CE 315", "Environment Engineering Laboratory", "https://example.com/subject"),
                ResourceItem("CE 316", "Computer Aided Design Laboratory", "https://example.com/subject"),
                ResourceItem("CE 317", "Structural Engineering Laboratory", "https://example.com/subject")
            )
        }
        else if (branch == "Civil Engineering" && semester == "Seventh Semester") {
            return listOf(
                ResourceItem("HS XXX", "Engineering Economics / Management Studies", "https://example.com/subject"),
                ResourceItem("CE 401", "Concrete Technology", "https://example.com/subject"),
                ResourceItem("CE 4XX", "Professional Core Elective- II", "https://example.com/subject"),
                ResourceItem("CE 4XX", "Open Elective- II", "https://example.com/subject"),
                ResourceItem("CE 498", "Project I", "https://example.com/subject"),
                ResourceItem("CE 430", "Industrial Training", "https://example.com/subject")
            )
        }
        else if (branch == "Civil Engineering" && semester == "Eighth Semester") {
            return listOf(
                ResourceItem("HS XXX", "Engineering Economics / Management Studies", "https://example.com/subject"),
                ResourceItem("CE 4XX", "Professional Core Elective- III", "https://example.com/subject"),
                ResourceItem("CE 4XX", "Open Elective- III", "https://example.com/subject"),
                ResourceItem("CE 499", "Project II", "https://example.com/subject")
            )
        }
        // Computer Science Engineering
        else if(branch == "Computer Science Engineering" && semester == "Third Semester"){
            return listOf(
                ResourceItem("CS 201", "Data Structure", "https://example.com/subject"),
                ResourceItem("MA 201", "Mathematics 3", "https://example.com/subject"),
                ResourceItem("CS 202", "Discrete Structures", "https://example.com/subject"),
                ResourceItem("EC 221", "Circuits & Switching", "https://example.com/subject"),
                ResourceItem("EE 223", "Microprocessor", "https://example.com/subject"),
                ResourceItem("CS 203", "Data Structure Lab", "https://example.com/subject"),
                ResourceItem("EE 224", "Microprocessor Lab", "https://example.com/subject"),
                ResourceItem("EC 222", "Circuits & Switching Lab", "https://example.com/subject"),
            )
        }
        else if(branch == "Computer Science Engineering" && semester == "Fourth Semester"){
            return listOf(
                ResourceItem("CS 204", "Theory Of Computation", "https://example.com/subject"),
                ResourceItem("CS 205", "Computer Architecture & Organization", "https://example.com/subject"),
                ResourceItem("CS 206", "Algorithms", "https://example.com/subject"),
                ResourceItem("MA 221", "Mathematics 4", "https://example.com/subject"),
                ResourceItem("CS 207", "Signals & Data Communication", "https://example.com/subject"),
                ResourceItem("CS 208", "OOP Lab", "https://example.com/subject"),
                ResourceItem("CS 209", "Algorithms Lab", "https://example.com/subject"),
                ResourceItem("CS 210", "Signals & Data Communication Lab", "https://example.com/subject"),
                ResourceItem("CS 211", "Applied Probability Lab", "https://example.com/subject")
            )
        }
        else if(branch == "Computer Science Engineering" && semester == "Fifth Semester"){
            return listOf(
                ResourceItem("CS 301", "Computer Network", "https://example.com/subject"),
                ResourceItem("CS 302", "Database Management System", "https://example.com/subject"),
                ResourceItem("CS 303", "Operating System", "https://example.com/subject"),
                ResourceItem("CS 304", "Software Engineering", "https://example.com/subject"),
                ResourceItem("CS 305", "Graph Theory", "https://example.com/subject"),
                ResourceItem("CS 311", "Computer Network Lab", "https://example.com/subject"),
                ResourceItem("CS 312", "Database Lab", "https://example.com/subject"),
                ResourceItem("CS 313", "Operating System Lab", "https://example.com/subject"),
                ResourceItem("CS 314", "Software Engineering Lab", "https://example.com/subject")
            )
        }
        else if (branch == "Computer Science Engineering" && semester == "Sixth Semester") {
            return listOf(
                ResourceItem("CS 306", "Principles of Programming Language", "https://example.com/subject"),
                ResourceItem("CS 307", "Compiler Design", "https://example.com/subject"),
                ResourceItem("CS 308", "Graphics & Multimedia", "https://example.com/subject"),
                ResourceItem("CS 33X", "Professional Core Elective I", "https://example.com/subject"),
                ResourceItem("CS 38X", "Open Elective I", "https://example.com/subject"),
                ResourceItem("CS 315", "Object Oriented Design Laboratory", "https://example.com/subject"),
                ResourceItem("CS 316", "Compiler Laboratory", "https://example.com/subject"),
                ResourceItem("CS 317", "Graphics & Multimedia Laboratory", "https://example.com/subject"),
                ResourceItem("CS 32Y", "Professional Core Elective-I Laboratory", "https://example.com/subject")
            )
        }
        else if (branch == "Computer Science Engineering" && semester == "Seventh Semester") {
            return listOf(
                ResourceItem("CS 401", "Artificial Intelligence", "https://example.com/subject"),
                ResourceItem("CS 43X", "Professional Core Elective II", "https://example.com/subject"),
                ResourceItem("CS 48X", "Open Elective II", "https://example.com/subject"),
                ResourceItem("HS 402", "Business Management", "https://example.com/subject"),
                ResourceItem("CS 498", "Project-I", "https://example.com/subject")
            )
        }
        else if (branch == "Computer Science Engineering" && semester == "Eighth Semester") {
            return listOf(
                ResourceItem("HS 401", "Managerial Economics", "https://example.com/subject"),
                ResourceItem("CS 44X", "Professional Core Elective III", "https://example.com/subject"),
                ResourceItem("CS 48X", "Open Elective III", "https://example.com/subject"),
                ResourceItem("CS 499", "Project II", "https://example.com/subject")
            )
        }
        // Electrical Engineering
        else if (branch == "Electrical Engineering" && semester == "Third Semester") {
            return listOf(
                ResourceItem("EE 201", "Signals and Systems", "https://example.com/subject"),
                ResourceItem("MA 201", "Mathematics III", "https://example.com/subject"),
                ResourceItem("EE 202", "Analog Electronics", "https://example.com/subject"),
                ResourceItem("EE 203", "Energy Science and Technology", "https://example.com/subject"),
                ResourceItem("EE 204", "Measuring Instruments and Measurements", "https://example.com/subject"),
                ResourceItem("EE 205", "Electromagnetic Field Theory", "https://example.com/subject"),
                ResourceItem("EE 211", "Programming and Simulation Laboratory", "https://example.com/subject"),
                ResourceItem("EE 212", "Measurement Laboratory", "https://example.com/subject"),
                ResourceItem("EC 226", "Analog Electronics Laboratory", "https://example.com/subject")
            )
        }
        else if (branch == "Electrical Engineering" && semester == "Fourth Semester") {
            return listOf(
                ResourceItem("EE 206", "Electrical Machines -I", "https://example.com/subject"),
                ResourceItem("EE 207", "Power Systems I", "https://example.com/subject"),
                ResourceItem("EE 208", "Digital Electronics", "https://example.com/subject"),
                ResourceItem("EE 209", "Circuit Theory", "https://example.com/subject"),
                ResourceItem("EE 210", "Microprocessors & Microcontrollers", "https://example.com/subject"),
                ResourceItem("CS 221", "Programming & Data Structure", "https://example.com/subject"),
                ResourceItem("EE 213", "Circuit Theory Laboratory", "https://example.com/subject"),
                ResourceItem("EE 214", "Microprocessor & Microcontroller Laboratory", "https://example.com/subject"),
                ResourceItem("EE 215", "Digital Electronics Laboratory", "https://example.com/subject")
            )
        }
        else if (branch == "Electrical Engineering" && semester == "Fifth Semester") {
            return listOf(
                ResourceItem("EE 301", "Control Systems", "https://example.com/subject"),
                ResourceItem("EE 302", "Power Systems II", "https://example.com/subject"),
                ResourceItem("EE 303", "Electrical Machines II", "https://example.com/subject"),
                ResourceItem("EE 304", "Power Electronics", "https://example.com/subject"),
                ResourceItem("EE 305", "Digital Signal Processing", "https://example.com/subject"),
                ResourceItem("EE 311", "Electrical Machine Laboratory-I", "https://example.com/subject"),
                ResourceItem("EE 312", "Power System Laboratory-I", "https://example.com/subject"),
                ResourceItem("EE 313", "Control System Laboratory", "https://example.com/subject"),
                ResourceItem("EE 314", "Signal Processing Laboratory", "https://example.com/subject")
            )
        }
        else if (branch == "Electrical Engineering" && semester == "Sixth Semester") {
            return listOf(
                ResourceItem("EE 306", "Switchgear and Protection", "https://example.com/subject"),
                ResourceItem("EE 307", "Industrial Drives", "https://example.com/subject"),
                ResourceItem("EC 327", "Analog and Digital Communication", "https://example.com/subject"),
                ResourceItem("EE 308", "Modern Control Systems", "https://example.com/subject"),
                ResourceItem("EE 3XX", "Professional Core Elective I", "https://example.com/subject"),
                ResourceItem("EE 3XX", "Open Elective I", "https://example.com/subject"),
                ResourceItem("EE 315", "Electrical Machine Laboratory II", "https://example.com/subject"),
                ResourceItem("EE 316", "Power System Laboratory II", "https://example.com/subject"),
                ResourceItem("EE 317", "Power Electronics and Drives Laboratory", "https://example.com/subject")
            )
        }
        else if (branch == "Electrical Engineering" && semester == "Seventh Semester") {
            return listOf(
                ResourceItem("EE 401", "Instrumentation", "https://example.com/subject"),
                ResourceItem("MS 401", "Business Management", "https://example.com/subject"),
                ResourceItem("EE 4XX", "Professional Core Elective II", "https://example.com/subject"),
                ResourceItem("EE 4XX", "Open Elective II", "https://example.com/subject"),
                ResourceItem("EE 497", "Industrial Training (Minimum 6 weeks)", "https://example.com/subject"),
                ResourceItem("EE 498", "Project I", "https://example.com/subject")
            )
        }
        else if (branch == "Electrical Engineering" && semester == "Eighth Semester") {
            return listOf(
                ResourceItem("EE 4XX", "Professional Core Elective III", "https://example.com/subject"),
                ResourceItem("HS 401", "Managerial Economics", "https://example.com/subject"),
                ResourceItem("EE 4XX", "Open Elective III", "https://example.com/subject"),
                ResourceItem("EE 499", "Project II", "https://example.com/subject")
            )
        }
        // Electronics and Communication Engineering
        else if (branch == "Electronics and Communication Engineering" && semester == "Third Semester") {
            return listOf(
                ResourceItem("EC-201", "Electronic Devices", "https://example.com/subject"),
                ResourceItem("MA-201", "Mathematics III", "https://example.com/subject"),
                ResourceItem("EE-221", "Network Analysis & Synthesis", "https://example.com/subject"),
                ResourceItem("CS-222", "Data Structures and Algorithm", "https://example.com/subject"),
                ResourceItem("EC-202", "Analog Electronic Circuits", "https://example.com/subject"),
                ResourceItem("EC-203", "Signals and Systems", "https://example.com/subject"),
                ResourceItem("EE-222", "Circuit Theory Laboratory", "https://example.com/subject"),
                ResourceItem("EC-211", "Analog Electronic Circuits Laboratory", "https://example.com/subject"),
                ResourceItem("CS-223", "Data Structure and Algorithm Laboratory", "https://example.com/subject")
            )
        }
        else if (branch == "Electronics and Communication Engineering" && semester == "Fourth Semester") {
            return listOf(
                ResourceItem("EC-204", "Digital Electronic Circuits", "https://example.com/subject"),
                ResourceItem("EC-205", "Analog Communication", "https://example.com/subject"),
                ResourceItem("EC-206", "Control Systems", "https://example.com/subject"),
                ResourceItem("EC-207", "Probability and Random Process", "https://example.com/subject"),
                ResourceItem("EC-208", "Electrical & Electronic Materials", "https://example.com/subject"),
                ResourceItem("EC-209", "Electromagnetic Fields & Wave Propagation", "https://example.com/subject"),
                ResourceItem("EC-212", "Digital Electronics Laboratory", "https://example.com/subject"),
                ResourceItem("EC-213", "Control Laboratory", "https://example.com/subject"),
                ResourceItem("EC-214", "Analog Communication Laboratory", "https://example.com/subject")
            )
        }
        else if (branch == "Electronics and Communication Engineering" && semester == "Fifth Semester") {
            return listOf(
                ResourceItem("EC-301", "Digital Communication", "https://example.com/subject"),
                ResourceItem("EC-302", "Microprocessors & Microcontrollers", "https://example.com/subject"),
                ResourceItem("EC-303", "Analog Integrated Circuits & Technology", "https://example.com/subject"),
                ResourceItem("EC-304", "Digital Signal Processing", "https://example.com/subject"),
                ResourceItem("EC-305", "Electronic Measurements and Instrumentation", "https://example.com/subject"),
                ResourceItem("EC-306", "Principles of Opto-Electronics and Fibre Optics", "https://example.com/subject"),
                ResourceItem("EC-311", "Microprocessor Laboratory", "https://example.com/subject"),
                ResourceItem("EC-312", "Digital Signal Processing Laboratory", "https://example.com/subject"),
                ResourceItem("EC-313", "Digital Communication Laboratory", "https://example.com/subject")
            )
        }
        else if (branch == "Electronics and Communication Engineering" && semester == "Sixth Semester") {
            return listOf(
                ResourceItem("EC-307", "RF and Microwave Engineering", "https://example.com/subject"),
                ResourceItem("EC-308", "Data Communication and Network", "https://example.com/subject"),
                ResourceItem("EC-309", "VLSI Design", "https://example.com/subject"),
                ResourceItem("EC-310", "Power Electronics", "https://example.com/subject"),
                ResourceItem("EC-33X", "Professional Core Elective I", "https://example.com/subject"),
                ResourceItem("EC-38X", "Open Elective I", "https://example.com/subject"),
                ResourceItem("EC-314", "Design Laboratory", "https://example.com/subject"),
                ResourceItem("EC-315", "Data & Optical Communication Laboratory", "https://example.com/subject"),
                ResourceItem("EC-316", "VLSI Design Laboratory", "https://example.com/subject")
            )
        }
        else if (branch == "Electronics and Communication Engineering" && semester == "Seventh Semester") {
            return listOf(
                ResourceItem("EC-401", "Wireless Communication", "https://example.com/subject"),
                ResourceItem("EC-43X", "Professional Core Elective II", "https://example.com/subject"),
                ResourceItem("EC-48X", "Open Elective II", "https://example.com/subject"),
                ResourceItem("MS-401", "Business Management", "https://example.com/subject"),
                ResourceItem("EC-498", "Project I", "https://example.com/subject"),
                ResourceItem("Industrial Training", "Industrial Training (2 credits)", "https://example.com/subject")
            )
        }
        else if (branch == "Electronics and Communication Engineering" && semester == "Eighth Semester") {
            return listOf(
                ResourceItem("HS-401", "Managerial Economics", "https://example.com/subject"),
                ResourceItem("EC-45X", "Professional Core Elective III", "https://example.com/subject"),
                ResourceItem("EC-49X", "Open Elective III", "https://example.com/subject"),
                ResourceItem("EC-499", "Project II", "https://example.com/subject")
            )
        }
        // Electronics and Instrumentation Engineering
        else if (branch == "Electronics and Instrumentation Engineering" && semester == "Third Semester") {
            return listOf(
                ResourceItem("EI 201", "Electrical & Electronic Measurements", "https://example.com/subject"),
                ResourceItem("MA 201", "Mathematics III", "https://example.com/subject"),
                ResourceItem("EI 202", "Analog Electronics", "https://example.com/subject"),
                ResourceItem("EI 203", "Circuits & Networks", "https://example.com/subject"),
                ResourceItem("CS 222", "Data Structure & Algorithm", "https://example.com/subject"),
                ResourceItem("EI 211", "Measurement Lab", "https://example.com/subject"),
                ResourceItem("EI 212", "Analog Electronics Lab", "https://example.com/subject"),
                ResourceItem("EI 213", "Circuits & Networks Lab", "https://example.com/subject"),
                ResourceItem("CS 223", "Data Structure and Algorithm Lab", "https://example.com/subject")
            )
        }
        else if (branch == "Electronics and Instrumentation Engineering" && semester == "Fourth Semester") {
            return listOf(
                ResourceItem("EI 204", "Sensors and Transducers", "https://example.com/subject"),
                ResourceItem("EI 205", "Signals and Systems", "https://example.com/subject"),
                ResourceItem("EI 206", "Control System-I", "https://example.com/subject"),
                ResourceItem("EI 207", "Digital Electronics", "https://example.com/subject"),
                ResourceItem("EI 208", "Power Electronics & Drives", "https://example.com/subject"),
                ResourceItem("EI 214", "Sensor and Transducers Lab", "https://example.com/subject"),
                ResourceItem("EI 215", "Control System Lab", "https://example.com/subject"),
                ResourceItem("EI 216", "Digital Electronics Lab", "https://example.com/subject"),
                ResourceItem("EI 217", "Power Electronics Lab", "https://example.com/subject")
            )
        }
        else if (branch == "Electronics and Instrumentation Engineering" && semester == "Fifth Semester") {
            return listOf(
                ResourceItem("EI 301", "Industrial Instrumentation-I", "https://example.com/subject"),
                ResourceItem("EI 302", "Microprocessors & Micro Controllers", "https://example.com/subject"),
                ResourceItem("EI 303", "Biomedical Instrumentation", "https://example.com/subject"),
                ResourceItem("EI 304", "Control System-II", "https://example.com/subject"),
                ResourceItem("EI 305", "Communication & Telemetry", "https://example.com/subject"),
                ResourceItem("EI 311", "Microprocessors & Micro Controllers Lab", "https://example.com/subject"),
                ResourceItem("EI 312", "Biomedical Instrumentation Lab", "https://example.com/subject"),
                ResourceItem("EI 313", "Communication & Telemetry Lab", "https://example.com/subject"),
                ResourceItem("EI 314", "Virtual Instrumentation Lab", "https://example.com/subject")
            )
        }
        else if (branch == "Electronics and Instrumentation Engineering" && semester == "Sixth Semester") {
            return listOf(
                ResourceItem("EI 306", "Industrial Instrumentation-II", "https://example.com/subject"),
                ResourceItem("EI 307", "Process Control Engineering", "https://example.com/subject"),
                ResourceItem("EI 308", "Digital Signal Processing", "https://example.com/subject"),
                ResourceItem("EI 3XX", "Professional Core Elective I", "https://example.com/subject"),
                ResourceItem("EI 3XX", "Open Elective I", "https://example.com/subject"),
                ResourceItem("EI 315", "Instrumentation Lab", "https://example.com/subject"),
                ResourceItem("EI 316", "Industrial Process Control and Automation Lab", "https://example.com/subject"),
                ResourceItem("EI 317", "Digital Signal Processing Lab", "https://example.com/subject"),
                ResourceItem("EI 318", "Simulation, Design & Fabrication Lab", "https://example.com/subject")
            )
        }
        else if (branch == "Electronics and Instrumentation Engineering" && semester == "Seventh Semester") {
            return listOf(
                ResourceItem("EI 401", "Analytical & Optical Instrumentation", "https://example.com/subject"),
                ResourceItem("EI 4XX", "Professional Core Elective II", "https://example.com/subject"),
                ResourceItem("EI 4XX", "Open Elective II", "https://example.com/subject"),
                ResourceItem("HS 401", "Managerial Economics", "https://example.com/subject"),
                ResourceItem("EI 497", "Industrial Training (Minimum 6 weeks)", "https://example.com/subject"),
                ResourceItem("EI 498", "Project I", "https://example.com/subject")
            )
        }
        else if (branch == "Electronics and Instrumentation Engineering" && semester == "Eighth Semester") {
            return listOf(
                ResourceItem("MS 401", "Business Management", "https://example.com/subject"),
                ResourceItem("EI 4XX", "Professional Core Elective III", "https://example.com/subject"),
                ResourceItem("EI 4XX", "Open Elective III", "https://example.com/subject"),
                ResourceItem("EI 499", "Project II", "https://example.com/subject")
            )
        }
        // Mechanical Engineering
        else if (branch == "Mechanical Engineering" && semester == "Third Semester") {
            return listOf(
                ResourceItem("ME 201", "Basic Thermodynamics", "https://example.com/subject"),
                ResourceItem("ME 202", "Theory of Machines", "https://example.com/subject"),
                ResourceItem("MA 103", "Mathematics III", "https://example.com/subject"),
                ResourceItem("ME 203", "Fluid Mechanics – I", "https://example.com/subject"),
                ResourceItem("ME 204", "Manufacturing Process", "https://example.com/subject"),
                ResourceItem("ME 205", "Material Science", "https://example.com/subject"),
                ResourceItem("ME 211", "Machine Drawing Lab", "https://example.com/subject"),
                ResourceItem("ME 212", "Manufacturing Lab", "https://example.com/subject"),
                ResourceItem("ME 213", "Thermo-Fluid Lab-I", "https://example.com/subject")
            )
        }
        else if (branch == "Mechanical Engineering" && semester == "Fourth Semester") {
            return listOf(
                ResourceItem("ME 206", "Applied Thermodynamics", "https://example.com/subject"),
                ResourceItem("ME 207", "Fluid Mechanics – II", "https://example.com/subject"),
                ResourceItem("ME 208", "Mechanics of Solids", "https://example.com/subject"),
                ResourceItem("ME 209", "Instrumentation and Measurement", "https://example.com/subject"),
                ResourceItem("ME 210", "Machining and Machine Tools", "https://example.com/subject"),
                ResourceItem("ME 217", "Energy Science and Technology", "https://example.com/subject"),
                ResourceItem("ME 214", "Material Testing Lab", "https://example.com/subject"),
                ResourceItem("ME 215", "Instrumentation and Measurement Lab", "https://example.com/subject"),
                ResourceItem("ME 216", "Thermo-Fluid Lab - II", "https://example.com/subject")
            )
        }
        else if (branch == "Mechanical Engineering" && semester == "Fifth Semester") {
            return listOf(
                ResourceItem("ME 301", "Heat Transfer", "https://example.com/subject"),
                ResourceItem("ME 302", "Machine Design - I", "https://example.com/subject"),
                ResourceItem("ME 303", "Turbomachinery", "https://example.com/subject"),
                ResourceItem("ME 304", "Advanced Solid Mechanics", "https://example.com/subject"),
                ResourceItem("ME 305", "I. C. Engine", "https://example.com/subject"),
                ResourceItem("ME 306", "Advanced Manufacturing Process", "https://example.com/subject"),
                ResourceItem("ME 311", "Fluid Machinery Lab", "https://example.com/subject"),
                ResourceItem("ME 312", "Machining Lab", "https://example.com/subject"),
                ResourceItem("ME 313", "Heat Transfer Lab", "https://example.com/subject")
            )
        }
        else if (branch == "Mechanical Engineering" && semester == "Sixth Semester") {
            return listOf(
                ResourceItem("ME 307", "Machine Design – II", "https://example.com/subject"),
                ResourceItem("ME 308", "Automobile Engineering", "https://example.com/subject"),
                ResourceItem("ME 309", "Power Plant Engineering", "https://example.com/subject"),
                ResourceItem("ME 310", "Dynamics and Control of Machinery", "https://example.com/subject"),
                ResourceItem("ME 3XX", "Deptt. Elective - I", "https://example.com/subject"),
                ResourceItem("ME 3XX", "Open Elective I", "https://example.com/subject"),
                ResourceItem("ME 314", "Automobile Lab", "https://example.com/subject"),
                ResourceItem("ME 315", "Dynamics Lab", "https://example.com/subject"),
                ResourceItem("ME 316", "Machine Design Lab", "https://example.com/subject")
            )
        }
        else if (branch == "Mechanical Engineering" && semester == "Seventh Semester") {
            return listOf(
                ResourceItem("ME 401", "Industrial Engineering and Operations Research", "https://example.com/subject"),
                ResourceItem("ME 4XX", "Deptt. Elective - II", "https://example.com/subject"),
                ResourceItem("ME 4XX", "Open Elective II", "https://example.com/subject"),
                ResourceItem("HS 401", "Managerial Economics", "https://example.com/subject"),
                ResourceItem("ME 497", "Industrial Training (Minimum 6 weeks)", "https://example.com/subject"),
                ResourceItem("ME 498", "Project I", "https://example.com/subject")
            )
        }
        else if (branch == "Mechanical Engineering" && semester == "Eighth Semester") {
            return listOf(
                ResourceItem("HS 401", "Business Management", "https://example.com/subject"),
                ResourceItem("ME 4XX", "Deptt. Elective - III", "https://example.com/subject"),
                ResourceItem("ME 4XX", "Open Elective III", "https://example.com/subject"),
                ResourceItem("ME 499", "Project II", "https://example.com/subject")
            )
        }

        return listOf(
            ResourceItem("EC 101", "Electronic Devices", "https://example.com/ec101"),
            ResourceItem("EC 102", "Analog Electronics", "https://example.com/ec102"),
            ResourceItem("EC 103", "Digital Systems", "https://example.com/ec103")
        )
    }

    private fun openResource(link: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        startActivity(intent)
    }
}
