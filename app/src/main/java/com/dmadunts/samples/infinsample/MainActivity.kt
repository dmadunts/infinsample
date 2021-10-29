package com.dmadunts.samples.infinsample

//Используя Github API сделать страницу авторизации в гитхабе с обработкой ошибок.
//После успешной авторизации переходить на окно поиска.
//Окно поиска должно содержать инпут и при вводе текста отображать в списке найденные репозитории( repos). Поиск должен проводиться по всем репозиториям без ограничений.
//В списке должны отображаться:
//полное название репозитория, аватар владельца, язык разработки.
//Список должен пагинироваться.
//По нажатию на элемент списка, должна открываться страница браузера с этим репозиторием
//Использовать MVVM, Dagger2, Glide, Retrofit2 (для iOS не нужно)
//
//Ориентировочное время на выполнение: 12 часов.

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.dmadunts.samples.infinsample.databinding.ActivityMainBinding
import com.dmadunts.samples.infinsample.persistence.getAccessToken
import com.dmadunts.samples.infinsample.utils.Constants

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var broadcastReceiver: BroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action.equals(Constants.ACTION_LOGOUT)) {
                    navController.navigate(R.id.loginDest)
                    navController.popBackStack(R.id.loginDest, false)
                }
            }
        }
        registerReceiver(broadcastReceiver, IntentFilter(Constants.ACTION_LOGOUT))
        setupNavigation()
    }

    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController
        val graphInflater = navController.navInflater
        val navGraph = graphInflater.inflate(R.navigation.nav_graph)
        navGraph.startDestination =
            if (getAccessToken() == null) R.id.introDest else R.id.searchDest
        navController.graph = navGraph
    }

    override fun onDestroy() {
        super.onDestroy()
        broadcastReceiver?.let {
            unregisterReceiver(it)
        }
    }
}