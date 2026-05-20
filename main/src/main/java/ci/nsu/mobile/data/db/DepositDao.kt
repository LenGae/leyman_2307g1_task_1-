package ci.nsu.mobile.data.db

import androidx.room.*

@Dao
interface DepositDao {

    @Query("SELECT * FROM deposit_calculations WHERE userId = :userId ORDER BY calculationDate DESC")
    suspend fun getHistory(userId: Long): List<DepositCalculation>

    @Insert
    suspend fun insert(item: DepositCalculation)

    @Delete
    suspend fun delete(item: DepositCalculation)
}