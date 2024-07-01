package com.example.billeteramodulo4.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.billeteramodulo4.R
import com.example.billeteramodulo4.databinding.FragmentHomePageEmptyCaseBinding
import com.example.billeteramodulo4.model.local.database.DataBaseWallet
import com.example.billeteramodulo4.model.remote.api.ApiWallet
import com.example.billeteramodulo4.model.remote.retrofit.RetrofitWallet
import com.example.billeteramodulo4.model.repository.ImplementRepository
import com.example.billeteramodulo4.viewmodel.AccountViewModel
import com.example.billeteramodulo4.viewmodel.LoginPageViewModel
import com.example.billeteramodulo4.viewmodel.TransactionViewModel
import com.example.billeteramodulo4.viewmodel.usecase.UseCaseWallet
import com.example.billeteramodulo4.viewmodel.viewmodelfactory.AccountViewModelFactory
import com.example.billeteramodulo4.viewmodel.viewmodelfactory.TransactionViewModelFactory
import com.example.billeteramodulo4.viewmodel.viewmodelfactory.UserViewModelFactory
import retrofit2.Retrofit

class HomePageEmptyCase : Fragment() {

    private lateinit var binding: FragmentHomePageEmptyCaseBinding
    private lateinit var userViewModel: LoginPageViewModel
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var useCase: UseCaseWallet
    private lateinit var userViewModelFactory: UserViewModelFactory
    private lateinit var accountViewModelFactory: AccountViewModelFactory
    private lateinit var transactionViewModelFactory: TransactionViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = DataBaseWallet.getDataBase(requireContext())
        val walletDao = database.daoWallet()
        val apiService = RetrofitWallet.getRetrofit().create(ApiWallet::class.java)
        val repository = ImplementRepository(apiService, walletDao)

        useCase = UseCaseWallet(repository)
        userViewModelFactory = UserViewModelFactory(useCase, requireContext())
        accountViewModelFactory = AccountViewModelFactory(useCase, requireContext())
        transactionViewModelFactory = TransactionViewModelFactory(useCase, requireContext())

        userViewModel =
            ViewModelProvider(this, userViewModelFactory).get(LoginPageViewModel::class.java)
        accountViewModel =
            ViewModelProvider(this, accountViewModelFactory).get(AccountViewModel::class.java)
        transactionViewModel = ViewModelProvider(
            this,
            transactionViewModelFactory
        ).get(TransactionViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomePageEmptyCaseBinding.inflate(inflater, container, false)

        val root = binding.root

        binding.enviardineroemptypage.setOnClickListener {
            findNavController().navigate(R.id.action_homePageEmptyCase_to_sendMoney)
        }
        binding.ingresardineroemptypage.setOnClickListener {
            findNavController().navigate(R.id.action_homePageEmptyCase_to_requestMoney)
        }

        binding.profileuserempty.setOnClickListener {
            findNavController().navigate(R.id.action_homePageEmptyCase_to_profilePage)
        }

        binding.tvCerrarSesion.setOnClickListener {
            findNavController().navigate(R.id.action_homePageEmptyCase_to_splash)
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel.fetchLoggedUser()
        userViewModel.usuarioLogueado.observe(viewLifecycleOwner) { usuario ->
            if (usuario != null) {
                Log.i("USUARIO", "usuario: $usuario")
                binding.holaempty.text = "Hola, ${usuario.firstName}"
                accountViewModel.fetchUserAccounts()
            }
        }
    }
}