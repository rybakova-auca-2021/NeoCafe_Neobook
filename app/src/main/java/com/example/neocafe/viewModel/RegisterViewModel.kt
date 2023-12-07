import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.neocafe.api.RetrofitInstance
import com.example.neocafe.constants.LocalStorageProvider
import com.example.neocafe.constants.Utils
import com.example.neocafe.model.RegisterUser
import com.example.neocafe.model.RegisterUserResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class RegisterViewModel : ViewModel() {
    fun register(
        context: Context,
        name: String,
        phone: String,
        qrCode: String,
        onSuccess: () -> Unit,
        onError:() -> Unit
    ) {
        val apiInterface = RetrofitInstance.authApi

        val imageFile: File? = qrCode.let { LocalStorageProvider.getFile(context, Uri.parse(it)) }

        val textPart = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val phonePart = phone.toRequestBody("text/plain".toMediaTypeOrNull())
        val imagePart = imageFile?.asRequestBody("image/png".toMediaTypeOrNull())?.let {
            MultipartBody.Part.createFormData("qr_code", imageFile.name, it)
        }

        val call = apiInterface.registerUser(textPart, phonePart, imagePart)
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