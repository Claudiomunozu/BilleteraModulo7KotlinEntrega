package com.example.billeteramodulo4.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.billeteramodulo4.R
import com.example.billeteramodulo4.databinding.FragmentWelcomeBinding

class Welcome : Fragment() {

    private lateinit var binding: FragmentWelcomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        val root = binding.root

        binding.buttonWelcome.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_singupPageFragment)
        }

        binding.yatienescuenta.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_loginPageFragment)
        }
        return root
    }
}
