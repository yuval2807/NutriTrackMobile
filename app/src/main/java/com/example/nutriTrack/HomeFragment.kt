package com.example.nutriTrack

import com.example.nutriTrack.utils.generateAIFunFact
import com.example.nutriTrack.databinding.FragmentHomeBinding

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    // This property is only valid between onCreateView and onDestroyView
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.funFactButton.setOnClickListener {
            binding.funFactButton.isEnabled = false

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
                    binding.funFactButton.isEnabled = true
                }
            }
        }
    }

    private fun createFunFactDialog(funFact: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("FunFact 🤓")
            .setMessage(funFact)
            .setNegativeButton("Thanks!", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}