package com.example.nutriTrack

import com.example.nutriTrack.utils.generateAIFunFact

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val funFactButton = view.findViewById<Button>(R.id.funFactButton)

        funFactButton.setOnClickListener {
            funFactButton.isEnabled = false

            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    lifecycleScope.launch {
                        val funFact = generateAIFunFact()
                        createFunFactDialog(funFact)
                    }
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Could not load nutrition fact", Toast.LENGTH_SHORT).show()
                    Log.e("FunFact", "Error getting fun fact", e)
                } finally {
                    funFactButton.isEnabled = true
                }
            }
        }

        return view
    }

    fun createFunFactDialog(funFact: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("FunFact 🤓")
            .setMessage(funFact)
            .setNegativeButton("Thanks!", null)
            .show()
    }
}