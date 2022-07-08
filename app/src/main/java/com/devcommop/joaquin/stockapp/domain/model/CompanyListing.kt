package com.devcommop.joaquin.stockapp.domain.model

data class CompanyListing(
    val name: String,
    val symbol: String,
    val exchange: String
    //Maps data to domain so that presentation layer won't have access to the data layer. Rather all the classes in domain are directly shown in the ui. This is done because if we decide to change our DB in future from Room to Realm then we only need to work on data layer part meanwhile domain and ui remains untouched, so that project is easily upgradable. In other words domain which lies just below data layer has no knowledge of 3rd party libraries it just gives data to ui when asked.

    //Don' be confused that domain resides below presentation. Actually domain in innermost layer which contains core business logic. eg. models for grocercy items. How you insert actual data into those models is task of data layer either through Firerbase/Django/Stapi/MongoDB/Room/Realm/Reddis etc. Also remember that outer layers could always access inner layers but not vice versa in clean architecture. So domain layer can't access ui or data.
)