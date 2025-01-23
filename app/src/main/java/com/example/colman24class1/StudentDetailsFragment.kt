package com.example.colman24class1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.Fragment

class StudentDetailsFragment : Fragment() {

    private var studentId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            studentId = it.getString("student_id")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_student_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        // Edit button click handler
        view.findViewById<Button>(R.id.editButton).setOnClickListener {
            navigateToEditStudent()
        }

        // Back button click handler
        view.findViewById<Button>(R.id.backButton).setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun navigateToEditStudent() {
        val editFragment = EditStudentFragment.newInstance(studentId ?: "")
        parentFragmentManager.beginTransaction()
            .replace(R.id.mainActivity_frameLayout, editFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.setToolbarTitle("Student Details")
    }

    companion object {
        fun newInstance(studentId: String) = StudentDetailsFragment().apply {
            arguments = Bundle().apply {
                putString("student_id", studentId)
            }
        }
    }
}
