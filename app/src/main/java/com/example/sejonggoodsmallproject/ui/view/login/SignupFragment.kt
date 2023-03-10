package com.example.sejonggoodsmallproject.ui.view.login

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.sejonggoodsmallproject.R
import com.example.sejonggoodsmallproject.data.model.SignupPost
import com.example.sejonggoodsmallproject.databinding.FragmentSignupBinding
import com.example.sejonggoodsmallproject.util.RetrofitInstance.retrofitService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignupFragment : Fragment() {
    private var _binding : FragmentSignupBinding? = null
    private val binding get() = _binding!!
    private lateinit var callback: OnBackPressedCallback
    private var emailFlag = false
    private var passFlag = false
    private var nameFlag = false
    private var birthFlag = false
    private var checkBoxFlag = false
    private var passFlagLiveData : MutableLiveData<Int> = MutableLiveData()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSignupBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSignupTextWatcher()

        passFlagLiveData.observe(viewLifecycleOwner) {
            if (it == 1) {
                passFlag = true
            } else if (it == 0) {
                passFlag = false
            }
        }

        binding.checkboxSignupTerms.setOnCheckedChangeListener { compoundButton, isCheckedd ->
            if (isCheckedd) {
                checkBoxFlag = true
                setSignupBtnFlag()
            } else {
                checkBoxFlag = false
                setSignupBtnFlag()
            }
        }

        binding.tvShowTerms.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.horizon_enter_front, 0)
                .add(R.id.init_container,TermsFragment())
                .commit()
        }
        binding.btnBackSignup.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .setCustomAnimations(0, R.anim.horizon_exit_front)
                .remove(this@SignupFragment)
                .commit()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().supportFragmentManager.beginTransaction()
                    .setCustomAnimations(0, R.anim.horizon_exit_front)
                    .remove(this@SignupFragment)
                    .commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun setSignupTextWatcher() {
        // ????????? ?????? ??????
        binding.etEmail.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.etEmail.text.contains('@')) {
                    binding.tvWarnEmail.visibility = View.INVISIBLE
                    binding.ivCheckEmail.visibility = View.VISIBLE
                    emailFlag = true
                } else {
                    binding.tvWarnEmail.visibility = View.VISIBLE
                    binding.ivCheckEmail.visibility = View.INVISIBLE
                    emailFlag = false
                }
                setSignupBtnFlag()
            }
            override fun afterTextChanged(p0: Editable?) { }
        })
        // ???????????? ?????? ??????
        binding.etPassword.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.etPassword.text.length >= 8) {
                    binding.tvWarnPassword.visibility = View.INVISIBLE
                    binding.ivCheckPassword.visibility = View.VISIBLE
                    if (binding.etPasswordConfirm.text.isEmpty()) {
                        binding.tvWarnPassword.visibility = View.INVISIBLE
                        binding.ivCheckPassword.visibility = View.VISIBLE
                        passFlagLiveData.value = 1
                    } else if (binding.etPassword.text.toString() == binding.etPasswordConfirm.text.toString()) {
                        binding.tvWarnPassword.visibility = View.INVISIBLE
                        binding.ivCheckPassword.visibility = View.VISIBLE
                        passFlagLiveData.value = 1
                    } else {
                        binding.tvWarnPassword.visibility = View.VISIBLE
                        binding.ivCheckPassword.visibility = View.INVISIBLE
                        passFlagLiveData.value = 0
                    }
                } else {
                    binding.tvWarnPassword.visibility = View.VISIBLE
                    binding.ivCheckPassword.visibility = View.INVISIBLE
                    passFlagLiveData.value = 0
                }
                setSignupBtnFlag()
            }
            override fun afterTextChanged(p0: Editable?) { }
        })
        // ???????????? ?????? ?????? ??????
        binding.etPasswordConfirm.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.etPassword.text.toString() == binding.etPasswordConfirm.text.toString()) {
                    binding.tvWarnPasswordConfirm.visibility = View.INVISIBLE
                    binding.ivCheckPasswordConfirm.visibility = View.VISIBLE
                    passFlagLiveData.value = 1
                } else {
                    binding.tvWarnPasswordConfirm.visibility = View.VISIBLE
                    binding.ivCheckPasswordConfirm.visibility = View.INVISIBLE
                    passFlagLiveData.value = 0
                }
                setSignupBtnFlag()
            }
            override fun afterTextChanged(p0: Editable?) { }
        })
        // ?????? ?????? ??????
        binding.etName.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                nameFlag = binding.etName.text.isNotEmpty()
                setSignupBtnFlag()
            }
        })
        // ???????????? ?????? ??????
        binding.etBirth.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val birthArray = binding.etBirth.text.toString().split("-")
                if (birthArray.size == 3 && binding.etBirth.text.toString().length == 10) {
                    if (birthArray[0].length == 4 && birthArray[1].length == 2 && birthArray[2].length == 2) {
                        birthFlag = true
                    }
                } else {
                    birthFlag = false
                }
                setSignupBtnFlag()
            }
            override fun afterTextChanged(p0: Editable?) { }
        })
    }

    private fun setSignupBtnFlag() {
        // ???????????? ?????? ?????????
        if (emailFlag && passFlag && nameFlag && birthFlag && checkBoxFlag
            && binding.etPassword.text.toString() == binding.etPasswordConfirm.text.toString()) {
            binding.btnSignupComplete.setBackgroundResource(R.drawable.background_rec_10dp_red_stroke_red_solid)

            binding.btnSignupComplete.setOnClickListener {
                // ???????????? ????????? ?????????
                val email = binding.etEmail.text.toString()
                val password = binding.etPassword.text.toString()
                val username = binding.etName.text.toString()
                val birth = binding.etBirth.text.toString()

                CoroutineScope(Dispatchers.IO).launch {
                    val response = retrofitService.authSignup(SignupPost(email, password, username, birth))

                    withContext(Dispatchers.Main) {
                        when(response.code()) {
                            200 -> {
                                requireActivity().supportFragmentManager.beginTransaction().remove(this@SignupFragment).commit()
                                Toast.makeText(requireContext(), "??????????????? ?????????????????????.",Toast.LENGTH_SHORT).show()
                            }
                            400 -> {
                                Toast.makeText(requireContext(), "?????? ????????? ??????????????????.",Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        } else {
            binding.btnSignupComplete.setBackgroundResource(R.drawable.background_rec_10dp_grey_solid)
            binding.btnSignupComplete.setOnClickListener {
                if (!birthFlag) {
                    Toast.makeText(requireContext(), "???????????? ????????? YYYY-MM-DD ?????????.",Toast.LENGTH_SHORT).show()
                } else if (!passFlag) {
                    Toast.makeText(requireContext(), "??????????????? ??????????????????.",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}