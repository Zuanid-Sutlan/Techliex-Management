package com.techliexai.management.presetation.screen.dashboard

import com.techliexai.management.presetation.screen.dashboard.screen.DrawerItems

sealed class DashboardAction {
    data class OnNavigateContentClicked(val item: DrawerItems) : DashboardAction()
}
