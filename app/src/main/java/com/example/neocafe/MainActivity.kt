package com.example.neocafe

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.neocafe.login.di.loginModule
import com.example.neocafe.viewModel.NotificationViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import org.koin.core.context.GlobalContext.startKoin

class MainActivity : AppCompatActivity() {
	private lateinit var bottomNavigationView: BottomNavigationView
	private val notificationViewModel: NotificationViewModel by viewModels()
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		startKoin {
			modules(loginModule)
		}
		
		setContentView(R.layout.activity_main)
		
		FirebaseApp.initializeApp(this)
		
		FirebaseMessaging.getInstance().token
			.addOnCompleteListener { task ->
				if (task.isSuccessful && task.result != null) {
					val token = task.result
					val title = "Горячие акции на горячие напитки"
					val body =
						"Дорогие наши гости рады вам сообщить что в такой холодную погоду у нас идет акции на горячие напитки"
					Log.d(ContentValues.TAG, "FCM Token: $token")
					
					notificationViewModel.sendNotification(
						title, body, token
					)
				} else {
					Log.w(ContentValues.TAG, "Error getting FCM token", task.exception)
				}
			}
		
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