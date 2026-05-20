package ci.nsu.mobile.data.repository

import ci.nsu.mobile.data.db.DepositCalculation
import ci.nsu.mobile.data.db.DepositDao

class DepositRepository(private val dao: DepositDao) {

    suspend fun insertDeposit(item: DepositCalculation) {
        dao.insert(item)
    }

    suspend fun getHistory(userId: Long): List<DepositCalculation> {
        return dao.getHistory(userId)
    }

    suspend fun deleteDeposit(item: DepositCalculation) {
        dao.delete(item)
    }
}