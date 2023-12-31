package com.example.neocafe.api

import com.example.neocafe.model.Branch
import com.example.neocafe.model.DetailOrder
import com.example.neocafe.model.NotificationRequest
import com.example.neocafe.model.GetOrder
import com.example.neocafe.model.Notification
import com.example.neocafe.model.NotificationDetail
import com.example.neocafe.model.Order
import com.example.neocafe.model.OrderConfirm
import com.example.neocafe.model.Product
import com.example.neocafe.model.ProductCategory
import com.example.neocafe.model.ProductDetail
import com.example.neocafe.model.Promotion
import com.example.neocafe.model.PromotionDetail
import com.example.neocafe.model.Question
import com.example.neocafe.model.QuestionAnswer
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MainInterface {
    @GET("branch-list/")
    fun getBranches(): Call<List<Branch>>

    @GET("product-categories/")
    fun getProductCategory(): Call<List<ProductCategory>>

    @GET("popular-products/")
    fun getPopularProducts(
        @Query("search") search: String? = null
    ): Call<List<Product>>

    @GET("products/")
    fun getProducts(
        @Query("category_name") category: String? = null,
        @Query("ordering") ordering: String? = null,
        @Query("search") search: String? = null,
        @Query("limit") limit: Int? = null
    ): Call<List<Product>>

    @GET("products/{id}/")
    fun getProductDetail(
        @Path("id") id: Int,
    ): Call<ProductDetail>

    @GET("promotions/")
    fun getPromotions(): Call<List<Promotion>>

    @GET("promotions/{id}/")
    fun getPromotionDetail(
        @Path("id") id: Int,
    ): Call<PromotionDetail>

    @POST("send-notification/")
    fun sendNotification(
        @Body request: NotificationRequest
    ): Call<NotificationRequest>

    @GET("notification/")
    fun getNotification(): Call<List<Notification>>

    @DELETE("notification/delete/{id}/")
    fun deleteNotification(@Path("id") id: Int): Call<Unit>

    @DELETE("notification/delete/all/")
    fun deleteNotification(): Call<Unit>

    @GET("notification/{id}/")
    fun getNotificationById(@Path("id") id: Int): Call<NotificationDetail>

    @POST("orders/")
    fun postOrders(
        @Header("Authorization") token: String,
        @Body request: Order
    ): Call<OrderConfirm>

    @GET("my-orders/")
    fun getOrders(@Header("Authorization") token: String): Call<List<GetOrder>>

    @GET("orders/{order_number}/")
    fun getOrderById(
        @Header("Authorization") token: String,
        @Path("order_number") id: Int
    ): Call<DetailOrder>

    @GET("question-list/")
    fun getQuestions() : Call<List<Question>>

    @GET("question-answer/{id}/")
    fun getQuestionAnswers(
        @Path("id") id: Int
    ) : Call<QuestionAnswer>
}