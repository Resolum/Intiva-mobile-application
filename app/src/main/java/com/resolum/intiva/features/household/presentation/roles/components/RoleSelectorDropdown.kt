package com.resolum.intiva.features.household.presentation.roles.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.resolum.intiva.features.household.domain.models.FamilyRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoleSelectorDropdown(
    currentRole: String,
    onRoleSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember(currentRole) { mutableStateOf(getRoleDisplayName(currentRole)) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { if (enabled) expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedText,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            enabled = enabled
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            FamilyRole.entries.forEach { role ->
                DropdownMenuItem(
                    text = { Text(text = getRoleDisplayName(role.name)) },
                    onClick = {
                        selectedText = getRoleDisplayName(role.name)
                        onRoleSelected(role.name)
                        expanded = false
                    }
                )
            }
        }
    }
}

private fun getRoleDisplayName(role: String): String {
    return when (role) {
        "ADMIN" -> "Administrador"
        "MEMBER" -> "Miembro"
        else -> role
    }
}
