package com.devcommop.joaquin.stockapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
//here we define our functions for local DB eg how to insert/read/delete data from RomDB
interface  StockDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompanyListings(
        companyListingEntities: List<CompanyListingEntity>
    )

    @Query("DELETE FROM companylistingentity")//companylistingentity class is auto generated by room since CompanyListingEntity was annotated with @Entity
    suspend fun clearCompanyListings()

    @Query(
        """
            SELECT *
            FROM companylistingentity
            WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%' OR
                UPPER(:query) == symbol
        """
    )
    suspend fun searchCompanyListing(query: String): List<CompanyListingEntity >
}