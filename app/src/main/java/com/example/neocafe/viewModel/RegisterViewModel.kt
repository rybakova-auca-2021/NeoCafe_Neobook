import androidx.lifecycle.ViewModel
import com.example.neocafe.api.RetrofitInstance
import com.example.neocafe.constants.Utils
import com.example.neocafe.model.RegisterUser
import com.example.neocafe.model.RegisterUserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {
    fun register(
        name: String,
        phone: String,
        onSuccess: () -> Unit,
        onError:() -> Unit
    ) {
        val request = RegisterUser(name, phone)
        val apiInterface = RetrofitInstance.authApi

        val call = apiInterface.registerUser(request)
        call.enqueue(object : Callback<RegisterUserResponse> {
            override fun onResponse(
                call: Call<RegisterUserResponse>,
                response: Response<RegisterUserResponse>
            ) {
                if (response.isSuccessful) {
                    onSuccess.invoke()
                    val responseBody = response.body()
                    if (responseBody != null) {
                        println("UserIdResponse: ${responseBody.user.id}")
                        Utils.userId = responseBody.user.id
                        println("UserIdModel: ${Utils.userId}")
                    }

                } else {
                    onError.invoke()
                    println("Request failed with status code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<RegisterUserResponse>, t: Throwable) {
                println("Request failed: ${t.message}")                }
        })
    }
}