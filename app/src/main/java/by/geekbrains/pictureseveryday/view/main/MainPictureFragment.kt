package by.geekbrains.pictureseveryday.view.main

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.geekbrains.pictureseveryday.R
import by.geekbrains.pictureseveryday.databinding.FragmentMainPictureBinding
import by.geekbrains.pictureseveryday.viewmodel.AppState
import by.geekbrains.pictureseveryday.viewmodel.MainPictureViewModel
import coil.api.load

class MainPictureFragment : Fragment() {

    private var _binding: FragmentMainPictureBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainPictureViewModel by lazy {
        ViewModelProvider(this)[MainPictureViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMainPictureBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveData()
            .observe(viewLifecycleOwner, androidx.lifecycle.Observer { renderData(it) })
    }

    private fun renderData(state: AppState) {
        when (state) {
            is AppState.Success -> {
                with(binding) {
                    mainPictureFragment.visibility = View.VISIBLE
                    loadingLayout.visibility = View.GONE
                }
                val serverResponseData = state.serverResponseData
                val url = serverResponseData.url
                if (url.isNullOrEmpty()) {
                    toast("Url is empty")
                } else {
                    binding.mainImageView.load(url) {
                        lifecycle(this@MainPictureFragment)
                        error(R.drawable.ic_load_error)
                        placeholder(R.drawable.ic_no_photo)
                    }
                }
            }

            is AppState.Loading -> {
                with(binding) {
                    mainPictureFragment.visibility = View.GONE
                    loadingLayout.visibility = View.VISIBLE
                }
            }

            is AppState.Error -> {
                with(binding) {
                    mainPictureFragment.visibility = View.VISIBLE
                    loadingLayout.visibility = View.GONE
                }
                toast(state.error.message)
            }
        }
    }

    private fun Fragment.toast(string: String?) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).apply {
            setGravity(Gravity.BOTTOM, 0, 250)
            show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = MainPictureFragment()
        private var isMain = true
    }
}