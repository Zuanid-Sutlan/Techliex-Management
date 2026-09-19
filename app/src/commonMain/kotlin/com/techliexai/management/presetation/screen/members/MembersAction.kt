package com.techliexai.management.presetation.screen.members

import com.techliexai.management.domain.model.User

sealed interface MembersAction {
    object OnNavigateBackClicked : MembersAction
    data class OnMemberClicked(val member: User) : MembersAction
    object OnAddMemberClicked : MembersAction
}