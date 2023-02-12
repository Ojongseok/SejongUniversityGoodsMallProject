package com.example.sejonggoodsmallproject.ui.view.search

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import com.example.sejonggoodsmallproject.R
import com.example.sejonggoodsmallproject.databinding.FragmentSearchBinding
import com.example.sejonggoodsmallproject.databinding.FragmentSearchResultBinding
import com.example.sejonggoodsmallproject.ui.view.home.ProductListAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchResultFragment : Fragment() {
    private var _binding : FragmentSearchResultBinding? = null
    private val binding get() = _binding!!
    private lateinit var callback: OnBackPressedCallback
    private lateinit var searchResultListAdapter : ProductListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSearchResultBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideKeyboard()

        binding.btnSearchResultBack.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .setCustomAnimations(0, R.anim.horizon_exit_front)
                .remove(this@SearchResultFragment)
                .commit()
        }

        binding.etSearchResultBar.text = arguments?.getString("searchKeyWord","")

        setRvSearchResult()
    }

    private fun setRvSearchResult() {
        searchResultListAdapter = ProductListAdapter(requireContext(), emptyList())

        CoroutineScope(Dispatchers.IO).launch {

        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().supportFragmentManager.beginTransaction()
                    .setCustomAnimations(0, R.anim.horizon_exit_front)
                    .remove(this@SearchResultFragment)
                    .commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun hideKeyboard() {
        if (requireActivity().currentFocus != null) {
            // 프래그먼트기 때문에 getActivity() 사용
            val inputManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(requireActivity().currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}