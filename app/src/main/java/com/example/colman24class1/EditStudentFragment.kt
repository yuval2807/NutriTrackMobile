package com.example.colman24class1

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar

class EditStudentFragment : Fragment() {

    private lateinit var nameTextField: EditText
    private lateinit var idTextField: EditText
    private lateinit var phoneNumberTextField: EditText
    private lateinit var addressTextField: EditText
    private lateinit var checkedInput: CheckBox
    private lateinit var birthDateView: TextInputEditText
    private lateinit var birthTimeView: TextView

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
        birthDateView  = view.findViewById(R.id.studentBirthDateView)
        birthTimeView = view.findViewById(R.id.studentBirthTimeView)

        val studentId = arguments?.getString("student_id")
        val student = studentId?.let { Student.getStudentById(it) }

        // Populate fields with existing student data
        student?.let {
            nameTextField.setText(it.name)
            idTextField.setText(it.id)
            phoneNumberTextField.setText(it.phone)
            addressTextField.setText(it.address)
            birthDateView.setText(it.birthDate)
            birthTimeView.setText(it.birthTime)
            checkedInput.isChecked = it.isChecked
        }

        // Save button click handler
        view.findViewById<Button>(R.id.saveButton).setOnClickListener {
            val updatedStudent = Student(
                idTextField.text.toString(),
                nameTextField.text.toString(),
                phoneNumberTextField.text.toString(),
                addressTextField.text.toString(),
                birthDateView.text.toString(),
                birthTimeView.text.toString(),
                checkedInput.isChecked
            )

            if (studentId != null) {
                Student.updateStudent(studentId, updatedStudent)
                showSaveSuccessDialog(updatedStudent)
            }
        }

        birthDateView.setOnClickListener {
            showDatePicker(birthDateView)
        }

        birthTimeView.setOnClickListener {
            showTimePicker(birthTimeView)
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

    private fun showDatePicker(birthDateView:TextInputEditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                birthDateView.setText(selectedDate)
            },
            year,
            month,
            day
        ).show()
    }

    private fun showTimePicker(birthTimeView:TextView) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(
            requireContext(),
            { _, selectedHour, selectedMinute ->
                val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                birthTimeView.text = selectedTime
            },
            hour,
            minute,
            true // 24-hour format
        ).show()
    }
    private fun showSaveSuccessDialog(updatedStudent: Student) {
        val dialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Save Successful")
            .setMessage("The student details have been saved successfully.")
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .setPositiveButton("OK") { _, _ ->
                // Navigate to the StudentDetailsFragment
                val detailsFragment = StudentDetailsFragment.newInstance(updatedStudent.id)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.mainActivity_frameLayout, detailsFragment)
                    .addToBackStack(null)
                    .commit()
            }
            .create()

        dialog.show()
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
