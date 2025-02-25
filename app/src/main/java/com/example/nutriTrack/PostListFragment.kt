package com.example.nutriTrack

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PostListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
//    private lateinit var adapter: StudentAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_post_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true) // Enable menu for this fragment

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

//        adapter = StudentAdapter(Student.getAllStudents()) { student ->
//            // Handle click on student item
//            parentFragmentManager.beginTransaction()
//                .replace(R.id.mainActivity_frameLayout, StudentDetailsFragment.newInstance(student.id))
//                .addToBackStack(null)
//                .commit()
//        }
//        recyclerView.adapter = adapter

    }
    private fun navigateToAddStudent() {
        val addStudentFragment = AddPostFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.mainActivity_frameLayout, addStudentFragment)
            .addToBackStack(null)
            .commit()
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_post_list, menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add -> navigateToAddStudent()
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onResume() {
        super.onResume()
        // Refresh the list when returning to this fragment
//        adapter.updateStudents(Student.getAllStudents())
        (activity as? MainActivity)?.setToolbarTitle("Students List")
    }
}
