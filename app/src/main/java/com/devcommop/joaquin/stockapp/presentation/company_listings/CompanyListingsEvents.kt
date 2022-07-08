package com.devcommop.joaquin.stockapp.presentation.company_listings

//When we want to handle user actions, we just send Events to ViewModel and it will take care of that. Eg: Swipe Refresh, Back Button pressed, 2 finger gesture, double tap, button clicked etc.
//Although in our concerned screen clicking an item won't be considered as event because we simply navigate to new sceen, we don't wish any other action so no need to send an event for that
sealed class CompanyListingsEvents {
    object Refresh: CompanyListingsEvents()
    data class OnSearchQueryChange(val query: String): CompanyListingsEvents()
}