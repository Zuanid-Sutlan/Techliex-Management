package com.techliexai.management

import com.techliexai.management.data.datastore.DataStoreFactory
import com.techliexai.management.data.remote.KtorClientFactory
import com.techliexai.management.data.remote.api.AuthApiService
import com.techliexai.management.data.remote.api.AuthApiServiceImpl
import com.techliexai.management.data.remote.api.DashboardApiService
import com.techliexai.management.data.remote.api.DashboardApiServiceImpl
import com.techliexai.management.data.remote.api.MediaApiService
import com.techliexai.management.data.remote.api.MediaApiServiceImpl
import com.techliexai.management.data.remote.api.OrderApiService
import com.techliexai.management.data.remote.api.OrderApiServiceImpl
import com.techliexai.management.data.remote.api.ProductApiService
import com.techliexai.management.data.remote.api.ProductApiServiceImpl
import com.techliexai.management.data.remote.api.UserApiService
import com.techliexai.management.data.remote.api.UserApiServiceImpl
import com.techliexai.management.data.repository.AuthRepositoryImpl
import com.techliexai.management.data.repository.DashboardRepositoryImpl
import com.techliexai.management.data.repository.MediaRepositoryImpl
import com.techliexai.management.data.repository.OrderRepositoryImpl
import com.techliexai.management.data.repository.ProductRepositoryImpl
import com.techliexai.management.data.repository.UserPreferencesRepositoryImpl
import com.techliexai.management.data.repository.UserRepositoryImpl
import com.techliexai.management.domain.repository.AuthRepository
import com.techliexai.management.domain.repository.DashboardRepository
import com.techliexai.management.domain.repository.MediaRepository
import com.techliexai.management.domain.repository.OrderRepository
import com.techliexai.management.domain.repository.ProductRepository
import com.techliexai.management.domain.repository.UserPreferencesRepository
import com.techliexai.management.domain.repository.UserRepository
import com.techliexai.management.presetation.screen.dashboard.DashboardViewModel
import com.techliexai.management.presetation.screen.login.LoginViewModel
import com.techliexai.management.presetation.screen.member_add.CreateUserViewModel
import com.techliexai.management.presetation.screen.member_detail.MemberDetailViewModel
import com.techliexai.management.presetation.screen.members.MemberScreenViewModel
import com.techliexai.management.presetation.screen.orders.OrdersViewModel
import com.techliexai.management.presetation.screen.orders_add.AddOrderViewModel
import com.techliexai.management.presetation.screen.orrder_detail.OrderDetailViewModel
import com.techliexai.management.presetation.screen.product_add.AddProductViewModel
import com.techliexai.management.presetation.screen.product_detail.ProductDetailViewModel
import com.techliexai.management.presetation.screen.products.HuntProductViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {

    single { DataStoreFactory.create(get()) }
    singleOf(::UserPreferencesRepositoryImpl).bind<UserPreferencesRepository>()

    // Network & Ktor Client
    single { KtorClientFactory.create(get()) }

    // API Services
    singleOf(::AuthApiServiceImpl).bind<AuthApiService>()
    singleOf(::UserApiServiceImpl).bind<UserApiService>()
    singleOf(::ProductApiServiceImpl).bind<ProductApiService>()
    singleOf(::OrderApiServiceImpl).bind<OrderApiService>()
    singleOf(::DashboardApiServiceImpl).bind<DashboardApiService>()
    singleOf(::MediaApiServiceImpl).bind<MediaApiService>()

    // Repositories
    singleOf(::AuthRepositoryImpl).bind<AuthRepository>()
    singleOf(::UserRepositoryImpl).bind<UserRepository>()
    singleOf(::ProductRepositoryImpl).bind<ProductRepository>()
    singleOf(::OrderRepositoryImpl).bind<OrderRepository>()
    singleOf(::DashboardRepositoryImpl).bind<DashboardRepository>()
    singleOf(::MediaRepositoryImpl).bind<MediaRepository>()

    // ViewModels
    viewModelOf(::LoginViewModel)
    viewModelOf(::DashboardViewModel)
    viewModelOf(::CreateUserViewModel)
    viewModelOf(::MemberScreenViewModel)
    viewModelOf(::MemberDetailViewModel)
    viewModelOf(::HuntProductViewModel)
    viewModelOf(::AddProductViewModel)
    viewModelOf(::ProductDetailViewModel)
    viewModelOf(::OrdersViewModel)
    viewModelOf(::AddOrderViewModel)
    viewModelOf(::OrderDetailViewModel)
}
