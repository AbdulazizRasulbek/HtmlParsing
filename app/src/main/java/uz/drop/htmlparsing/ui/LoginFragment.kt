package uz.drop.htmlparsing.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import uz.drop.htmlparsing.R
import uz.drop.htmlparsing.data.Response
import uz.drop.htmlparsing.databinding.FragmentLoginBinding
import uz.drop.htmlparsing.ui.viewmodels.MainViewModel
import uz.drop.htmlparsing.utils.extensions.viewBinding

class LoginFragment : Fragment(R.layout.fragment_login) {
    private val binding: FragmentLoginBinding by viewBinding { FragmentLoginBinding.bind(it) }
    private val viewModel: MainViewModel by activityViewModels()

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("AAA", "onViewCreated: ")
        viewModelStore.clear()
        binding.loginBtn.setOnClickListener {
            val login = binding.loginEt.text.toString()
            val password = binding.passwordEt.text.toString()
            when {
                login.isEmpty() -> {
                    binding.loginLayout.error = "Введите логин"
                }
                password.isEmpty() -> {
                    binding.passwordLayout.error = "Введите пароль"
                }
                else -> {
                    viewModel.login(login, password)
                }
            }
        }
        binding.loginEt.doAfterTextChanged {
            binding.loginLayout.error = null
        }
        binding.passwordEt.doAfterTextChanged {
            binding.passwordEt.error = null
        }
        viewModel.htmlContentLiveData.observe(this, observer)
    }

    val observer = Observer<Response<String>> {
        when (it) {
            is Response.Loading -> {
                binding.loginBtn.isVisible = false
                binding.progress.isVisible = true
            }
            is Response.Success -> {
                binding.loginBtn.isVisible = true
                binding.progress.isVisible = false
                findNavController().navigate(R.id.action_loginFragment_to_dataFragment)
            }
            is Response.Error -> {
                binding.loginBtn.isVisible = true
                binding.progress.isVisible = false
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                it.e?.message?.let { it1 -> Log.e("AAA", it1) }
            }
        }
    }

}