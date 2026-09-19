package com.techliexai.management.presetation.screen.member_detail

sealed interface MemberDetailAction {
    data class OnLoadMember(val username: String) : MemberDetailAction
    object OnBackClicked : MemberDetailAction
    object OnEditMemberClicked : MemberDetailAction
    object OnDeleteMemberClicked : MemberDetailAction
}