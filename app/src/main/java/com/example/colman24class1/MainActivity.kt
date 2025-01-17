package com.example.colman24class1

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {

    var fragmentOne: BlueFragment? = null
    var fragmentTwo: BlueFragment? = null
    var fragmentThree: BlueFragment? = null
    var fragmentFour: BlueFragment? = null

    var buttonOne: Button? = null
    var buttonTwo: Button? = null
    var buttonThree: Button? = null
    var buttonFour: Button? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


    // TODO: 1. Button to navigate to Add com.example.colman24class1.Student
    // TODO: 2. Create a button listener
    // TODO: 3. Create an intent

        val addStudentButton: Button = findViewById<Button>(R.id.mainActivity_addStudent_button)
        val showStudentListButton: Button = findViewById<Button>(R.id.mainActivity_showStudentList_button)

//    class MyListener { }
//    val listener = MyListener();
//    addStudentButton.setOnClickListener( View.OnClickListener {
//        override fun onClick(v: View?) {
//            //TOdo
//        }
//    })
// addStudentButton.setOnClickListener(listener)

//    addStudentButton.setOnClickListener {
//        val intent = Intent(this, AddStudentActivity::class.java)
//        startActivity(intent)
//
////        if (fragment == null) {
////            addFragment()
////        } else {
////            removeFragment()
////        }
//    }

        showStudentListButton.setOnClickListener {
            val intent = Intent(this, StudentListActivity::class.java)
            startActivity(intent)

//        if (fragment == null) {
//            addFragment()
//        } else {
//            removeFragment()
//        }
        }

         fragmentOne = BlueFragment.newInstance("1");
         fragmentTwo = BlueFragment.newInstance("2");
         fragmentThree = BlueFragment.newInstance("3");
         fragmentFour = BlueFragment.newInstance("4");

         buttonOne= findViewById<Button>(R.id.button)
         buttonTwo= findViewById<Button>(R.id.button2)
         buttonThree = findViewById<Button>(R.id.button3)
         buttonFour = findViewById<Button>(R.id.button4)

        buttonOne?.setOnClickListener {
            addFragment(fragmentOne)
        }

        buttonTwo?.setOnClickListener {
            addFragment(fragmentTwo)
        }

        buttonThree?.setOnClickListener {
            addFragment(fragmentThree)
        }

        buttonFour?.setOnClickListener {
            addFragment(fragmentFour)
        }

        addFragment(fragmentOne);

 }

    fun addFragment(fragment: BlueFragment?) {
        //fragment = BlueFragment.newInstance("This is my title");
        //fragment?.title = "This is my text";
        fragment?.let {
            supportFragmentManager.beginTransaction().apply {
                add(R.id.mainActivity_frameLayout, fragment)
                commit();
            }
        }

    }

    fun removeFragment(fragment: BlueFragment? ) {
        fragment?.let {
            supportFragmentManager.beginTransaction().apply {
                remove(it)
                commit();
            }
        }

        //fragment = null;

    }
}
