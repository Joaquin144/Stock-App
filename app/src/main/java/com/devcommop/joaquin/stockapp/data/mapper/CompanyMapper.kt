package com.devcommop.joaquin.stockapp.data.mapper

import com.devcommop.joaquin.stockapp.data.local.CompanyListingEntity
import com.devcommop.joaquin.stockapp.domain.model.CompanyListing

//Extension function to convert DTO [coming raw from DB, contains annotations & libraries] to model [intended for UI,no annotatons/libraries].
//Mappers will always be amde in data layer and not domain layer beacuse data can access domain but not vice versa
fun CompanyListingEntity.toCompanyListing(): CompanyListing{
    return CompanyListing(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}

fun CompanyListing.toCompanyListingEntity(): CompanyListingEntity{
    return CompanyListingEntity(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}
