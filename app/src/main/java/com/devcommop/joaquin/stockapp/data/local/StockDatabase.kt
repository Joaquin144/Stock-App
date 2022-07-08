package com.devcommop.joaquin.stockapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

//This class is kept abstract so that Room can generate its biolerplate code to creat the Dao implementation
@Database(
    entities = [CompanyListingEntity::class],
    version = 1
)
abstract class StockDatabase: RoomDatabase {
    abstract val dao: StockDao
}