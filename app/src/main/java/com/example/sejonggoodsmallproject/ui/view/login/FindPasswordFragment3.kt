package com.example.sejonggoodsmallproject.ui.view.login

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.example.sejonggoodsmallproject.R
import com.example.sejonggoodsmallproject.data.model.FindPasswordPost2
import com.example.sejonggoodsmallproject.data.model.FindPasswordPost3
import com.example.sejonggoodsmallproject.databinding.FragmentFindPassword2Binding
import com.example.sejonggoodsmallproject.databinding.FragmentFindPassword3Binding
import com.example.sejonggoodsmallproject.util.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FindPasswordFragment3 : Fragment() {
    private var _binding : FragmentFindPassword3Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFindPassword3Binding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideKeyboard()

        binding.btnUpdatePassComplete.setOnClickListener {
            if (binding.etUpdatePassword1.text.length>=8
                && (binding.etUpdatePassword1.text.toString() == binding.etUpdatePassword2.text.toString())) {
                val newPassword = binding.etUpdatePassword1.text.toString()
                CoroutineScope(Dispatchers.IO).launch {
                    val response = RetrofitInstance.retrofitService
                        .updatePassword(FindPasswordPost3(arguments?.getString("email").toString(), newPassword))

                    withContext(Dispatchers.Main) {
                        if (response.code() == 200) {
                            Toast.makeText(requireContext(), "???????????? ????????? ?????????????????????.", Toast.LENGTH_SHORT).show()

                            requireActivity().supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                        }
                    }
                }
            } else {
                Toast.makeText(requireContext(), "????????? ??????????????? ??????????????????.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun hideKeyboard() {
        if (requireActivity().currentFocus != null) {
            // ?????????????????? ????????? getActivity() ??????
            val inputManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(requireActivity().currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}