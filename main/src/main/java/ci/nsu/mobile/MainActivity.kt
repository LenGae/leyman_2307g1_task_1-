package ci.nsu.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import androidx.compose.runtime.*

import ci.nsu.mobile.di.ServiceLocator
import ci.nsu.mobile.ui.viewmodel.AppViewModelFactory
import ci.nsu.mobile.ui.viewmodel.AuthViewModel
import ci.nsu.mobile.ui.viewmodel.DepositViewModel
import ci.nsu.mobile.utils.TokenManager

class MainActivity : ComponentActivity() {

    private lateinit var serviceLocator: ServiceLocator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        TokenManager.init(this)
        serviceLocator = ServiceLocator(this)

        setContent {

            val navController = rememberNavController()

            val authVm = remember { AuthViewModel() }

            val depositVm: DepositViewModel = viewModel(
                factory = AppViewModelFactory(
                    serviceLocator.depositRepository
                )
            )

            val startDestination =
                if (TokenManager.token != null) "main" else "login"

            NavHost(
                navController = navController,
                startDestination = startDestination
            ) {

                composable("login") {
                    ci.nsu.mobile.ui.screens.LoginScreen(
                        vm = authVm,
                        navController = navController
                    )
                }

                composable("register") {
                    ci.nsu.mobile.ui.screens.RegisterScreen(
                        vm = authVm,
                        navController = navController
                    )
                }

                composable("main") {
                    ci.nsu.mobile.ui.screens.MainScreen(
                        authVm = authVm,
                        depositVm = depositVm
                    )
                }
            }
        }
    }
}