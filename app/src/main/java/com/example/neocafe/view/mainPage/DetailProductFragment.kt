package com.example.neocafe.view.mainPage

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.neocafe.MainActivity
import com.example.neocafe.R
import com.example.neocafe.databinding.FragmentDetailProductBinding
import com.example.neocafe.model.ProductDetail
import com.example.neocafe.room.MyApplication
import com.example.neocafe.room.Product
import com.example.neocafe.room.ProductDao
import com.example.neocafe.viewModel.GetProductDetailViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DetailProductFragment : Fragment() {
    private lateinit var binding: FragmentDetailProductBinding
    private val viewModel: GetProductDetailViewModel by viewModels()
    private val productDao: ProductDao by lazy {
        (requireActivity().application as MyApplication).database.productDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailProductBinding.inflate(inflater, container, false)
        (requireActivity() as MainActivity).hideBtmNav()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = arguments?.getInt("id")
        if (id != null) {
            getProductDetail(id)
        }
        setupNavigation()
    }

    private fun setupNavigation() {
        binding.btnBack.setOnClickListener {
            val sourceFragment = arguments?.getString("sourceFragment") ?: ""
            when (sourceFragment) {
                "popularPageFragment" -> findNavController().navigate(R.id.action_detailProductFragment_to_popularPageFragment)
                "mainPageFragment" -> findNavController().navigate(R.id.action_detailProductFragment_to_mainFragment)
                "menuPageFragment" -> findNavController().navigate(R.id.action_detailProductFragment_to_menuFragment)
                else -> findNavController().popBackStack()
            }
        }
    }

    private suspend fun insertItem(id: Int, title: String, category: Int, image: String, quantity: Int, price: String) {
        val cartItem = Product(
            id,
            title,
            category,
            image,
            quantity,
            price
        )
        productDao.insertCartItem(cartItem)
    }

    private fun setTotalPrice(productDetail: ProductDetail, quantity: Int) {
        val price = productDetail.price.toDouble() // Assuming price is in String format
        val totalPrice = price * quantity
        binding.productPrice.text = totalPrice.toString()
    }


    private fun getProductDetail(id: Int) {
        viewModel.getProductDetail(id, onSuccess = { productDetail ->
            updateProductDetails(productDetail)
            setupAddToCartButton(productDetail)
        })
    }

    private fun updateProductDetails(productDetail: ProductDetail) {
        with(binding) {
            detailTitle.text = productDetail.title
            detailDesc.text = productDetail.description
            detailPrice.text = productDetail.price
            Glide.with(detailImg).load(productDetail.image).into(detailImg)
        }
    }

    private fun setupAddToCartButton(productDetail: ProductDetail) {
        with(binding) {
            btnAddToCart.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    productDetail.image?.let { it1 ->
                        insertItem(
                            productDetail.id,
                            productDetail.title,
                            productDetail.category,
                            it1,
                            productDetail.quantity,
                            productDetail.price
                        )
                    }
                }
                btnAddToCart.visibility = View.GONE
                cardAddedToCard.visibility = View.VISIBLE
                productQuantity.text = "1"
                setTotalPrice(productDetail, productDetail.quantity)

                setupPlusButton(productDetail)
                setupMinusButton(productDetail)
            }
        }
    }

    private fun setupPlusButton(productDetail: ProductDetail) {
        binding.btnPlus.setOnClickListener {
            val currentQuantity = binding.productQuantity.text.toString().toInt()
            val newQuantity = currentQuantity + 1
            binding.productQuantity.text = newQuantity.toString()

            CoroutineScope(Dispatchers.IO).launch {
                productDetail.id?.let {
                    productDao.increaseProductQuantity(it, 1)
                }
            }
            setTotalPrice(productDetail, newQuantity)
        }
    }

    private fun setupMinusButton(productDetail: ProductDetail) {
        binding.btnMinus.setOnClickListener {
            val currentQuantity = binding.productQuantity.text.toString().toInt()
            val newQuantity = currentQuantity - 1
            if (newQuantity >= 0) {
                binding.productQuantity.text = newQuantity.toString()

                CoroutineScope(Dispatchers.IO).launch {
                    productDetail.id?.let {
                        productDao.decreaseProductQuantity(it, 1)
                    }
                }

                setTotalPrice(productDetail, newQuantity)

                handleQuantityZero(productDetail, newQuantity)
            }
        }
    }

    private fun handleQuantityZero(productDetail: ProductDetail, newQuantity: Int) {
        if (newQuantity == 0) {
            CoroutineScope(Dispatchers.IO).launch {
                productDetail.id?.let {
                    productDao.deleteProductById(it)
                }
            }
            with(binding) {
                cardAddedToCard.visibility = View.GONE
                btnAddToCart.visibility = View.VISIBLE
                cardDialog.visibility = View.VISIBLE
                Handler(Looper.getMainLooper()).postDelayed({
                    cardDialog.visibility = View.GONE
                }, 3000)
            }
        }
    }

}