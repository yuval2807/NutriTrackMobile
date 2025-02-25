package com.example.nutriTrack

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.nutriTrack.Model.Student

class ProfileFragment : Fragment() {

    private var studentId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            studentId = it.getString("student_id")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true) // Enable menu for this fragment

        val student = studentId?.let { Student.getStudentById(it) }

        student?.let {
            // Populate views with student data
            view.findViewById<TextView>(R.id.studentNameView).text = it.name
            view.findViewById<TextView>(R.id.studentIdView).text = it.id
            view.findViewById<TextView>(R.id.studentPhoneView).text = it.phone
            view.findViewById<TextView>(R.id.studentAddressView).text = it.address
            view.findViewById<CheckBox>(R.id.student_checked_input).isChecked = it.isChecked
            view.findViewById<TextView>(R.id.studentBirthDateView).text = it.birthDate
            view.findViewById<TextView>(R.id.studentBirthTimeView).text = it.birthTime
        }

        // Back button click handler
        view.findViewById<Button>(R.id.backButton).setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun navigateToEditStudent() {
        val editFragment = EditPostFragment.newInstance(studentId ?: "")
        parentFragmentManager.beginTransaction()
            .replace(R.id.mainActivity_frameLayout, editFragment)
            .addToBackStack(null)
            .commit()
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_post_details, menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_edit -> navigateToEditStudent()

        }
        return super.onOptionsItemSelected(item)
    }
    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.setToolbarTitle("Student Details")
    }

    companion object {
        fun newInstance(studentId: String) = ProfileFragment().apply {
            arguments = Bundle().apply {
                putString("student_id", studentId)
            }
        }
    }
}
