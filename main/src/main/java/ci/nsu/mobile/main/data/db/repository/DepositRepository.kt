package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.db.DepositDao
import ci.nsu.mobile.main.data.db.DepositEntity
import kotlinx.coroutines.flow.Flow

class DepositRepository(private val dao: DepositDao) {
    suspend fun insertDeposit(deposit: DepositEntity) = dao.insert(deposit)
    fun getHistory(): Flow<List<DepositEntity>> = dao.getAllDeposits()
}