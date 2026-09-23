package com.techliexai.management.presetation.screen.products

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*import androidx.compose.material3.Button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*import androidx.compose.material3.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*import androidx.compose.material3.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*import androidx.compose.material3.CardDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*import androidx.compose.material3.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*import androidx.compose.material3.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*import androidx.compose.material3.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techliexai.management.domain.model.ProductHunt
import com.techliexai.management.presetation.screen.orders.OrderScreenAction
import com.techliexai.management.presetation.screen.product_add.AddProductScreenAction
import com.techliexai.management.domain.utils.base64ToImageBitmap

@Composable
fun HuntProductScreen(
    state: HuntProductScreenState,
    onAction: (HuntProductScreenAction) -> Unit
) {
    Scaffold(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(Color(0xFF121212))
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // --- Header Section ---
//                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
//                    Text(
//                        text = "Product Hunts",
//                        color = Color.White,
//                        fontSize = 28.sp,
//                        fontWeight = FontWeight.ExtraBold
//                    )
//                    Text(
//                        text = "Discover and share winning products",
//                        color = Color.Gray,
//                        fontSize = 14.sp
//                    )
//                }
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { onAction(HuntProductScreenAction.OnNavigateBackClicked) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.Yellow)
                    }
                    Text(
                        text = "Product Hunts",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // --- Search Bar ---
                OutlinedTextField(
                    value = state.searchQuery,
                    onValueChange = { onAction(HuntProductScreenAction.OnSearchQueryChanged(it)) },
                    placeholder = { Text("Search products...", color = Color.Gray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = null,
                            tint = Color.Yellow
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Yellow,
                        unfocusedBorderColor = Color.Gray.copy(0.3f),
//                    textColor = Color.White,
                        cursorColor = Color.Yellow
                    ),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(20.dp))

                // --- Product Grid/List ---
                val filteredProducts = state.products.filter {
                    it.title.contains(state.searchQuery, ignoreCase = true)
                }

                if (filteredProducts.isEmpty()) {
                    EmptyProductsState()
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 100.dp)
                    ) {
                        items(filteredProducts) { product ->
                            ProductHuntCard(
                                product = product,
                                onClick = {
                                    onAction(
                                        HuntProductScreenAction.OnProductClicked(product)
                                    )
                                }
                            )
                        }
                    }
                }
            }

            // --- Manual Floating Action Button ---
            Button(
                onClick = { onAction(HuntProductScreenAction.OnAddProductClicked) },
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
                    contentDescription = "Add Product",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }

//        // --- Add Product FAB ---
//        FloatingActionButton(
//            onClick = {  },
//            backgroundColor = Color.Yellow,
//            shape = RoundedCornerShape(16.dp),
//            modifier = Modifier
//                .align(Alignment.BottomEnd)
//                .padding(24.dp)
//        ) {
//            Icon(Icons.Default.Add, contentDescription = "Add Product", tint = Color.Black)
//        }
        }
    }
}

@Composable
fun ProductHuntCard(
    product: ProductHunt,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(Color(0xFF1E1E1E)),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            // --- Image Section ---
            val bitmap = remember(product.productImage) { base64ToImageBitmap(product.productImage) }

            Box(modifier = Modifier
                .height(180.dp)
                .fillMaxWidth()) {
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap,
                        contentDescription = product.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Warning,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }
                }

                // --- Price Tag Overlay ---
                Surface(
                    color = Color.Yellow,
                    modifier = Modifier.fillMaxWidth().align(Alignment.BottomEnd),
                    shape = RoundedCornerShape(topStart = 16.dp)
                ) {
                    Text(
                        text = "Warehouse Price: $${if (product.warehousePrice == 0) "N/A" else product.warehousePrice}",
                        color = Color.Black,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp
                    )
                }
            }

            // --- Content Section ---
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = product.title,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = product.description,
                    color = Color.Gray,
                    fontSize = 13.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.Yellow,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Added by ${product.addedBy}",
                        color = Color.Yellow,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyProductsState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.Close,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(64.dp)
        )
        Text("No product hunts found.", color = Color.Gray)
    }
}