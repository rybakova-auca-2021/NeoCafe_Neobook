package com.example.neocafe.view.registration

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.neocafe.MainActivity
import com.example.neocafe.R
import com.example.neocafe.constants.Utils
import com.example.neocafe.databinding.FragmentProfileInfoBinding
import com.example.neocafe.viewModel.GetProfileInfoViewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ProfileInfoFragment : Fragment() {
    private lateinit var binding: FragmentProfileInfoBinding
    private var selectedDate: String? = null
    private val viewModel: GetProfileInfoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileInfoBinding.inflate(inflater, container, false)
        (requireActivity() as MainActivity).hideBtmNav()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigation()
        setupValidation()
        getUserData()
    }

    private fun setupNavigation() {
        binding.etBirth.setOnClickListener {
            showDatePicker()
        }
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.mainPageFragment)
        }
        binding.btnLogout.setOnClickListener {
            logout()
        }
        binding.btnSave.setOnClickListener {
            changeUserInfo()
        }
    }

    private fun logout() {
        showDialog()
    }

    private fun changeUserInfo() {
        val name = binding.etName.text.toString()
        val birth = binding.etBirth.text.toString()
        val phone = binding.etPhone.text.toString()
        val mail = binding.etMail.text.toString()
        viewModel.updateProfile(name, birth, mail, phone, onSuccess = {findNavController().navigate(R.id.mainPageFragment)})
    }

    private fun getUserData() {
        viewModel.getProfile { profileData ->
            profileData?.let {
                val name = profileData.first_name ?: ""
                val birth = profileData.birth_date ?: ""
                val phone = profileData.phone ?: ""
                val email = profileData.email ?: ""

                val nameEditable =
                    Editable.Factory.getInstance().newEditable(name)
                val birthEditable =
                    Editable.Factory.getInstance().newEditable(birth)
                val phoneEditable =
                    Editable.Factory.getInstance().newEditable(phone)
                val emailEditable =
                    Editable.Factory.getInstance().newEditable(email)

                binding.etName.text = nameEditable
                binding.etPhone.text = phoneEditable
                binding.etMail.text = emailEditable

                selectedDate = birth
                binding.etBirth.text = birthEditable
            }
        }
    }

    private fun showDatePicker() {
        val currentDate = selectedDate?.toDate() ?: Calendar.getInstance().timeInMillis
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = currentDate

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            R.style.MyDatePickerStyle,
            { _, year, monthOfYear, dayOfMonth ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(year, monthOfYear, dayOfMonth)
                selectedDate = selectedCalendar.timeInMillis.toString()

                val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedCalendar.time)
                binding.etBirth.text = Editable.Factory.getInstance().newEditable(formattedDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }



    fun String.toDate(): Long {
        try {
            val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val date = sdf.parse(this)
            return date?.time ?: 0
        } catch (e: ParseException) {
            e.printStackTrace()
            return 0
        }
    }



    private fun setupValidation() {
        val textWatchers = arrayOf(
            binding.etName,
            binding.etBirth,
            binding.etPhone,
            binding.etMail
        )
        for (watchedText in textWatchers) {
            watchedText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                @SuppressLint("ResourceAsColor")
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val name = binding.etName.text.toString()
                    val phone = binding.etPhone.text.toString()
                    val birth = binding.etBirth.text.toString()
                    val mail = binding.etMail.text.toString()
                    binding.btnSave.isEnabled = name.isNotEmpty() && phone.isNotEmpty() && birth.isNotEmpty() && mail.isNotEmpty()

                    val whiteColor = resources.getColor(com.example.neocafe.R.color.white)
                    binding.btnSave.setTextColor(whiteColor)

                }

                override fun afterTextChanged(s: Editable?) {
                }
            })
        }
    }

    private fun showDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_logout, null)
        val myDialog = Dialog(requireContext())
        myDialog.setContentView(dialogView)

        myDialog.setCancelable(true)
        myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myDialog.show()

        val closeBtn = myDialog.findViewById<TextView>(R.id.btn_logout)
        val logoutBtn = myDialog.findViewById<TextView>(R.id.btn_logout)

        closeBtn.setOnClickListener {
            myDialog.dismiss()
        }
        logoutBtn.setOnClickListener {
            myDialog.dismiss()
            Utils.access = null
            findNavController().navigate(R.id.profileFragment)
        }

        dialogView.setOnClickListener {
            myDialog.dismiss()
        }
    }
}