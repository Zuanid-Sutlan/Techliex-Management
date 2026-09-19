package com.techliexai.management.presetation.screen.dashboard

import com.techliexai.management.domain.model.User
import com.techliexai.management.presetation.screen.dashboard.screen.DrawerItems

sealed interface DashboardAction {
    data class OnNavigateContentClicked(val item: DrawerItems) : DashboardAction


}