package com.example.billeteramodulo4.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.billeteramodulo4.R
import com.example.billeteramodulo4.databinding.FragmentSingupPageBinding
import com.example.billeteramodulo4.model.local.database.DataBaseWallet
import com.example.billeteramodulo4.model.remote.api.ApiWallet
import com.example.billeteramodulo4.model.remote.request_response.UserResponse
import com.example.billeteramodulo4.model.remote.retrofit.RetrofitWallet
import com.example.billeteramodulo4.model.repository.ImplementRepository
import com.example.billeteramodulo4.viewmodel.LoginPageViewModel
import com.example.billeteramodulo4.viewmodel.SingupPageViewModel
import com.example.billeteramodulo4.viewmodel.usecase.UseCaseWallet
import com.example.billeteramodulo4.viewmodel.viewmodelfactory.UserViewModelFactory

class SingupPage : Fragment() {

    private lateinit var binding: FragmentSingupPageBinding
    private var _binding: FragmentSingupPageBinding? = null
    private val viewModel: SingupPageViewModel by viewModels()
    private lateinit var useCase: UseCaseWallet
    private lateinit var userViewModelFactory: UserViewModelFactory
    private lateinit var userViewModel: LoginPageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dataBase = DataBaseWallet.getDataBase(requireContext())
        val dao = dataBase.daoWallet()
        val api = RetrofitWallet.getRetrofit().create(ApiWallet::class.java)
        val repository = ImplementRepository(api, dao)
        useCase = UseCaseWallet(repository)
        userViewModelFactory = UserViewModelFactory(useCase, requireContext())
        userViewModel =
            ViewModelProvider(this, userViewModelFactory).get(LoginPageViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSingupPageBinding.inflate(inflater, container, false)
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.welcomeFragment)
                }
            })
        val root = binding.root

        binding.yatienescuentasinguppage.setOnClickListener {

            findNavController().navigate(R.id.loginPageFragment)
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.crearcuenta.setOnClickListener { crearCuenta() }

        userViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                Log.e("ERROR", it)
            }
        }

        userViewModel.createdUserId.observe(viewLifecycleOwner) { userId ->
            if (userId != null) {
                Toast.makeText(requireContext(), "Registro exitoso", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.loginPageFragment)
            } else {
                Toast.makeText(requireContext(), "Error en el registro", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun crearCuenta() {

        val name = binding.etNombre.text.toString()
        val lastName = binding.etApellido.text.toString()
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        val password2 = binding.etPassword2.text.toString()

        if (password != password2) {
            Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT)
                .show()
            return
        }
        viewModel.validarDatos(name, lastName, email, password, password2)

        val newUser = UserResponse(
            id = 0,
            firstName = name,
            lastName = lastName,
            email = email,
            password = password,
            points = 0,
            roleId = 1
        )
        userViewModel.createUser(newUser)
        Toast.makeText(requireContext(), "Registro exitoso", Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.loginPageFragment)
    }
}




