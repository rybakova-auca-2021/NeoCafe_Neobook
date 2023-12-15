package com.example.neocafe.viewModel

import androidx.lifecycle.ViewModel
import com.example.neocafe.api.RetrofitInstance
import com.example.neocafe.model.QuestionAnswer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetQuestionAnswerViewModel : ViewModel() {
    private val apiInterface = RetrofitInstance.mainApi
    fun getQuestionAnswer(
        id: Int,
        onSuccess: (QuestionAnswer) -> Unit
    ) {
        apiInterface.getQuestionAnswers(id)
            .enqueue(getCallback(onSuccess))
    }

    private fun getCallback(onSuccess: (QuestionAnswer) -> Unit): Callback<QuestionAnswer> {
        return object : Callback<QuestionAnswer> {
            override fun onResponse(call: Call<QuestionAnswer>, response: Response<QuestionAnswer>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        onSuccess.invoke(body)
                    }
                } else {
                    println("Request failed with status code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<QuestionAnswer>, t: Throwable) {
                println("Request failed: ${t.message}")
            }
        }
    }
}