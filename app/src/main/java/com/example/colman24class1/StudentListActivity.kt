package com.example.colman24class1

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class StudentListActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: com.example.colman24class1.StudentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_list)

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.studentsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = com.example.colman24class1.StudentAdapter(Student.getAllStudents()) { student ->
            // Handle click on student item
            val intent = Intent(this, Student::class.java)
            intent.putExtra("student_id", student.id)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        // FAB for adding new student
        val fabAddStudent: FloatingActionButton = findViewById(R.id.fabAddStudent)
        fabAddStudent.setOnClickListener {
            startActivity(Intent(this, AddStudentActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh the list when returning to this activity
        adapter.updateStudents(Student.getAllStudents())
    }
}

class StudentAdapter(
    private var students: List<Student>,
    private val onItemClick: (Student) -> Unit
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    class StudentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameText: TextView = view.findViewById(R.id.studentName)
        val idText: TextView = view.findViewById(R.id.studentId)
        val checkBox: CheckBox = view.findViewById(R.id.studentCheckBox)
//        val studentImage: ImageView = view.findViewById(R.id.studentImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.student_list_item, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = students[position]
        holder.nameText.text = student.name
        holder.idText.text = student.id
        holder.checkBox.isChecked = student.isChecked
//        holder.studentImage.setImageResource(R.drawable.default_student_image)

        holder.itemView.setOnClickListener { onItemClick(student) }

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            student.isChecked = isChecked
            Student.updateStudent(student)
        }
    }

    override fun getItemCount() = students.size

    fun updateStudents(newStudents: List<Student>) {
        students = newStudents
        notifyDataSetChanged()
    }
}