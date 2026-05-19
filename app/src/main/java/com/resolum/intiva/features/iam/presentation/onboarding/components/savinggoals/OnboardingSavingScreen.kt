package com.resolum.intiva.features.iam.presentation.onboarding.components.savinggoals

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
import com.resolum.intiva.features.iam.presentation.onboarding.components.PremiumGoalsIllustration

/**
 * OnboardingSavingScreen - Composable for the second onboarding slide focused on saving goals.
 *
 * Displays an illustration and text encouraging users to set saving goals, with a clean and engaging design.
 */
@Composable
fun OnboardingSavingScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        PremiumGoalsIllustration()

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Establece metas de ahorro",
                style = TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    lineHeight = 36.sp
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Define tus objetivos y nosotros te ayudaremos a alcanzarlos paso a paso.",
                style = TextStyle(
                    fontSize = 15.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                ),
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}