package com.example.billeteramodulo4.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.billeteramodulo4.R
import com.example.billeteramodulo4.databinding.FragmentSplashBinding

class Splash : Fragment() {

    private lateinit var binding: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Infla el diseño del fragmento
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        val root = binding.root

        binding.root.setOnClickListener {
            findNavController().navigate(R.id.action_splash_to_welcomeFragment)
        }
        return root
    }
}