package com.example.colman24class1

data class Student(
    var id: String,
    var name: String,
    var phone: String,
    var address: String,
    var isChecked: Boolean = false
) {
    companion object {
        private var counter = 0

        // Static list to store students (our in-memory "database")
        private val studentsList = mutableListOf<Student>()

        fun getAllStudents(): List<Student> {
            return listOf(
                Student("1", "Alice", "0500000", "Tel Aviv",false),
                Student("2", "Bob", "0500000", "Tel Aviv",true),
                Student("3", "Charlie", "0500000", "Tel Aviv",false)
            )
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