package ci.nsu.mobile.data.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "deposit_calculations")
data class DepositCalculation(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userLogin: String,
    val initialAmount: Double,
    val periodMonths: Int,
    val interestRate: Double,
    val monthlyTopUp: Double,
    val finalAmount: Double,
    val interestEarned: Double,
    val calculationDate: Long
) : Parcelable