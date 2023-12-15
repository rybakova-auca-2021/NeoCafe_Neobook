package com.example.neocafe.viewModel

import androidx.lifecycle.ViewModel
import com.example.neocafe.api.RetrofitInstance
import com.example.neocafe.model.Question
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetQuestionsViewModel : ViewModel() {
    private val apiInterface = RetrofitInstance.mainApi
    fun getAllQuestion(
        onSuccess: (List<Question>) -> Unit
    ) {
        apiInterface.getQuestions()
            .enqueue(getCallback(onSuccess))
    }

    private fun getCallback(onSuccess: (List<Question>) -> Unit): Callback<List<Question>> {
        return object : Callback<List<Question>> {
            override fun onResponse(call: Call<List<Question>>, response: Response<List<Question>>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        onSuccess.invoke(body)
                    }
                } else {
                    println("Request failed with status code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Question>>, t: Throwable) {
                println("Request failed: ${t.message}")
            }
        }
    }
}