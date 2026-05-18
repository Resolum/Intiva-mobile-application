package com.resolum.intiva.features.savings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.resolum.intiva.core.ui.theme.IntivaColors

/**
 * Screen used to edit or modify an existing savings goal.
 * Pre-populated with mock data for demonstration.
 *
 * @param goalId The unique identifier of the goal to modify.
 * @param onNavigateBack Callback to go back to the previous screen.
 * @param onGoalUpdated Callback triggered when changes are successfully saved.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavingsGoalEditScreen(
    goalId: String,
    onNavigateBack: () -> Unit,
    onGoalUpdated: () -> Unit
) {
    var goalType by remember { mutableStateOf(0) } 

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Modificar Meta", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = Color.White)
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White, modifier = Modifier.size(28.dp))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = IntivaColors.PrimaryBrand)
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(IntivaColors.BackgroundSurface)
                    .padding(20.dp)
            ) {
                Button(
                    onClick = onGoalUpdated,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = IntivaColors.PrimaryAction),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Icon(Icons.Default.CheckCircleOutline, contentDescription = null, tint = IntivaColors.TextPrimary, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("GUARDAR CAMBIOS", color = IntivaColors.TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        },
        containerColor = IntivaColors.BackgroundSurface
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 32.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "MONTO OBJETIVO", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = IntivaColors.TextSecondary)
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(text = "S/ ", fontSize = 28.sp, color = IntivaColors.TextSecondary, modifier = Modifier.padding(bottom = 6.dp))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "5,000", fontSize = 42.sp, fontWeight = FontWeight.Bold, color = IntivaColors.PrimaryBrand)
                            Box(modifier = Modifier.width(100.dp).height(4.dp).background(IntivaColors.PrimaryAction, RoundedCornerShape(2.dp)))
                        }
                    }
                }
            }
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(72.dp)) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(IntivaColors.PrimaryBrand, CircleShape)
                                .align(Alignment.Center),
                            contentAlignment = Alignment.Center
                        ) {
                 
                            Text("✈", color = Color.White, fontSize = 32.sp)
                        }
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(Color.White, CircleShape)
                                .align(Alignment.BottomEnd),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar ícono", tint = IntivaColors.TextSecondary, modifier = Modifier.size(14.dp))
                        }
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    Column {
                        Text(text = "Nombre de la meta", fontSize = 14.sp, color = IntivaColors.TextSecondary, fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "Viaje a Cusco", fontSize = 20.sp, color = IntivaColors.TextPrimary, fontWeight = FontWeight.Bold)
                    }
                }
            }
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Fecha límite", fontSize = 14.sp, color = IntivaColors.TextSecondary, fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "15 Diciembre, 2024", fontSize = 18.sp, color = IntivaColors.TextPrimary, fontWeight = FontWeight.Bold)
                    }
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color(0xFFF2F0FA), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.CalendarToday, contentDescription = null, tint = IntivaColors.PrimaryBrand, modifier = Modifier.size(24.dp))
                    }
                }
            }
            
            Text(text = "Tipo de meta", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = IntivaColors.TextPrimary, modifier = Modifier.padding(top = 8.dp))
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE8E6F1), RoundedCornerShape(12.dp))
                    .padding(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (goalType == 0) Color.White else Color.Transparent)
                        .clickable { goalType = 0 }
                        .height(48.dp)
                        .padding(horizontal = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = if (goalType == 0) IntivaColors.PrimaryBrand else IntivaColors.TextSecondary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Personal", color = if (goalType == 0) IntivaColors.PrimaryBrand else IntivaColors.TextSecondary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (goalType == 1) Color.White else Color.Transparent)
                        .clickable { goalType = 1 }
                        .height(48.dp)
                        .padding(horizontal = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Group, contentDescription = null, tint = if (goalType == 1) IntivaColors.PrimaryBrand else IntivaColors.TextSecondary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Familiar", color = if (goalType == 1) IntivaColors.PrimaryBrand else IntivaColors.TextSecondary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
