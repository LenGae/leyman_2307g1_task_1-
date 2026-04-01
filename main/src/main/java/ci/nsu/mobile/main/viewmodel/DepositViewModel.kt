package ci.nsu.mobile.main.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.db.DepositEntity
import ci.nsu.mobile.main.data.repository.DepositRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(private val repository: DepositRepository) : ViewModel() {

    val history = repository.getHistory()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun insertDeposit(deposit: DepositEntity) {
        viewModelScope.launch {
            repository.insertDeposit(deposit)
        }
    }
}