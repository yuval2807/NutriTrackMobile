package com.example.colman24class1

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar

class AddStudentFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_student, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cancelButton: Button = view.findViewById(R.id.cancelButton)
        val createButton: Button = view.findViewById(R.id.create_button)
        val nameTextField: TextView = view.findViewById(R.id.name_input)
        val idTextField: TextView = view.findViewById(R.id.id_input)
        val phoneNumberTextField: TextView = view.findViewById(R.id.phone_num_input)
        val addressTextField: TextView = view.findViewById(R.id.address_input)
        val checkedInput: CheckBox = view.findViewById(R.id.user_checked_input)
        val birthDateView : TextInputEditText = view.findViewById(R.id.studentBirthDateView)
        val birthTimeView:TextView = view.findViewById(R.id.studentBirthTimeView)

        cancelButton.setOnClickListener {
            // Navigate back to the student list
            parentFragmentManager.popBackStack()
        }

        createButton.setOnClickListener {
            val newStudent = Student(
                idTextField.text.toString(),
                nameTextField.text.toString(),
                phoneNumberTextField.text.toString(),
                addressTextField.text.toString(),
                birthDateView.text.toString(),
                birthTimeView.text.toString(),
                checkedInput.isChecked
            )

            Student.addStudent(newStudent)

            // Navigate back to the student list
            parentFragmentManager.beginTransaction()
                .replace(R.id.mainActivity_frameLayout, StudentListFragment())
                .addToBackStack(null)
                .commit()
        }

        birthDateView.setOnClickListener {
            this.showDatePicker(birthDateView)
        }

        // Setup time picker
        birthTimeView.setOnClickListener {
            this.showTimePicker(birthTimeView)
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh the list when returning to this fragment
        (activity as? MainActivity)?.setToolbarTitle("New Student")
    }

    companion object {
        fun newInstance() = AddStudentFragment()
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

}
