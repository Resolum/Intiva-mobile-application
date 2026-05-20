package com.resolum.intiva.features.savings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.resolum.intiva.core.ui.theme.IntivaColors

/**
 * Screen displaying the details of a specific savings goal.
 * Features a circular progress card, quick metric stats, family contributions list, and transaction history.
 *
 * @param goalId The unique identifier of the savings goal.
 * @param onNavigateBack Callback to go back to the previous screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavingsGoalDetailScreen(
    goalId: String,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Fondo de emergencia", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = IntivaColors.TextPrimary) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = IntivaColors.TextPrimary, modifier = Modifier.size(28.dp))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = IntivaColors.BackgroundSurface)
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
                    onClick = { /* TODO */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = IntivaColors.PrimaryAction),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = IntivaColors.TextPrimary, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Aportar", color = IntivaColors.TextPrimary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
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
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(220.dp)
                    ) {
                        CircularProgressIndicator(
                            progress = { 1f },
                            modifier = Modifier.fillMaxSize(),
                            color = IntivaColors.BackgroundSurface,
                            strokeWidth = 14.dp
                        )
                        CircularProgressIndicator(
                            progress = { 0.75f },
                            modifier = Modifier.fillMaxSize(),
                            color = IntivaColors.PrimaryAction,
                            strokeWidth = 14.dp,
                            trackColor = Color.Transparent
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "75%", fontWeight = FontWeight.Bold, fontSize = 42.sp, color = IntivaColors.PrimaryBrand)
                            Text(text = "S/. 2,250", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = IntivaColors.TextPrimary)
                            Text(text = "DE S/. 3,000", fontSize = 14.sp, color = IntivaColors.TextSecondary, fontWeight = FontWeight.Bold)
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "PROGRESO", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = IntivaColors.TextSecondary)
                            Text(text = "Excelente ritmo", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = IntivaColors.PrimaryBrand)
                        }
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color(0xFFF4FAD8), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.TrendingUp, contentDescription = null, tint = Color(0xFF5A7000), modifier = Modifier.size(24.dp))
                        }
                    }
                }
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CalendarToday, contentDescription = null, tint = IntivaColors.PrimaryBrand, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "TIEMPO", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = IntivaColors.PrimaryBrand)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(text = "45", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = IntivaColors.TextPrimary)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "días", fontSize = 16.sp, color = IntivaColors.TextSecondary, modifier = Modifier.padding(bottom = 3.dp))
                        }
                        Text(text = "Restantes", fontSize = 14.sp, color = IntivaColors.TextSecondary)
                    }
                }
                
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Sync, contentDescription = null, tint = IntivaColors.PrimaryBrand, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "AUTO", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = IntivaColors.PrimaryBrand)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(text = "Activo", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = IntivaColors.TextPrimary)
                        Text(text = "S/ 150/qncna", fontSize = 14.sp, color = IntivaColors.TextSecondary)
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
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "Familia", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = IntivaColors.TextPrimary)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = "VER TODOS", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = IntivaColors.PrimaryBrand)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row {
                            Box(modifier = Modifier.size(48.dp).clip(CircleShape).background(Color.Gray).border(3.dp, Color.White, CircleShape), contentAlignment = Alignment.Center) { Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp)) }
                            Box(modifier = Modifier.size(48.dp).offset(x = (-12).dp).clip(CircleShape).background(Color.DarkGray).border(3.dp, Color.White, CircleShape), contentAlignment = Alignment.Center) { Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp)) }
                            Box(modifier = Modifier.size(48.dp).offset(x = (-24).dp).clip(CircleShape).background(IntivaColors.PrimaryBrand).border(3.dp, Color.White, CircleShape), contentAlignment = Alignment.Center) { Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp)) }
                            Box(
                                modifier = Modifier.size(48.dp).offset(x = (-36).dp).background(Color.White, CircleShape).border(2.dp, Color.LightGray, CircleShape).clickable { },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Añadir", tint = Color.Gray, modifier = Modifier.size(24.dp))
                            }
                        }
                    }
                }
            }
            
            Text(text = "Historial de aportes", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = IntivaColors.TextPrimary, modifier = Modifier.padding(top = 8.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column {
                    HistoryItem(name = "Tú", time = "Hoy, 10:30 AM", amount = "+ S/. 250", iconBg = Color.DarkGray, isAuto = false)
                    Divider(color = IntivaColors.BackgroundSurface, thickness = 1.dp, modifier = Modifier.padding(horizontal = 20.dp))
                    HistoryItem(name = "Ahorro automático", time = "15 Oct, 08:00 AM", amount = "+ S/. 150", iconBg = Color(0xFFE8E6F1), isAuto = true)
                    Divider(color = IntivaColors.BackgroundSurface, thickness = 1.dp, modifier = Modifier.padding(horizontal = 20.dp))
                    HistoryItem(name = "Maria G.", time = "12 Oct, 18:45 PM", amount = "+ S/. 500", iconBg = IntivaColors.PrimaryBrand, isAuto = false)
                }
            }
            
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

/**
 * Individual row item representing a past contribution inside the detail screen.
 */
@Composable
fun HistoryItem(name: String, time: String, amount: String, iconBg: Color, isAuto: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(iconBg, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (isAuto) {
                Icon(Icons.Default.Sync, contentDescription = null, tint = IntivaColors.PrimaryBrand, modifier = Modifier.size(24.dp))
            } else {
                Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = IntivaColors.TextPrimary)
            Text(text = time, fontSize = 14.sp, color = IntivaColors.TextSecondary)
        }
        Text(
            text = amount,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color(0xFF5A7000)
        )
    }
}
