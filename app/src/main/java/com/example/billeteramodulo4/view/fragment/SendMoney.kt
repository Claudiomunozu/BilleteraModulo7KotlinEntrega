package com.example.billeteramodulo4.view.fragment

import android.os.Bundle
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
import com.example.billeteramodulo4.databinding.FragmentSendMoneyBinding
import com.example.billeteramodulo4.model.local.database.DataBaseWallet
import com.example.billeteramodulo4.model.remote.api.ApiWallet
import com.example.billeteramodulo4.model.remote.retrofit.RetrofitWallet
import com.example.billeteramodulo4.model.repository.ImplementRepository
import com.example.billeteramodulo4.viewmodel.AccountViewModel
import com.example.billeteramodulo4.viewmodel.LoginPageViewModel
import com.example.billeteramodulo4.viewmodel.SendMoneyViewModel
import com.example.billeteramodulo4.viewmodel.TransactionViewModel
import com.example.billeteramodulo4.viewmodel.usecase.UseCaseWallet
import com.example.billeteramodulo4.viewmodel.viewmodelfactory.AccountViewModelFactory
import com.example.billeteramodulo4.viewmodel.viewmodelfactory.TransactionViewModelFactory
import com.example.billeteramodulo4.viewmodel.viewmodelfactory.UserViewModelFactory

class SendMoney : Fragment() {

    private lateinit var binding: FragmentSendMoneyBinding
    private val viewModel: SendMoneyViewModel by viewModels()
    private lateinit var userViewModel: LoginPageViewModel
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var transactionViewModel: TransactionViewModel
    private var cantidadInt: Int = 0
    private lateinit var useCase: UseCaseWallet
    private lateinit var userViewModelFactory: UserViewModelFactory
    private lateinit var accountViewModelFactory: AccountViewModelFactory
    private lateinit var transactionViewModelFactory: TransactionViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

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

        binding = FragmentSendMoneyBinding.inflate(inflater, container, false)
        val root = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnEnviarDineroSendPage.setOnClickListener {
                val nota = binding.tvNotaTransferencia.text.toString()
                if (nota.isBlank()) {
                    Toast.makeText(
                        requireContext(),
                        "Por favor ingrese una nota",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    return@setOnClickListener
                }
                val cantidad = etDineroSendPage.text.toString()
                cantidadInt = cantidad.toIntOrNull()!!
                val saldo = accountViewModel.restarSaldoUsuario(cantidadInt)

                if (cantidadInt != null && cantidadInt > 0) {
                    viewModel.validarEmptySend(cantidadInt)
                } else {
                    // Mostrar mensaje de error
                    Toast.makeText(
                        requireContext(),
                        "Ingrese una cantidad válida",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        viewModel.validarSend.observe(viewLifecycleOwner, Observer { valido ->
            val user = userViewModel.usuarioLogueado.value
            val nota = binding.tvNotaTransferencia.text.toString()
            accountViewModel.accounts.observe(viewLifecycleOwner) { accounts ->
                if (accounts.isNotEmpty() && user != null) {
                    val userAccount = accounts[0]
                    transactionViewModel.createTransaction(
                        cantidadInt.toLong(),
                        nota,
                        "sendMoney",
                        userAccount.id,
                        user.id
                    )
                    Toast.makeText(requireContext(), "Envío de dinero exitoso", Toast.LENGTH_SHORT)
                        .show()
                    findNavController().navigate(R.id.homePage)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "No se pudo obtener la cuenta del usuario",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            if (valido == true) {
                Toast.makeText(requireContext(), "Transacción exitosa", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_sendMoney_to_homePage)
                binding.etDineroSendPage.setText("") // Borrar el caché del EditText
            } else {
                Toast.makeText(
                    requireContext(),
                    "No se pudo realizar la transacción",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}