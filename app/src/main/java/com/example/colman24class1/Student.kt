package com.example.colman24class1

data class Student(
    var id: String,
    var name: String,
    var phone: String,
    var address: String,
    var isChecked: Boolean
) {
    companion object {
        private var counter = 0

        // Static list to store students (our in-memory "database")
        private val studentsList = mutableListOf<Student>()

        fun getAllStudents(): List<Student> {
            return studentsList
        }

        fun addStudent(student: Student) {
            studentsList.add(student)
        }

        fun updateStudent(updatedStudent: Student) {
            val index = studentsList.indexOfFirst { it.id == updatedStudent.id }
            if (index != -1) {
                studentsList[index] = updatedStudent
            }
        }

        fun deleteStudent(studentId: String) {
            studentsList.removeIf { it.id == studentId }
        }

        fun getStudentById(studentId: String): Student? {
            return studentsList.find { it.id == studentId }
        }
    }
}