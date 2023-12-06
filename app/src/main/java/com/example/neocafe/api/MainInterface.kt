package com.example.neocafe.api

import com.example.neocafe.model.Branch
import com.example.neocafe.model.Product
import com.example.neocafe.model.ProductCategory
import com.example.neocafe.model.ProductDetail
import com.example.neocafe.model.Promotion
import com.example.neocafe.model.PromotionDetail
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MainInterface {
    @GET("branch-list/")
    fun getBranches(): Call<List<Branch>>

    @GET("product-categories/")
    fun getProductCategory(): Call<ProductCategory>

    @GET("products/")
    fun getProducts(
        @Query("category_name") category: String? = null,
        @Query("ordering") ordering: String? = null,
        @Query("search") search: String? = null,
        @Query("limit") limit: Int? = null
    ): Call<List<Product>>

    @GET("products/{id}/")
    fun getProductDetail(
        @Query("id") id: Int,
    ): Call<ProductDetail>

    @GET("promotions/")
    fun getPromotions(): Call<List<Promotion>>

    @GET("promotions/{id}/")
    fun getPromotionDetail(
        @Path("id") id: Int,
    ): Call<PromotionDetail>
}