package com.techliexai.management

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.techliexai.management.ui.theme.ManagementTheme
import com.techliexai.management.presetation.navigation.NavGraph
import com.techliexai.management.presetation.navigation.Screen
import com.techliexai.management.presetation.navigation.NavDestination
import com.techliexai.management.presetation.navigation.ResponsiveNavigationLayout
import com.techliexai.management.presetation.components.Loading
import com.techliexai.management.presetation.components.SnackBarHost
import com.techliexai.management.presetation.components.enums.MessageType
import com.techliexai.management.presetation.utils.EventManager
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject

fun main() = application {
    val state = rememberWindowState(width = 1280.dp, height = 750.dp)

    Window(
        onCloseRequest = ::exitApplication,
        title = "Techliex Management (Desktop)",
        state = state
    ) {
        KoinApplication(application = {
            modules(sharedAppModule)
        }) {
            ManagementTheme(darkTheme = true) {
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
                                    launchSingleTop = true
                                }
                            }
                            is EventManager.AppEvent.NavigateBack -> {
                                navController.navigateUp()
                            }
                        }
                    }
                }

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val currentDestination = when {
                    currentRoute?.contains("DashboardScreen") == true -> NavDestination.DASHBOARD
                    currentRoute?.contains("MembersScreen") == true -> NavDestination.MEMBERS
                    currentRoute?.contains("HuntProductScreen") == true -> NavDestination.PRODUCTS
                    currentRoute?.contains("OrdersScreen") == true -> NavDestination.ORDERS
                    currentRoute?.contains("SettingsScreen") == true -> NavDestination.SETTINGS
                    else -> null
                }

                val isLogin = currentRoute?.contains("LoginScreen") == true || currentRoute == null

                val navGraphContent = @Composable {
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
                }

                if (isLogin) {
                    navGraphContent()
                } else {
                    ResponsiveNavigationLayout(
                        currentDestination = currentDestination,
                        onNavigate = { destination ->
                            navController.navigate(destination.screen) {
                                popUpTo(Screen.DashboardScreen) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        onLogout = {
                            navController.navigate(Screen.LoginScreen) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    ) {
                        navGraphContent()
                    }
                }

                Loading(showLoading = loadingState)

                SnackBarHost(
                    hostState = snackBarHostState,
                    type = snackBarMessageType
                )
            }
        }
    }
}
