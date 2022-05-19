package by.geekbrains.pictureseveryday.view.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.geekbrains.pictureseveryday.R
import by.geekbrains.pictureseveryday.databinding.FragmentMainPictureBinding
import by.geekbrains.pictureseveryday.view.MainActivity
import by.geekbrains.pictureseveryday.view.details.SettingsFragment
import by.geekbrains.pictureseveryday.viewmodel.AppState
import by.geekbrains.pictureseveryday.viewmodel.MainPictureViewModel
import coil.api.load
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior

class MainPictureFragment : Fragment() {

    private var _binding: FragmentMainPictureBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainPictureViewModel by lazy {
        ViewModelProvider(this)[MainPictureViewModel::class.java]
    }

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var bottomSheetHeader: TextView
    private lateinit var bottomSheetContent: TextView

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
        setBottomAppBar(view)
        binding.textInputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data =
                    Uri.parse("https://en.wikipedia.org/wiki/${binding.textInputEditText.text.toString()}")
            })
        }
        setBottomSheetBehaviour(view.findViewById(R.id.bottom_sheet_container))
        bottomSheetHeader = view.findViewById(R.id.description_header_text_view_bottom_sheet)
        bottomSheetContent = view.findViewById(R.id.description_text_view_bottom_sheet)
        viewModel.getLiveData()
            .observe(viewLifecycleOwner) { renderData(it) }
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
                    bottomSheetHeader.text = serverResponseData.title
                    bottomSheetContent.text = serverResponseData.explanation
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

    private fun setBottomSheetBehaviour(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_DRAGGING -> toast("STATE_DRAGGING")
                    BottomSheetBehavior.STATE_COLLAPSED -> toast("STATE_COLLAPSED")
                    BottomSheetBehavior.STATE_EXPANDED -> toast("STATE_EXPANDED")
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> toast("STATE_HALF_EXPANDED")
                    BottomSheetBehavior.STATE_HIDDEN -> toast("STATE_HIDDEN")
                    BottomSheetBehavior.STATE_SETTLING -> toast("STATE_SETTLING")
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_bar_screens, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_favorite -> toast(getString(R.string.favourite))
            R.id.app_bar_settings -> activity?.apply {
                this.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.settings_fragment_container, SettingsFragment())
                    .addToBackStack("")
                    .commit()
            }
            android.R.id.home -> {
                activity?.let {
                    BottomNavigationDrawerFragment().show(it.supportFragmentManager, "")
                }
            }
            R.id.app_bar_search -> toast(getString(R.string.search))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setBottomAppBar(view: View) {
        val context = activity as MainActivity
        context.setSupportActionBar(view.findViewById(R.id.bottom_app_bar))
        setHasOptionsMenu(true)
        binding.mainFab.setOnClickListener {
            if (isMain) {
                isMain = false
                with(binding) {
                    bottomAppBar.navigationIcon = null
                    bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                    mainFab.setImageDrawable(ContextCompat.getDrawable(context,
                        R.drawable.ic_baseline_arrow_back))
                    bottomAppBar.replaceMenu(R.menu.menu_search_bottom_bar)
                }
            } else {
                isMain = true
                with(binding) {
                    bottomAppBar.navigationIcon = ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_menu_bottom_bar
                    )
                    bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                    mainFab.setImageDrawable(ContextCompat.getDrawable(context,
                        R.drawable.ic_baseline_add_24))
                    bottomAppBar.replaceMenu(R.menu.menu_bottom_bar_screens)
                }
            }
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