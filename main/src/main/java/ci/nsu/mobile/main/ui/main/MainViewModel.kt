package ci.nsu.mobile.main.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.db.DepositEntity
import ci.nsu.mobile.main.data.repository.DepositRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel(private val repository: DepositRepository) : ViewModel() {

    // Вводимые данные
    var initialAmount: Double = 0.0
    var periodMonths: Int = 0
    var interestRate: Double = 0.0
    var monthlyTopUp: Double = 0.0

    // Последний расчет
    private val _lastCalculation = MutableStateFlow<DepositEntity?>(null)
    val lastCalculation: StateFlow<DepositEntity?> get() = _lastCalculation

    fun calculateDeposit(): DepositEntity {
        val months = periodMonths
        val rate = interestRate / 100
        var total = initialAmount
        var interestAccrued = 0.0

        for (i in 1..months) {
            val interest = total * rate / 12
            interestAccrued += interest
            total += interest
            total += monthlyTopUp
        }

        val deposit = DepositEntity(
            initialAmount = initialAmount,
            periodMonths = periodMonths,
            interestRate = interestRate,
            monthlyTopUp = if (monthlyTopUp > 0) monthlyTopUp else null,
            finalAmount = total,
            interestEarned = interestAccrued,
            calculationDate = Date().time
        )

        _lastCalculation.value = deposit
        return deposit
    }

    fun saveCalculation() {
        _lastCalculation.value?.let { deposit ->
            viewModelScope.launch {
                repository.insertDeposit(deposit)
            }
        }
    }

    fun getCalculationSummary(): String {
        val deposit = _lastCalculation.value ?: return "Расчёт не выполнен"
        return """
            Стартовый взнос: ${deposit.initialAmount}
            Срок вклада (мес.): ${deposit.periodMonths}
            Процентная ставка: ${deposit.interestRate}%
            Ежемесячное пополнение: ${deposit.monthlyTopUp ?: 0.0}
            Итоговая сумма: ${deposit.finalAmount}
            Начисленные проценты: ${deposit.interestEarned}
        """.trimIndent()
    }
}