package com.techliexai.management.presetation.screen.product_add.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.techliexai.management.domain.model.User

@Composable
fun MemberSelectionDialog(
    members: List<User>,
    onDismiss: () -> Unit,
    onSaveMemberSelection: (List<User>) -> Unit
) {
    val selectedList = remember { mutableStateListOf<User>() }
    Dialog(onDismissRequest = { onDismiss() }) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .background(Color(0xFF121212), RoundedCornerShape(12.dp))
                .border(BorderStroke(1.dp, Color.Yellow), shape = RoundedCornerShape(12.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Select Members", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.width(16.dp))
            FlowRow(modifier = Modifier) {
                members.forEach { user ->
                    UserItemView(
                        user = user,
                        isSelected = selectedList.contains(user),
                        onSelectUser = {
                            if (it) {
                                selectedList.add(user)
                            } else {
                                selectedList.remove(user)
                            }
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.width(24.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = selectedList.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(Color.Yellow),
                onClick = { onSaveMemberSelection(selectedList); onDismiss() }
            ) {
                Text(text = "Save")
            }
        }
    }
}


@Composable
fun UserItemView(user: User, isSelected: Boolean, onSelectUser: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.wrapContentSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isSelected,
            onCheckedChange = {
                onSelectUser(it)
            },
            colors = CheckboxDefaults.colors(Color.Yellow)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = user.name, style = MaterialTheme.typography.bodyMedium)
    }
}