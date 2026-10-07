package com.techliexai.management.presetation.screen.members

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techliexai.management.domain.model.User

@Composable
fun MemberScreen(
    state: MemberState,
    onAction: (MembersAction) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
    ) {
        Scaffold(modifier = Modifier.fillMaxSize(), containerColor = Color(0xFF121212)) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { onAction(MembersAction.OnNavigateBackClicked) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.Yellow)
                    }
                    Text(
                        text = "Community Members",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (state.members.isEmpty()) {
                    EmptyMembersState()
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 100.dp)
                    ) {
                        items(state.members) { member ->
                            MemberItem(
                                member = member,
                                onClick = { onAction(MembersAction.OnMemberClicked(member)) }
                            )
                        }
                    }
                }
            }
        }

        Button(
            onClick = { onAction(MembersAction.OnAddMemberClicked) },
            modifier = Modifier
                .padding(bottom = 36.dp, end = 24.dp)
                .align(Alignment.BottomEnd),
            shape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Yellow,
                contentColor = Color.Black
            )
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Create User",
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun EmptyMembersState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Group,
            contentDescription = null,
            tint = Color.Gray.copy(alpha = 0.3f),
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No members yet",
            color = Color.Gray,
            fontSize = 16.sp
        )
    }
}

@Composable
fun MemberItem(member: User, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        color = Color(0xFF1E1E1E),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        if (member.role == "Mentor") Color.Yellow else Color(0xFF333333),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = member.name.take(1).uppercase(),
                    color = if (member.role == "Mentor") Color.Black else Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(member.name, color = Color.White, fontWeight = FontWeight.Bold)
                Text(member.role, color = Color.Yellow, fontSize = 12.sp)
            }

            Icon(
                imageVector = Icons.Default.Circle,
                contentDescription = null,
                modifier = Modifier.size(10.dp),
                tint = if (member.isActive) Color.Green else Color.Gray
            )
        }
    }
}
