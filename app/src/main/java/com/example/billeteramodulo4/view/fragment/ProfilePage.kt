package com.example.billeteramodulo4.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.billeteramodulo4.R
import com.example.billeteramodulo4.databinding.FragmentProfilePageBinding

class ProfilePage : Fragment() {

    private lateinit var binding: FragmentProfilePageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfilePageBinding.inflate(inflater, container, false)
        val root = binding.root

        binding.sendhome.setOnClickListener {
            findNavController().navigate(R.id.action_profilePage_to_splash)
        }
        return root
    }
}