package com.devcommop.joaquin.stockapp.presentation.company_listings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devcommop.joaquin.stockapp.domain.repository.StockRepository
import com.devcommop.joaquin.stockapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyListingsViewModel @Inject constructor(
    private val repository: StockRepository
): ViewModel() {
    var state by mutableStateOf(CompanyListingsState())
    private var searchjob: Job? = null//Gyan Ki Baat => jaise jaise user type karta jayega hum usko live(just a little bit intentional delay) search results deikhate rahneg iske liye humko Job ka use karna hota hai. Ghabrayein bilkul mat 😁

    fun onEvent(event: CompanyListingsEvents){
        when(event){
            is CompanyListingsEvents.Refresh -> {
                getCompanyListings(fetchFromRemote = true)
            }
            is CompanyListingsEvents.OnSearchQueryChange -> {
                state = state.copy(searchQuery = event.query)
                searchjob?.cancel()//agar phele se kuch search kar rahe ho toh ruk jao abhi naya search karna padega
                searchjob = viewModelScope.launch {
                    delay(500L)//intentional delay
                    getCompanyListings()
                }
            }
        }
    }

    private fun getCompanyListings(
        fetchFromRemote: Boolean = false,
        query: String = state.searchQuery.lowercase()
    ){
        viewModelScope.launch {
            repository
                .getCompanyListings(fetchFromRemote,query)
                .collect{ result ->
                    when(result){
                        is Resource.Success -> {
                            result.data?.let { listings ->
                                state = state.copy(//with copy we don't need to have var fields inside state.
                                    companies = listings
                                )
                            }
                        }
                        is Resource.Error -> Unit // todo: Error handling on screen karo
                        is Resource.Loading -> {
                            state = state.copy(isLoading = result.isLoading)
                        }
                    }
                }
        }
    }
}