package com.unero.sceneviewdemo.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.unero.sceneviewdemo.R
import com.unero.sceneviewdemo.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding as FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnArViewer.setOnClickListener { setDestination(0) }
            btnArCursor.setOnClickListener { setDestination(1) }
            btnArAnchor.setOnClickListener { showNotImplemented() }
            btnViewer.setOnClickListener { setDestination(2) }
        }
    }

    private fun setDestination(target: Int) {
        val action = when(target) {
            0 -> {
                HomeFragmentDirections.actionHomeFragmentToArModelViewerFragment()
            }
            1 -> {
                HomeFragmentDirections.actionHomeFragmentToArCursorFragment()
            }
            2 -> {
                HomeFragmentDirections.actionHomeFragmentToModelViewerFragment()
            }
            else -> {
                HomeFragmentDirections.actionHomeFragmentToArModelViewerFragment()
            }
        }
        findNavController().navigate(action)
    }

    private fun showNotImplemented() {
        Snackbar.make(
            binding.root,
            "Not implemented yet",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}