package com.devcommop.joaquin.stockapp.data.repository

import com.devcommop.joaquin.stockapp.data.local.StockDatabase
import com.devcommop.joaquin.stockapp.data.mapper.toCompanyListing
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
    val db: StockDatabase
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
            val remoteList = try{
                val response = api.getListings()
                //todo: Now read that ResponseBody as CSV file
                val responseStream = response.byteStream()
                val csvReader = CSVReader(InputStreamReader(responseStream))
            }catch(e: IOException){
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data: ${e.message}"))
            }catch(e: HttpException){
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data: ${e.message}"))
            }
        }
    }
}