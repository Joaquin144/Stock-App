package com.devcommop.joaquin.stockapp.domain.repository

import com.devcommop.joaquin.stockapp.domain.model.CompanyListing
import com.devcommop.joaquin.stockapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface StockRepository {
    suspend fun getCompanyListings(
        fetchFromRemote: Boolean,//agar true hua toh DB se naya data lao warna cache wala dikha do. We will do this in SwipeRefresh action
    query: String
    ): Flow<Resource<List<CompanyListing>>>//here we must not use data layer's dto because domain should not have access to it
}