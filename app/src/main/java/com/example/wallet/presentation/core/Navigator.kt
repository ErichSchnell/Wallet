package com.example.wallet.presentation.core

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.wallet.presentation.auth.AuthScreen
import com.example.wallet.presentation.core.Routes.*
import com.example.wallet.presentation.home.HomeScreen

@Composable
fun ContentWrapper(navigatonController: NavHostController) {


    NavHost(
        navController = navigatonController,
        startDestination = Login.route
    ){
        composable(Login.route){
            AuthScreen { email ->
                navigatonController.navigate(Home.createRoute(email))
            }
        }
        composable(
            Home.route,
            arguments = listOf(
                navArgument("email") {type = NavType.StringType}
            )
        ){
            HomeScreen(
                userEmail = it.arguments?.getString("email").orEmpty(),
                navigateToLogin = {
                    navigatonController.popBackStack(navigatonController.graph.startDestinationId, true)
                    navigatonController.navigate(Login.route)
                }
            )
        }
    }



}

sealed class Routes(val route:String){

    object Login:Routes("auth")
    object Home:Routes("home/{email}"){
        fun createRoute(email: String): String{
            return "home/${email}"
        }
    }
}