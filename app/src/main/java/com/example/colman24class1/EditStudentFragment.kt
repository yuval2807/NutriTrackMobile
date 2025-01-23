package com.example.colman24class1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment

class EditStudentFragment : Fragment() {

    private lateinit var nameTextField: EditText
    private lateinit var idTextField: EditText
    private lateinit var phoneNumberTextField: EditText
    private lateinit var addressTextField: EditText
    private lateinit var checkedInput: CheckBox

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit_student, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get references to views
        nameTextField = view.findViewById(R.id.studentNameView)
        idTextField = view.findViewById(R.id.studentIdView)
        phoneNumberTextField = view.findViewById(R.id.studentPhoneView)
        addressTextField = view.findViewById(R.id.studentAddressView)
        checkedInput = view.findViewById(R.id.student_checked_input)

        val studentId = arguments?.getString("student_id")
        val student = studentId?.let { Student.getStudentById(it) }

        // Populate fields with existing student data
        student?.let {
            nameTextField.setText(it.name)
            idTextField.setText(it.id)
            phoneNumberTextField.setText(it.phone)
            addressTextField.setText(it.address)
            checkedInput.isChecked = it.isChecked
        }

        // Save button click handler
        view.findViewById<Button>(R.id.saveButton).setOnClickListener {
            val updatedStudent = Student(
                idTextField.text.toString(),
                nameTextField.text.toString(),
                phoneNumberTextField.text.toString(),
                addressTextField.text.toString(),
                checkedInput.isChecked
            )

            if (studentId != null) {
                Student.updateStudent(studentId, updatedStudent)
            }

            // Navigate to details screen
            navigateToStudentDetails(updatedStudent.id)
        }

        // Delete button click handler
        view.findViewById<Button>(R.id.delete_button).setOnClickListener {
            studentId?.let { id ->
                Student.deleteStudent(id)
                // Navigate to list screen
                navigateToStudentList()
            }
        }

        // Cancel button click handler
        view.findViewById<Button>(R.id.cancelButton).setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun navigateToStudentDetails(studentId: String) {
        val detailsFragment = StudentDetailsFragment.newInstance(studentId)
        parentFragmentManager.beginTransaction()
            .replace(R.id.mainActivity_frameLayout, detailsFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun navigateToStudentList() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.mainActivity_frameLayout, StudentListFragment())
            .addToBackStack(null)
            .commit()
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.setToolbarTitle("Edit Student")
    }

    companion object {
        fun newInstance(studentId: String): EditStudentFragment {
            val fragment = EditStudentFragment()
            val args = Bundle()
            args.putString("student_id", studentId)
            fragment.arguments = args
            return fragment
        }
    }
}
