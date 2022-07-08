package com.devcommop.joaquin.stockapp.presentation.company_listings

import com.devcommop.joaquin.stockapp.domain.model.CompanyListing

//holds the state of the UI
data class CompanyListingsState(
    val companies: List<CompanyListing> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,//jab swiperefrsh hoga tab isko true kar dnege and phir wapas se false
    val searchQuery: String = ""
)