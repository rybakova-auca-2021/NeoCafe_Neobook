package com.example.neocafe.api

import com.example.neocafe.model.Branch
import com.example.neocafe.model.DetailOrder
import com.example.neocafe.model.NotificationRequest
import com.example.neocafe.model.GetOrder
import com.example.neocafe.model.Product
import com.example.neocafe.model.ProductCategory
import com.example.neocafe.model.ProductDetail
import com.example.neocafe.model.Promotion
import com.example.neocafe.model.PromotionDetail
import com.example.neocafe.model.Question
import com.example.neocafe.model.QuestionAnswer
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
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

    @POST("orders/")
    fun postOrders(
        @Body request: GetOrder
    ): Call<List<GetOrder>>

    @GET("my-orders/")
    fun getOrders(): Call<List<GetOrder>>

    @GET("orders/{order_number}/")
    fun getOrderById(
        @Path("id") id: Int
    ): Call<DetailOrder>

    @GET("question-list/")
    fun getQuestions() : Call<List<Question>>

    @GET("question-answer/{id}/")
    fun getQuestionAnswers(
        @Path("id") id: Int
    ) : Call<QuestionAnswer>
}