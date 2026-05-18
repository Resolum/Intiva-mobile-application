package com.resolum.intiva.features.savings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.NotificationsNone
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
 * Data class representing a savings goal item.
 */
data class SavingsGoal(
    val id: String,
    val title: String,
    val savedAmount: String,
    val totalAmount: String,
    val progress: Int,
    val iconResId: Int // Placeholder for icon selection
)

/**
 * Primary savings goals dashboard list screen.
 *
 * @param onNavigateBack Callback to go back to the previous screen.
 * @param onNavigateToCreate Callback to navigate to the goal creation screen.
 * @param onNavigateToDetail Callback to view details for a specific goal.
 * @param onNavigateToEdit Callback to navigate to the goal editing screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavingsGoalsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToCreate: () -> Unit,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToEdit: (String) -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) } 
    
    val goals = listOf(
        SavingsGoal("1", "Fondo de emergencia", "2,250", "3,000", 75, 0),
        SavingsGoal("2", "Viaje familiar", "2,000", "5,000", 40, 0)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Intiva", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = IntivaColors.TextPrimary)
                    }
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Default.NotificationsNone, contentDescription = "Notifications", tint = IntivaColors.IconPurple, modifier = Modifier.size(28.dp))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = IntivaColors.BackgroundLavender)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToCreate,
                containerColor = IntivaColors.PrimaryGreen,
                contentColor = IntivaColors.TextPrimary,
                shape = CircleShape,
                modifier = Modifier.size(64.dp) 
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Goal", modifier = Modifier.size(32.dp))
            }
        },
        containerColor = IntivaColors.BackgroundLavender
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp) 
        ) {
            Text(
                text = "Mis Metas",
                fontSize = 32.sp, 
                fontWeight = FontWeight.Bold,
                color = IntivaColors.TextPrimary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Mantén el control de tus ahorros y alcanza tus objetivos financieros.",
                fontSize = 16.sp, 
                color = IntivaColors.TextSecondary,
                lineHeight = 24.sp
            )
            Spacer(modifier = Modifier.height(24.dp))
            
       
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE8E6F1), RoundedCornerShape(12.dp))
                    .padding(6.dp)
            ) {
                TabItem(
                    title = "Personales",
                    isSelected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    modifier = Modifier.weight(1f)
                )
                TabItem(
                    title = "Familiares",
                    isSelected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(bottom = 100.dp) 
            ) {
                items(goals) { goal ->
                    GoalCard(
                        goal = goal, 
                        onClick = { onNavigateToDetail(goal.id) },
                        onEditClick = { onNavigateToEdit(goal.id) }
                    )
                }
            }
        }
    }
}

/**
 * Tab item used in the selector tab row.
 */
@Composable
fun TabItem(title: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) Color.White else Color.Transparent)
            .clickable(onClick = onClick)
            .height(48.dp) 
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = if (isSelected) IntivaColors.IconPurple else IntivaColors.TextSecondary,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            fontSize = 15.sp
        )
    }
}

/**
 * Goal item card representing a single savings goal with circular and horizontal progress bars.
 */
@Composable
fun GoalCard(goal: SavingsGoal, onClick: () -> Unit, onEditClick: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp), 
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp) 
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(0xFFF2F0FA), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(modifier = Modifier.size(24.dp).background(IntivaColors.IconPurple, CircleShape))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = goal.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = IntivaColors.TextPrimary,
                    modifier = Modifier.weight(1f)
                )
                Box {
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = "Options",
                            tint = IntivaColors.TextSecondary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(Color.White)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Editar meta", color = IntivaColors.TextPrimary, fontSize = 16.sp) },
                            onClick = {
                                expanded = false
                                onEditClick()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Eliminar meta", color = IntivaColors.StatusError, fontSize = 16.sp) },
                            onClick = {
                                expanded = false
                                // TODO: Delete action implementation
                            }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Ahorrado", fontSize = 14.sp, color = IntivaColors.TextSecondary, fontWeight = FontWeight.Medium)
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(text = "S/. ", fontSize = 18.sp, color = IntivaColors.TextPrimary)
                        Text(text = goal.savedAmount, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = IntivaColors.TextPrimary)
                    }
                    Text(text = "de S/. ${goal.totalAmount}", fontSize = 14.sp, color = IntivaColors.TextSecondary)
                }
                
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(70.dp)) {
                    CircularProgressIndicator(
                        progress = { 1f },
                        modifier = Modifier.fillMaxSize(),
                        color = Color(0xFFF2F0FA),
                        strokeWidth = 7.dp
                    )
                    CircularProgressIndicator(
                        progress = { goal.progress / 100f },
                        modifier = Modifier.fillMaxSize(),
                        color = IntivaColors.PrimaryGreen,
                        strokeWidth = 7.dp,
                        trackColor = Color.Transparent
                    )
                    Text(
                        text = "${goal.progress}%",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = IntivaColors.TextPrimary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            LinearProgressIndicator(
                progress = { goal.progress / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = IntivaColors.PrimaryGreen,
                trackColor = Color(0xFFE8E6F1)
            )
        }
    }
}
