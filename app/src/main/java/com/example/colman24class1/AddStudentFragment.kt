package com.example.colman24class1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.Fragment

class AddStudentFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_student, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cancelButton: Button = view.findViewById(R.id.cancel_button)
        val createButton: Button = view.findViewById(R.id.create_button)
        val nameTextField: TextView = view.findViewById(R.id.name_input)
        val idTextField: TextView = view.findViewById(R.id.id_input)
        val phoneNumberTextField: TextView = view.findViewById(R.id.phone_num_input)
        val addressTextField: TextView = view.findViewById(R.id.address_input)
        val checkedInput: CheckBox = view.findViewById(R.id.user_checked_input)

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
                checkedInput.isChecked
            )

            Student.addStudent(newStudent)

            // Navigate back to the student list
            parentFragmentManager.beginTransaction()
                .replace(R.id.mainActivity_frameLayout, StudentListFragment())
                .addToBackStack(null)
                .commit()
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
}
