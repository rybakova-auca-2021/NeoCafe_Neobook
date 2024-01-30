package com.example.neocafe.login.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.neocafe.R
import com.example.neocafe.constants.Utils
import com.example.neocafe.databinding.FragmentLoginBinding
import com.example.neocafe.view.registration.SendCodeFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import okhttp3.internal.EMPTY_BYTE_ARRAY
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BottomSheetDialogFragment() {
	
	private var _binding: FragmentLoginBinding? = null
	private val binding: FragmentLoginBinding get() = _binding!!
	
	private val viewModel: LoginViewModel by viewModel()
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentLoginBinding.inflate(inflater, container, false)
		return binding.root
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setupNavigation()
		setupValidation()
		val phone = binding.etPhone.text.toString()
		
		//todo: never do this!!!!
		Utils.phone = phone
	}
	
	private fun setupNavigation() {
		binding.btnContinue.setOnClickListener {
			login()
		}
		binding.imageView5.setOnClickListener {
			dismiss()
			findNavController().navigate(R.id.profileFragment)
		}
	}
	
	private fun login() {
		val phone = binding.etPhone.text.toString()
		viewModel.login(phone)
		viewModel.result.observe(viewLifecycleOwner) { result ->
			result.onSuccess {
				dismiss()
				val bottomSheetFragment = SendCodeFragment()
				bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
			}.onFailure {
				binding.errorMsg.visibility = View.VISIBLE
				binding.etPhone.setTextColor(resources.getColor(R.color.main_orange));
			}
		}
	}
	
	private fun setupValidation() {
		val textWatchers = arrayOf(
			binding.etPhone
		)
		for (watchedText in textWatchers) {
			watchedText.addTextChangedListener(object : TextWatcher {
				override fun beforeTextChanged(
					s: CharSequence?,
					start: Int,
					count: Int,
					after: Int,
				) {
				}
				
				@SuppressLint("ResourceAsColor")
				override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
					val phone = binding.etPhone.text.toString()
					binding.btnContinue.isEnabled = phone.isNotEmpty()
					
					val whiteColor = resources.getColor(R.color.white)
					binding.btnContinue.setTextColor(whiteColor)
					
				}
				
				override fun afterTextChanged(s: Editable?) {
				}
			})
		}
	}
}