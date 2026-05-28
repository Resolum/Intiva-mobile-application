import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.resolum.intiva.features.finances.domain.models.TransactionGroupByDate
import com.resolum.intiva.features.finances.presentation.transactions.components.TransactionItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * This composable displays a section of transactions grouped by a specific date.
 * It shows the date as a header and lists all transactions that occurred on that date.
 *
 * @param group The data model containing the date and the list of transactions for that date.
 */
@Composable
fun TransactionDateSection(
    group: TransactionGroupByDate
) {
    Column {
        Text(
            text = formatDate(group.date),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            group.transactions.forEach { transaction ->
                TransactionItem(transaction = transaction)
            }
        }
    }
}


private fun formatDate(date: String): String {
    return runCatching {
        val parsedDate = LocalDate.parse(date)
        parsedDate.format(DateTimeFormatter.ofPattern("dd 'de' MMMM, yyyy"))
    }.getOrElse { date }
}