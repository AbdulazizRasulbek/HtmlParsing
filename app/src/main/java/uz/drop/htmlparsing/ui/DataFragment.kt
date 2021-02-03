package uz.drop.htmlparsing.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import uz.drop.htmlparsing.R
import uz.drop.htmlparsing.data.Data
import uz.drop.htmlparsing.data.Response
import uz.drop.htmlparsing.databinding.FragmentDataBinding
import uz.drop.htmlparsing.ui.adapter.Adapter
import uz.drop.htmlparsing.ui.viewmodels.MainViewModel
import uz.drop.htmlparsing.utils.extensions.viewBinding

class DataFragment : Fragment(R.layout.fragment_data) {
    private val binding: FragmentDataBinding by viewBinding { FragmentDataBinding.bind(it) }
    private val viewModel: MainViewModel by activityViewModels()

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModelStore.clear()
                isEnabled = false
                requireActivity().onBackPressed()
            }

        })
        binding.recycler.layoutManager = LinearLayoutManager(context)
        viewModel.htmlContentLiveData.observe(this, htmlContentObserver)
        binding.recycler.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )


        viewModel.listLiveData.observe(this, listObserver)
    }

    private val listObserver = Observer<Response<List<Data>>> {
        when (it) {
            is Response.Loading -> {
                binding.progress.isVisible = true
            }
            is Response.Success -> {
                binding.recycler.adapter = Adapter(it.data)
                binding.progress.isVisible = false
            }
            is Response.Error -> {
                binding.progress.isVisible = false
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val htmlContentObserver = Observer<Response<String>> {
        when (it) {
            is Response.Loading -> {
                binding.progress.isVisible = true
            }
            is Response.Success -> {
                binding.progress.isVisible = false
                val html = it.data
                viewModel.parseHtml(html = html)
                Log.d("AAA", it.data)
            }
            is Response.Error -> {
                binding.progress.isVisible = false
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                it.e?.message?.let { it1 -> Log.e("AAA", it1) }
            }
        }
    }


}