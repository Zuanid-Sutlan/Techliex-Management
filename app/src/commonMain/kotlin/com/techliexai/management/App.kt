package com.techliexai.management

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.techliexai.management.presetation.components.Loading
import com.techliexai.management.presetation.components.SnackBarHost
import com.techliexai.management.presetation.components.enums.MessageType
import com.techliexai.management.presetation.navigation.NavGraph
import com.techliexai.management.presetation.navigation.Screen
import com.techliexai.management.presetation.utils.EventManager
import com.techliexai.management.ui.theme.ManagementTheme
import org.koin.compose.koinInject

@Composable
fun App() {
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    var snackBarMessageType by remember { mutableStateOf(MessageType.INFO) }

    var loadingState by remember { mutableStateOf(false) }

    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        EventManager.eventFlow.collect {
            when (it) {
                is EventManager.AppEvent.ShowToast -> {
                    snackBarMessageType = it.type
                    snackBarHostState.showSnackbar(it.message)
                }

                is EventManager.AppEvent.LoadingState -> {
                    loadingState = it.isLoading
                }

                is EventManager.AppEvent.NavigateTo -> {
                    navController.navigate(it.screen) {
                        popUpTo(Screen.LoginScreen) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }

                is EventManager.AppEvent.NavigateBack -> {
                    navController.navigateUp()
                }
            }
        }
    }

    ManagementTheme(darkTheme = true) {
        NavGraph(
            modifier = Modifier.fillMaxSize(),
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

        Loading(showLoading = loadingState)

        SnackBarHost(
            hostState = snackBarHostState,
            type = snackBarMessageType
        )
    }
}
