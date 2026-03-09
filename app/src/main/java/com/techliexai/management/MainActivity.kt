package com.techliexai.management

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.techliexai.management.presetation.navigation.NavGraph
import com.techliexai.management.presetation.components.Loading
import com.techliexai.management.presetation.components.SnackBarHost
import com.techliexai.management.presetation.components.enums.MessageType
import com.techliexai.management.presetation.navigation.Screen
import com.techliexai.management.presetation.screen.member_add.CreateUserViewModel
import com.techliexai.management.presetation.screen.dashboard.DashboardViewModel
import com.techliexai.management.presetation.screen.login.LoginViewModel
import com.techliexai.management.presetation.screen.members.MemberScreenViewModel
import com.techliexai.management.presetation.utils.EventManager
import com.techliexai.management.ui.theme.ManagementTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

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

            val loginViewModel = koinViewModel<LoginViewModel>()
            val dashboardViewModel = koinViewModel<DashboardViewModel>()
            val createUserViewModel = koinViewModel<CreateUserViewModel>()
            val memberScreenViewModel = koinViewModel<MemberScreenViewModel>()

            ManagementTheme(darkTheme = true) {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                NavGraph(
                    modifier = Modifier.fillMaxSize(), // .padding(innerPadding),
                    navController = navController,
                    loginViewModel = loginViewModel,
                    dashboardViewModel = dashboardViewModel,
                    createUserViewModel = createUserViewModel,
                    memberScreenViewModel = memberScreenViewModel,
                    memberDetailViewModel = koinViewModel(),
                    huntProductViewModel = koinViewModel(),
                    addProductViewModel = koinViewModel(),
                    productDetailViewModel = koinViewModel(),
                    orderViewModel = koinViewModel(),
                    addOrderViewModel = koinViewModel(),
                    orderDetailViewModel = koinViewModel()
                )
//                }

                Loading(showLoading = loadingState)

                SnackBarHost(
                    hostState = snackBarHostState,
                    type = snackBarMessageType
                )
            }
        }
    }
}


@Composable
fun TextScreen(scope: CoroutineScope, innerPadding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Hello Android!",
            modifier = Modifier
        )

        Button(onClick = {
            scope.launch {
                EventManager.showMessage("test done", MessageType.SUCCESS)
            }
        }) {
            Text("test success")
        }
        Button(onClick = {
            scope.launch {
                EventManager.showMessage("test done", MessageType.ERROR)
            }
        }) {
            Text("test error")
        }
        Button(onClick = {
            scope.launch {
                EventManager.showMessage("test done", MessageType.WARNING)
            }
        }) {
            Text("test warning")
        }
        Button(onClick = {
            scope.launch {
                EventManager.showMessage("test done", MessageType.INFO)
            }
        }) {
            Text("test info")
        }

        Button(onClick = {
            scope.launch {
                EventManager.showLoading()

                delay(5000)
                EventManager.hideLoading()
            }
        }) {
            Text("show loadinng")
        }
    }
}