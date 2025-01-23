package com.example.colman24class1

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private var studentListFragment: StudentListFragment? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            studentListFragment = StudentListFragment();
            addFragment(studentListFragment)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar = findViewById<Toolbar>(R.id.main_activity_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true) // Enable the back button
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (supportFragmentManager.backStackEntryCount > 0) {
                    supportFragmentManager.popBackStack()
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })

 }

    fun setToolbarTitle(title: String) {
        supportActionBar?.title = title
    }

    fun addFragment(fragment: StudentListFragment?) {
        fragment?.let {
            supportFragmentManager.beginTransaction().apply {
                add(R.id.mainActivity_frameLayout, fragment)
                    .addToBackStack(null).commit();
            }
        }

    }

    fun removeFragment(fragment: StudentListFragment? ) {
        fragment?.let {
            supportFragmentManager.beginTransaction().apply {
                remove(it)
                commit();
            }
        }

        //fragment = null;

    }
}
