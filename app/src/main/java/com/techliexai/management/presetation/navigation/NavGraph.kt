package com.techliexai.management.presetation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.techliexai.management.presetation.screen.product_add.AddProductScreen
import com.techliexai.management.presetation.screen.product_add.AddProductViewModel
import com.techliexai.management.presetation.screen.member_add.CreateNewRole
import com.techliexai.management.presetation.screen.member_add.CreateUserViewModel
import com.techliexai.management.presetation.screen.dashboard.DashboardScreen
import com.techliexai.management.presetation.screen.dashboard.DashboardViewModel
import com.techliexai.management.presetation.screen.member_detail.MemberDetailAction
import com.techliexai.management.presetation.screen.member_detail.MemberDetailScreen
import com.techliexai.management.presetation.screen.member_detail.MemberDetailViewModel
import com.techliexai.management.presetation.screen.login.LoginScreen
import com.techliexai.management.presetation.screen.login.LoginViewModel
import com.techliexai.management.presetation.screen.members.MemberScreen
import com.techliexai.management.presetation.screen.members.MemberScreenViewModel
import com.techliexai.management.presetation.screen.orders.OrdersScreen
import com.techliexai.management.presetation.screen.orders.OrdersViewModel
import com.techliexai.management.presetation.screen.orders_add.AddOrderScreen
import com.techliexai.management.presetation.screen.orders_add.AddOrderViewModel
import com.techliexai.management.presetation.screen.orrder_detail.OrderDetailScreen
import com.techliexai.management.presetation.screen.orrder_detail.OrderDetailScreenAction
import com.techliexai.management.presetation.screen.orrder_detail.OrderDetailViewModel
import com.techliexai.management.presetation.screen.product_detail.ProductDetailScreen
import com.techliexai.management.presetation.screen.product_detail.ProductDetailScreenAction
import com.techliexai.management.presetation.screen.product_detail.ProductDetailViewModel
import com.techliexai.management.presetation.screen.products.HuntProductScreen
import com.techliexai.management.presetation.screen.products.HuntProductViewModel

@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: Screen = Screen.LoginScreen,
    loginViewModel: LoginViewModel,
    dashboardViewModel: DashboardViewModel,
    createUserViewModel: CreateUserViewModel,
    memberScreenViewModel: MemberScreenViewModel,
    memberDetailViewModel: MemberDetailViewModel,
    huntProductViewModel: HuntProductViewModel,
    addProductViewModel: AddProductViewModel,
    productDetailViewModel: ProductDetailViewModel,
    orderViewModel: OrdersViewModel,
    addOrderViewModel: AddOrderViewModel,
    orderDetailViewModel: OrderDetailViewModel
) {

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable<Screen.LoginScreen> {
            val state by loginViewModel.state.collectAsStateWithLifecycle()
            LoginScreen(
                state = state,
                onAction = loginViewModel::onAction
            )
        }
        composable<Screen.DashboardScreen> {
            val state by dashboardViewModel.state.collectAsStateWithLifecycle()
            DashboardScreen(
                state = state,
                onAction = dashboardViewModel::onAction
            )
        }

        composable<Screen.CreateMemberScreen> {
            val state by createUserViewModel.state.collectAsStateWithLifecycle()
            CreateNewRole(state = state, onAction = createUserViewModel::onAction)
        }

        composable<Screen.MembersScreen> {
            val state by memberScreenViewModel.state.collectAsStateWithLifecycle()
            MemberScreen(
                state = state,
                onAction = memberScreenViewModel::onAction
            )
        }

        composable<Screen.MemberDetailScreen> {
            val args = it.toRoute<Screen.MemberDetailScreen>()

            LaunchedEffect(Unit) {
                memberDetailViewModel.onAction(MemberDetailAction.OnLoadMember(args.username))
            }
            val state by memberDetailViewModel.state.collectAsStateWithLifecycle()
            MemberDetailScreen(
                state = state,
                onAction = memberDetailViewModel::onAction
            )
        }

        composable<Screen.HuntProductScreen> {
            val state by huntProductViewModel.state.collectAsStateWithLifecycle()
            HuntProductScreen(
                state = state,
                onAction = huntProductViewModel::onAction
            )
        }
        composable<Screen.AddProductScreen> {
            val state by addProductViewModel.state.collectAsStateWithLifecycle()
            AddProductScreen(
                state = state,
                onAction = addProductViewModel::onAction
            )
        }
        composable<Screen.ProductDetailScreen> {
            val args = it.toRoute<Screen.ProductDetailScreen>()
            LaunchedEffect(Unit) {
                productDetailViewModel.onAction(ProductDetailScreenAction.OnLoadProduct(args.productId))
            }
            val state by productDetailViewModel.state.collectAsStateWithLifecycle()
            ProductDetailScreen(
                state = state,
                isAdmin = state.isAdmin,
                onAction = productDetailViewModel::onAction
                )
        }
        composable<Screen.OrdersScreen> {
            val state by orderViewModel.state.collectAsStateWithLifecycle()
            OrdersScreen(
                state = state,
                onAction = orderViewModel::onAction
            )
        }
        composable<Screen.AddOrderScreen> {
            val state by addOrderViewModel.state.collectAsStateWithLifecycle()
            AddOrderScreen(
                state = state,
                onAction = addOrderViewModel::onAction
            )
        }
        composable<Screen.OrderDetailScreen> {

            val args = it.toRoute<Screen.OrderDetailScreen>()

            LaunchedEffect(Unit) {
                orderDetailViewModel.onAction(OrderDetailScreenAction.OnLoadOrder(args.orderId))
            }

            val state by orderDetailViewModel.state.collectAsStateWithLifecycle()
            OrderDetailScreen(
                state = state,
                onAction = orderDetailViewModel::onAction
            )
        }
    }

}