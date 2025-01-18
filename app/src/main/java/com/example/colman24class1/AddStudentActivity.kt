package com.example.colman24class1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import androidx.activity.enableEdgeToEdge
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.setPadding


class AddStudentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_student)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar = findViewById<Toolbar>(R.id.add_user_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true) // Enable the back button
        }

        // Handle back button press using OnBackPressedDispatcher
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Perform any custom logic here if needed
                finish() // Close the activity
            }
        })

        val cancelButton: Button = findViewById<Button>(R.id.cancel_button)
        val createButton: Button = findViewById<Button>(R.id.create_button)
        val nameTextField: TextView = findViewById(R.id.name_input)
        val idTextField: TextView = findViewById(R.id.id_input)
        val phoneNumberTextField: TextView = findViewById(R.id.phone_num_input)
        val addressTextField: TextView = findViewById(R.id.address_input)
        val checkedInput: CheckBox = findViewById(R.id.user_checked_input)


        cancelButton.setOnClickListener {
            val intent = Intent(this, StudentListActivity::class.java)
            startActivity(intent)
        }

        createButton.setOnClickListener {
            val newStudent: Student =
                Student(idTextField.text.toString(),
                    nameTextField.text.toString(),
                    phoneNumberTextField.text.toString(),
                    addressTextField.text.toString(),checkedInput.isChecked)

            Student.addStudent(newStudent)
            val intent = Intent(this, StudentListActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish() // Handle toolbar back button
        return true
    }
}