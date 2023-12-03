package com.example.neocafe.viewModel

import androidx.lifecycle.ViewModel
import com.example.neocafe.api.RetrofitInstance
import com.example.neocafe.model.Branch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetBranchesViewModel : ViewModel() {
    private val apiInterface = RetrofitInstance.mainApi
    fun getAllBranches(
        onSuccess: (List<Branch>) -> Unit
    ) {
        apiInterface.getBranches()
            .enqueue(getCallback(onSuccess))
    }

    private fun getCallback(onSuccess: (List<Branch>) -> Unit): Callback<List<Branch>> {
        return object : Callback<List<Branch>> {
            override fun onResponse(call: Call<List<Branch>>, response: Response<List<Branch>>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        onSuccess.invoke(body)
                    }
                } else {
                    println("Request failed with status code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Branch>>, t: Throwable) {
                println("Request failed: ${t.message}")
            }
        }
    }
}