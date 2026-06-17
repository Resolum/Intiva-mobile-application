package com.resolum.intiva.features.finances.presentation.spendinglimits.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.resolum.intiva.core.ui.theme.IntivaColors

@Composable
fun AmountInputCard(
    amount: String,
    onAmountChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(112.dp)
                .padding(horizontal = 40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "S/",
                fontSize = 24.sp,
                color = IntivaColors.TextSecondary
            )
            Spacer(modifier = Modifier.width(16.dp))
            TextField(
                value = amount,
                onValueChange = onAmountChange,
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text(
                        text = "1,000.00",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = IntivaColors.TextPrimary
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = IntivaColors.PrimaryBrand,
                    unfocusedIndicatorColor = IntivaColors.PrimaryBrand,
                    cursorColor = IntivaColors.PrimaryBrand
                )
            )
        }
    }
}
