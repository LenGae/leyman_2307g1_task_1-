package ci.nsu.mobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.data.db.DepositCalculation
import ci.nsu.mobile.ui.viewmodel.DepositViewModel

@Composable
fun NewCalculationScreen(
    vm: DepositViewModel,
    userId: Long,
    onDone: () -> Unit
) {

    var amount by remember { mutableStateOf("") }
    var period by remember { mutableStateOf("") }
    var rate by remember { mutableStateOf("") }
    var topUp by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("Новый расчёт", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Сумма") }
        )

        OutlinedTextField(
            value = period,
            onValueChange = { period = it },
            label = { Text("Срок") }
        )

        OutlinedTextField(
            value = rate,
            onValueChange = { rate = it },
            label = { Text("Ставка %") }
        )

        OutlinedTextField(
            value = topUp,
            onValueChange = { topUp = it },
            label = { Text("Пополнение") }
        )

        Spacer(Modifier.height(16.dp))

        Button(onClick = {

            val a = amount.toDoubleOrNull() ?: return@Button
            val p = period.toIntOrNull() ?: return@Button
            val r = rate.toDoubleOrNull() ?: return@Button
            val t = topUp.toDoubleOrNull() ?: 0.0

            var result = a
            repeat(p) {
                result += result * r / 100 + t
            }

            val deposit = DepositCalculation(
                userId = userId,
                initialAmount = a,
                periodMonths = p,
                interestRate = r,
                monthlyTopUp = t,
                finalAmount = result,
                interestEarned = result - a - t * p,
                calculationDate = System.currentTimeMillis()
            )

            vm.add(deposit, userId)
            onDone()

        }) {
            Text("Рассчитать")
        }
    }
}