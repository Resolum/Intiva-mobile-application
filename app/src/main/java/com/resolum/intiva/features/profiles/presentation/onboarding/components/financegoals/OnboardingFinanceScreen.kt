package com.resolum.intiva.features.profiles.presentation.onboarding.components.financegoals

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.resolum.intiva.features.profiles.presentation.onboarding.components.PremiumDashboardIllustration

/**
 * OnboardingFinanceScreen - First slide of the onboarding flow.
 *
 * Showcases the "Financial Goals" feature with an illustration and descriptive text.
 */
@Composable
fun OnboardingFinanceScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        PremiumDashboardIllustration()

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Registra tus gastos fácilmente",
                style = TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF332F85),
                    textAlign = TextAlign.Center,
                    lineHeight = 36.sp
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Lleva un control detallado de tus finanzas personales y familiares en un solo lugar.",
                style = TextStyle(
                    fontSize = 15.sp,
                    color = Color(0xFF6E6A8A),
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                ),
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}