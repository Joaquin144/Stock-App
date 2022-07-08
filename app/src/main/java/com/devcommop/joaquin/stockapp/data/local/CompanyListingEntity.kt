package com.devcommop.joaquin.stockapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CompanyListingEntity(
    val name: String,
    val symbol: String,
    val exchange: String,
    @PrimaryKey val id: Int?= null//PrimaryKey will tell room to auto-generate key for each of these rows
)
