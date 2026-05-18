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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.resolum.intiva.core.ui.theme.IntivaColors

/**
 * Screen used to create a new savings goal.
 *
 * @param onNavigateBack Callback to go back to the previous screen.
 * @param onGoalCreated Callback triggered when the goal is successfully created.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavingsGoalCreateScreen(
    onNavigateBack: () -> Unit,
    onGoalCreated: () -> Unit
) {
    var goalType by remember { mutableStateOf(0) } 
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("Nueva Meta", fontWeight = FontWeight.Medium, fontSize = 20.sp, modifier = Modifier.padding(end = 48.dp))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = IntivaColors.TextPrimary, modifier = Modifier.size(28.dp))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = IntivaColors.BackgroundLavender)
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(IntivaColors.BackgroundLavender)
                    .padding(20.dp) 
            ) {
                Button(
                    onClick = onGoalCreated,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp), 
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = IntivaColors.PrimaryGreen),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Icon(Icons.Default.CheckCircleOutline, contentDescription = null, tint = IntivaColors.TextPrimary, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Guardar Meta",
                        color = IntivaColors.TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        containerColor = IntivaColors.BackgroundLavender
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "S/", fontSize = 28.sp, color = IntivaColors.TextSecondary, fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "5,000", fontSize = 42.sp, color = IntivaColors.BackgroundPurple, fontWeight = FontWeight.Bold)
                            Box(modifier = Modifier.width(80.dp).height(4.dp).background(IntivaColors.PrimaryGreen, RoundedCornerShape(2.dp)))
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
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
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .background(IntivaColors.BackgroundPurple, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(modifier = Modifier.size(32.dp).background(Color.White, CircleShape)) 
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(24.dp)
                                .background(Color.White, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp), tint = IntivaColors.TextSecondary)
                        }
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    Column {
                        Text(text = "Nombre de la meta", fontSize = 14.sp, color = IntivaColors.TextSecondary, fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "Viaje a Cusco", fontSize = 20.sp, color = IntivaColors.TextPrimary, modifier = Modifier.fillMaxWidth(), fontWeight = FontWeight.Bold)
                        Divider(color = Color(0xFFE8E6F1), thickness = 1.dp, modifier = Modifier.padding(top = 8.dp))
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(text = "Fecha límite", fontSize = 14.sp, color = IntivaColors.TextSecondary, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "15 Diciembre, 2024", fontSize = 18.sp, color = IntivaColors.TextPrimary, fontWeight = FontWeight.Bold)
                        Icon(Icons.Default.CalendarToday, contentDescription = null, tint = IntivaColors.IconPurple, modifier = Modifier.size(24.dp))
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(text = "Tipo de meta", fontSize = 14.sp, color = IntivaColors.TextSecondary, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF2F0FA), RoundedCornerShape(12.dp))
                            .padding(6.dp)
                    ) {
                        TypeTab(
                            title = "Personal",
                            icon = Icons.Default.Person,
                            isSelected = goalType == 0,
                            onClick = { goalType = 0 },
                            modifier = Modifier.weight(1f)
                        )
                        TypeTab(
                            title = "Familiar",
                            icon = Icons.Default.Group,
                            isSelected = goalType == 1,
                            onClick = { goalType = 1 },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            
            if (goalType == 1) {
                Spacer(modifier = Modifier.height(20.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(text = "Seleccionar Grupo", fontSize = 14.sp, color = IntivaColors.TextSecondary, fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White, RoundedCornerShape(12.dp))
                                .padding(paddingValues = PaddingValues(1.dp)) 
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.White)
                                    .clickable { /* open group selector */ }
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .background(IntivaColors.PrimaryGreen, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Group, contentDescription = null, tint = IntivaColors.TextPrimary, modifier = Modifier.size(24.dp))
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Familia Torres", fontSize = 18.sp, color = IntivaColors.TextPrimary, fontWeight = FontWeight.Bold)
                                    Text("4 integrantes", fontSize = 14.sp, color = IntivaColors.TextSecondary)
                                }
                                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = IntivaColors.TextSecondary, modifier = Modifier.size(28.dp))
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}

/**
 * Tab item used in the goal type selector (Personal vs Family).
 */
@Composable
fun TypeTab(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) Color.White else Color.Transparent)
            .clickable(onClick = onClick)
            .height(48.dp)
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = if (isSelected) IntivaColors.IconPurple else IntivaColors.TextSecondary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                color = if (isSelected) IntivaColors.IconPurple else IntivaColors.TextSecondary,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 16.sp
            )
        }
    }
}
