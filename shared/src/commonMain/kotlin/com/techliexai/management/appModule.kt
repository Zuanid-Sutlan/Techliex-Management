package com.techliexai.management

import com.techliexai.management.data.repository.OrderRepositoryFake
import com.techliexai.management.data.repository.ProductRepositoryFake
import com.techliexai.management.data.repository.UserPreferencesRepositoryFake
import com.techliexai.management.data.repository.UserRepositoryFake
import com.techliexai.management.domain.repository.OrderRepository
import com.techliexai.management.domain.repository.ProductRepository
import com.techliexai.management.domain.repository.UserPreferencesRepository
import com.techliexai.management.domain.repository.UserRepository
import com.techliexai.management.presetation.screen.login.LoginViewModel
import com.techliexai.management.presetation.screen.dashboard.DashboardViewModel
import com.techliexai.management.presetation.screen.member_add.CreateUserViewModel
import com.techliexai.management.presetation.screen.member_detail.MemberDetailViewModel
import com.techliexai.management.presetation.screen.members.MemberScreenViewModel
import com.techliexai.management.presetation.screen.orders.OrdersViewModel
import com.techliexai.management.presetation.screen.orders_add.AddOrderViewModel
import com.techliexai.management.presetation.screen.orrder_detail.OrderDetailViewModel
import com.techliexai.management.presetation.screen.product_add.AddProductViewModel
import com.techliexai.management.presetation.screen.product_detail.ProductDetailViewModel
import com.techliexai.management.presetation.screen.products.HuntProductViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val sharedAppModule = module {
    singleOf(::UserRepositoryFake).bind<UserRepository>()
    singleOf(::UserPreferencesRepositoryFake).bind<UserPreferencesRepository>()
    singleOf(::ProductRepositoryFake).bind<ProductRepository>()
    singleOf(::OrderRepositoryFake).bind<OrderRepository>()

    factoryOf(::LoginViewModel)
    factoryOf(::DashboardViewModel)
    factoryOf(::CreateUserViewModel)
    factoryOf(::MemberScreenViewModel)
    factoryOf(::MemberDetailViewModel)
    factoryOf(::HuntProductViewModel)
    factoryOf(::AddProductViewModel)
    factoryOf(::ProductDetailViewModel)
    factoryOf(::OrdersViewModel)
    factoryOf(::AddOrderViewModel)
    factoryOf(::OrderDetailViewModel)
}
