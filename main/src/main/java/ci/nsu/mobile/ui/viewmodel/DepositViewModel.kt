package ci.nsu.mobile.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.data.db.DepositCalculation
import ci.nsu.mobile.data.repository.DepositRepository
import kotlinx.coroutines.launch

class DepositViewModel(
    private val repository: DepositRepository
) : ViewModel() {

    var history by mutableStateOf<List<DepositCalculation>>(emptyList())
        private set

    fun load(userId: Long) {
        viewModelScope.launch {
            history = repository.getHistory(userId)
        }
    }

    fun add(item: DepositCalculation, userId: Long) {
        viewModelScope.launch {
            repository.insertDeposit(item)
            load(userId)
        }
    }

    fun delete(item: DepositCalculation, userId: Long) {
        viewModelScope.launch {
            repository.deleteDeposit(item)
            load(userId)
        }
    }
}