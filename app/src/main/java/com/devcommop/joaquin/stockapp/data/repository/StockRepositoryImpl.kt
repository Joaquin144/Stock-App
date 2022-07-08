package com.devcommop.joaquin.stockapp.data.repository

import com.devcommop.joaquin.stockapp.data.csv.CSVParser
import com.devcommop.joaquin.stockapp.data.local.StockDatabase
import com.devcommop.joaquin.stockapp.data.mapper.toCompanyListing
import com.devcommop.joaquin.stockapp.data.mapper.toCompanyListingEntity
import com.devcommop.joaquin.stockapp.data.remote.StockApi
import com.devcommop.joaquin.stockapp.domain.model.CompanyListing
import com.devcommop.joaquin.stockapp.domain.repository.StockRepository
import com.devcommop.joaquin.stockapp.util.Resource
import com.opencsv.CSVReader
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    val api: StockApi,
    val db: StockDatabase,
    val companyListingsParser: CSVParser<CompanyListing>
): StockRepository {

    private val dao = db.dao

    override suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> {
        //below code is called as a Flow builder block
        return flow{
            emit(Resource.Loading(true))
            val localListings = dao.searchCompanyListing(query)
            emit(Resource.Success(
                data = localListings.map {
                    it.toCompanyListing()
                }
            ))
            val isDbEmpty = localListings.isEmpty() && query.isBlank()
            val shouldJustLoadFromCache = !isDbEmpty && !fetchFromRemote
            if(shouldJustLoadFromCache){
                emit(Resource.Loading(false))
                return@flow//flow se bahar aa jao agey lines execute mat karo. E! why -> Simple return won't work
            }
            val remoteListings = try{
                val response = api.getListings()
                //Now read that ResponseBody as CSV file
                val responseStream = response.byteStream()
                companyListingsParser.parse(responseStream)
            }catch(e: IOException){
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data: ${e.message}"))
                null
            }catch(e: HttpException){
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data: ${e.message}"))
                null
            }
            remoteListings?.let { listings ->
                dao.clearCompanyListings()
                dao.insertCompanyListings(//here we implement single source of truth. i.e. we show give data to UI only from cache and not from api. To do so we first store api data in cache and then give the cached data to ui
                    listings.map { it.toCompanyListingEntity() }
                )
                emit(Resource.Loading(false))//E! --> why this line giving error when placed at last
                emit(Resource.Success(
                    data = dao
                        .searchCompanyListing("")
                        .map{ it.toCompanyListing() }
                ))
            }
        }
    }
}