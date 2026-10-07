package com.techliexai.management

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.navigation.compose.rememberNavController
import com.techliexai.management.ui.theme.ManagementTheme
import com.techliexai.management.presetation.navigation.NavGraph
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Techliex Management (Desktop KMP)"
    ) {
        KoinApplication(application = {
            modules(sharedAppModule)
        }) {
            ManagementTheme {
                val navController = rememberNavController()
                NavGraph(
                    navController = navController,
                    loginViewModel = koinInject(),
                    dashboardViewModel = koinInject(),
                    createUserViewModel = koinInject(),
                    memberScreenViewModel = koinInject(),
                    memberDetailViewModel = koinInject(),
                    huntProductViewModel = koinInject(),
                    addProductViewModel = koinInject(),
                    productDetailViewModel = koinInject(),
                    orderViewModel = koinInject(),
                    addOrderViewModel = koinInject(),
                    orderDetailViewModel = koinInject()
                )
            }
        }
    }
}
