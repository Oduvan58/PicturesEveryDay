package by.geekbrains.pictureseveryday.view.api

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnticipateOvershootInterpolator
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import by.geekbrains.pictureseveryday.R
import by.geekbrains.pictureseveryday.databinding.FragmentYesterdayStartBinding
import by.geekbrains.pictureseveryday.utils.toast
import by.geekbrains.pictureseveryday.viewmodel.AppState
import by.geekbrains.pictureseveryday.viewmodel.MainPictureViewModel
import coil.api.load
import java.text.SimpleDateFormat
import java.util.*

class YesterdayFragment : Fragment() {

    private var _binding: FragmentYesterdayStartBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainPictureViewModel by lazy {
        ViewModelProvider(this)[MainPictureViewModel::class.java]
    }

    private var isShow = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentYesterdayStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.yesterdayImageView.setOnClickListener {
            if (isShow) hideInfo() else showInfo()
        }
        viewModel.getLiveData(yesterdayDate()).observe(viewLifecycleOwner) { renderData(it) }
    }

    private fun showInfo() {
        isShow = true
        val constraintSet = ConstraintSet()
        constraintSet.clone(context, R.layout.fragment_yesterday_end)
        val transition = ChangeBounds()
        transition.interpolator = AnticipateOvershootInterpolator(5.0f)
        transition.duration = 1000
        TransitionManager.beginDelayedTransition(
            binding.fragmentYesterdayStartContainer,
            transition
        )
        constraintSet.applyTo(binding.fragmentYesterdayStartContainer)
    }

    private fun hideInfo() {
        isShow = false
        val constraintSet = ConstraintSet()
        constraintSet.clone(context, R.layout.fragment_yesterday_start)
        val transition = ChangeBounds()
        transition.interpolator = AnticipateOvershootInterpolator(5.0f)
        transition.duration = 1000
        TransitionManager.beginDelayedTransition(
            binding.fragmentYesterdayStartContainer,
            transition
        )
        constraintSet.applyTo(binding.fragmentYesterdayStartContainer)
    }

    private fun yesterdayDate(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1)
        return formatter.format(calendar.time)
    }

    private fun renderData(state: AppState) {
        when (state) {
            is AppState.Success -> {
                val serverResponseData = state.serverResponseData
                val url = serverResponseData.url
                if (url.isNullOrEmpty()) {
                    toast("Url is empty")
                } else {
                    binding.yesterdayImageView.load(url) {
                        lifecycle(this@YesterdayFragment)
                        error(R.drawable.ic_load_error)
                        placeholder(R.drawable.ic_no_photo)
                    }
                    binding.yesterdayTitleTextView.text = serverResponseData.title
                }
            }
            is AppState.Loading -> {
            }
            is AppState.Error -> {
                toast(state.error.message)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = YesterdayFragment()
    }
}