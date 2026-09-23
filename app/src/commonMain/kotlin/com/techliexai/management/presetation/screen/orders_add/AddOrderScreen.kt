package com.techliexai.management.presetation.screen.orders_add

import android.graphics.ImageDecoder
import android.os.Build
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material.icons.filled.*import androidx.compose.material3.DatePicker
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*import androidx.compose.material3.DatePickerDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*import androidx.compose.material3.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techliexai.management.domain.utils.base64ToImageBitmap
import com.techliexai.management.domain.utils.byteArrayToBase64
import com.techliexai.management.domain.utils.rememberImagePicker
import com.techliexai.management.domain.model.Order
import com.techliexai.management.domain.model.ProductHunt
import com.techliexai.management.presetation.screen.products.HuntProductScreenAction
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOrderScreen(
    state: AddOrderScreeState,
    onAction: (AddOrderScreenAction) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    var showSheet by remember { mutableStateOf(false) }

    // --- Date Picker States ---
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    // Image Picker Logic
    val imagePicker = rememberImagePicker()
    imagePicker.registerPicker { bytes ->
        val base64 = byteArrayToBase64(bytes)
        onAction(AddOrderScreenAction.OnPaymentImageChanged(base64))
    }

    // --- Date Picker Dialog ---
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val selectedDate = datePickerState.selectedDateMillis?.let {
                        val sdf =
                            java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                        sdf.format(java.util.Date(it))
                    } ?: ""
                    onAction(AddOrderScreenAction.OnOrderDateChanged(selectedDate))
                    showDatePicker = false
                }) {
                    Text("OK", color = Color.Yellow, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("CANCEL", color = Color.Gray)
                }
            },
            colors = DatePickerDefaults.colors(containerColor = Color(0xFF1E1E1E))
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    todayContentColor = Color.Yellow,
                    selectedDayContainerColor = Color.Yellow,
                    selectedDayContentColor = Color.Black,
                    titleContentColor = Color.White,
                    headlineContentColor = Color.White
                )
            )
        }
    }

    // Material 3 BottomSheet
    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState,
            containerColor = Color(0xFF1E1E1E),
            dragHandle = { CustomDragHandle() },
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            ProductSelectionSheet(
                productList = state.productList,
                onProductSelected = { product ->
                    onAction(AddOrderScreenAction.OnProductSelected(product))
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) showSheet = false
                    }
                }
            )
        }
    }

    Scaffold(
        containerColor = Color(0xFF121212)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // --- Header ---
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { onAction(AddOrderScreenAction.OnNavigateBackClicked) }) {
                    Icon(Icons.Default.ArrowBack, null, tint = Color.Yellow)
                }
                Text(
                    "Create New Order",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- 1. Product Selector ---
            Text(
                "Select Product",
                color = Color.Yellow,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { showSheet = true },
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color.Gray.copy(0.2f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.List, null, tint = Color.Yellow)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = state.selectedProduct?.title ?: "Tap to choose a product...",
                        color = if (state.selectedProduct != null) Color.White else Color.Gray,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Icon(Icons.Default.KeyboardArrowDown, null, tint = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- 2. Date Selector (NEW) ---
            Text("Order Date", color = Color.Yellow, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { showDatePicker = true },
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color.Gray.copy(0.2f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.DateRange, null, tint = Color.Yellow)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = state.orderDate.ifBlank { "Select order date..." },
                        color = if (state.orderDate.isNotEmpty()) Color.White else Color.Gray,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- 3. Rest of the Form ---
            OrderInputField(state.address, "Full Shipping Address", isSingleLine = false) {
                onAction(AddOrderScreenAction.OnAddressChanged(it))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OrderInputField(state.quantity, "Qty", Modifier.weight(1f), keyboardType = KeyboardType.Number) {
                    onAction(AddOrderScreenAction.OnQuantityChanged(it))
                }
                OrderInputField(state.listingPrice, "Order Price ($)", Modifier.weight(1.5f), keyboardType = KeyboardType.Number) {
                    onAction(AddOrderScreenAction.OnListingPriceChanged(it))
                }
            }

            OrderInputField(state.variationNote, "Variation (Size, Color, etc.)") {
                onAction(AddOrderScreenAction.OnVariationNoteChanged(it))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- Payment Proof ---
            Text(
                "Payment Proof",
                color = Color.Yellow,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(vertical = 8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF1E1E1E))
                    .clickable { imagePicker.pickImage() },
                contentAlignment = Alignment.Center
            ) {
                if (state.paymentImage.isEmpty()) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.Add,
                            null,
                            tint = Color.Gray,
                            modifier = Modifier.size(40.dp)
                        )
                        Text("Upload Payment Screenshot", color = Color.Gray, fontSize = 12.sp)
                    }
                } else {
                    val bitmap = remember(state.paymentImage) { base64ToImageBitmap(state.paymentImage) }
                    bitmap?.let {
                        Image(
                            it,
                            null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { onAction(AddOrderScreenAction.OnSaveClicked) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("PLACE ORDER", color = Color.Black, fontWeight = FontWeight.ExtraBold)
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun CustomDragHandle() {
    Box(
        modifier = Modifier
            .padding(vertical = 12.dp)
            .width(40.dp)
            .height(4.dp)
            .background(Color.Gray.copy(0.5f), CircleShape)
    )
}

@Composable
fun ProductSelectionSheet(
    productList: List<ProductHunt>,
    onProductSelected: (ProductHunt) -> Unit
) {
    var query by remember { mutableStateOf("") }
    val filteredList = productList.filter { it.title.contains(query, ignoreCase = true) }

    Column(modifier = Modifier
        .fillMaxHeight(0.8f)
        .padding(20.dp)) {
        Text(
            "Select Product Hunt",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Search within Sheet
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            placeholder = { Text("Search hunts...", color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    tint = Color.Yellow,
                    contentDescription = null
                )
            },
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.Yellow),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(filteredList) { product ->
                Card(
                    colors = CardDefaults.cardColors(Color(0xFF2A2A2A)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onProductSelected(product) },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Small Thumbnail decode
                        val thumb =
                            remember(product.productImage) { base64ToImageBitmap(product.productImage) }
                        Surface(
                            Modifier.size(40.dp),
                            shape = RoundedCornerShape(4.dp),
                            color = Color.Black
                        ) {
                            thumb?.let {
                                Image(
                                    it,
                                    null,
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(product.title, color = Color.White, fontWeight = FontWeight.Bold)
                            Text("$${product.sourcePrice}", color = Color.Yellow, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderInputField(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    isSingleLine: Boolean = true,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontSize = 12.sp) },
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        singleLine = isSingleLine,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Yellow,
            unfocusedBorderColor = Color.Gray.copy(0.3f),
//            textColor = Color.White,
            cursorColor = Color.Yellow,
            focusedLabelColor = Color.Yellow
        ),
        shape = RoundedCornerShape(12.dp)
    )
}

