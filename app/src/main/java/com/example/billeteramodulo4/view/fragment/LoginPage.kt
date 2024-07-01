package com.example.billeteramodulo4.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.billeteramodulo4.R
import com.example.billeteramodulo4.databinding.FragmentLoginPageBinding
import com.example.billeteramodulo4.model.local.database.DataBaseWallet
import com.example.billeteramodulo4.model.remote.api.ApiWallet
import com.example.billeteramodulo4.model.remote.retrofit.RetrofitWallet
import com.example.billeteramodulo4.model.repository.ImplementRepository
import com.example.billeteramodulo4.viewmodel.LoginPageViewModel
import com.example.billeteramodulo4.viewmodel.usecase.UseCaseWallet
import com.example.billeteramodulo4.viewmodel.viewmodelfactory.UserViewModelFactory

class LoginPage : Fragment() {

    private lateinit var binding: FragmentLoginPageBinding
    private val viewModel: LoginPageViewModel by viewModels()
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
        userViewModel =ViewModelProvider(this, userViewModelFactory).get(LoginPageViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentLoginPageBinding.inflate(inflater, container, false)
        val root = binding.root

        binding.crearUnaNuevaCuentaLogin.setOnClickListener {
            findNavController().navigate(R.id.action_loginPageFragment_to_singupPageFragment)
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener { validarEmailPassword() }

        userViewModel.token.observe(viewLifecycleOwner) { token ->
            if (token != null) {
                Toast.makeText(requireContext(),"Login exitoso. Token: $token",Toast.LENGTH_SHORT).show()
                Log.e("TOKEN prueba",token.toString()
                )// Muestra Token recibido desde Api en consola Logcat
                viewModel.fetchLoggedUser() // Fetch user data
            } else {
                Toast.makeText(
                    requireContext(),
                    "User o contraseña incorrectos",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }

        userViewModel.usuarioLogueado.observe(viewLifecycleOwner) { usuario ->
            Log.d("USUARIO", usuario.firstName)
            if (usuario != null) {
                findNavController().navigate(R.id.action_loginPageFragment_to_homePageEmptyCase)
            }
        }
    }

    private fun validarEmailPassword() {

        if (binding.etEmailAddress.text.toString().isEmpty()) {
            binding.etEmailAddress.error = "Campo obligatorio"
            return
        }
        if (binding.etPassword.text.toString().isEmpty()) {
            binding.etPassword.error = "Campo obligatorio"
        } else {
            val email = binding.etEmailAddress.text.toString()
            val password = binding.etPassword.text.toString()
            userViewModel.login(email, password)
        }
    }
}
