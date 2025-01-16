package com.example.colman24class1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.time.temporal.TemporalField


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

        val cancelButton: Button = findViewById<Button>(R.id.cancel_button)
        val createButton: Button = findViewById<Button>(R.id.create_button)
        val firstNameTextField: TextView = findViewById(R.id.first_name)
        val secondNameTextField: TextView = findViewById(R.id.second_name)
        val savedFistName: EditText = findViewById(R.id.editText)
        // val savedTextField: TextView = findViewById(R.id.add_student_success_saved_text_view)

        cancelButton.setOnClickListener {
          //  savedTextField.text = "${nameTextField.text} ${idTextField.text} is saved...!!!"

        }

        createButton.setOnClickListener {
            finish()

        }
    }

}
