package com.techliexai.management.presetation.screen.members

import com.techliexai.management.domain.model.User
import kotlinx.serialization.Serializable

@Serializable
data class MemberState(
    val members: List<User> = emptyList(),
)
