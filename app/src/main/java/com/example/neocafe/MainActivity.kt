package com.example.neocafe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.hostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(navController)

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.btn_main -> {
                    navController.navigate(R.id.mainPageFragment)
                    true
                }

                R.id.btn_menu -> {
                    navController.navigate(R.id.menuFragment)
                    true
                }

                R.id.btn_basket -> {
                    navController.navigate(R.id.basketFragment)
                    true
                }

                R.id.btn_info -> {
                    navController.navigate(R.id.infoFragment)
                    true
                }

                else -> false
            }
        }
    }
    fun hideBtmNav() {
        val navBar = findViewById<View>(R.id.bottomNavigationView)
        navBar.visibility = View.GONE
    }

    fun showBtmNav() {
        val navBar = findViewById<View>(R.id.bottomNavigationView)
        navBar.visibility = View.VISIBLE
    }
}