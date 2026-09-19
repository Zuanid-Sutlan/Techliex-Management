package com.techliexai.management

import com.techliexai.management.data.datastore.DataStoreFactory
import com.techliexai.management.data.repository.UserPreferencesRepositoryImpl
import com.techliexai.management.data.repository.UserRepositoryImpl
import com.techliexai.management.domain.repository.UserPreferencesRepository
import com.techliexai.management.domain.repository.UserRepository
import com.techliexai.management.presetation.screen.product_add.AddProductViewModel
import com.techliexai.management.presetation.screen.member_add.CreateUserViewModel
import com.techliexai.management.presetation.screen.dashboard.DashboardViewModel
import com.techliexai.management.presetation.screen.member_detail.MemberDetailViewModel
import com.techliexai.management.presetation.screen.login.LoginViewModel
import com.techliexai.management.presetation.screen.members.MemberScreenViewModel
import com.techliexai.management.presetation.screen.orders.OrdersViewModel
import com.techliexai.management.presetation.screen.orders_add.AddOrderViewModel
import com.techliexai.management.presetation.screen.orrder_detail.OrderDetailViewModel
import com.techliexai.management.presetation.screen.product_detail.ProductDetailViewModel
import com.techliexai.management.presetation.screen.products.HuntProductViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {

    single { DataStoreFactory.create() }

    singleOf(::UserRepositoryImpl).bind<UserRepository>()
    singleOf(::UserPreferencesRepositoryImpl).bind<UserPreferencesRepository>()

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