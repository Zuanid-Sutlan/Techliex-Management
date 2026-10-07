package com.techliexai.management.presetation.screen.orders_add

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.techliexai.management.presetation.utils.EventManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOrderScreen(
    state: AddOrderScreeState,
    onAction: (AddOrderScreenAction) -> Unit
) {
    Scaffold(
        containerColor = Color(0xFF121212),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(Color(0xFF121212)),
                title = { Text("Add Order", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { EventManager.navigateBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.Yellow)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = state.address,
                onValueChange = { onAction(AddOrderScreenAction.OnAddressChanged(it)) },
                label = { Text("Shipping Address") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = state.quantity,
                onValueChange = { onAction(AddOrderScreenAction.OnQuantityChanged(it)) },
                label = { Text("Quantity") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = state.listingPrice,
                onValueChange = { onAction(AddOrderScreenAction.OnListingPriceChanged(it)) },
                label = { Text("Listing Price") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = state.paymentImage,
                onValueChange = { onAction(AddOrderScreenAction.OnPaymentImageChanged(it)) },
                label = { Text("Payment Screenshot Image URL") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            if (state.paymentImage.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF1E1E1E))
                ) {
                    AsyncImage(
                        model = state.paymentImage,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Button(
                onClick = { onAction(AddOrderScreenAction.OnSaveClicked) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("SUBMIT ORDER", color = Color.Black, fontWeight = FontWeight.ExtraBold)
            }
        }
    }
}
