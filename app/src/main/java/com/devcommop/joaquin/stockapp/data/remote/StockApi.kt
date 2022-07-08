package com.devcommop.joaquin.stockapp.data.remote

import com.ramcosta.composedestinations.BuildConfig
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface StockApi {
    @GET("query?function=LISTING_STATUS")
    suspend fun getListings(
        @Query("apikey") apiKey: String = API_KEY
    ): ResponseBody//ResponseBody is nothing but general byte stream of data provided by okhttp3 request. Eg CSV data ko as a byteStream lete hain pir OpenCsv se usko parse karte hain.

    @GET("query?function=TIME_SERIES_INTRADAY&interval=60min&datatype=csv")
    suspend fun getIntradayInfo(
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String = API_KEY
    ): ResponseBody

    @GET("query?function=OVERVIEW")
    suspend fun getCompanyInfo(
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String = API_KEY
    ): CompanyInfoDto

    companion object {
        const val API_KEY = "112UTO0F5QMU8DJD"
        const val BASE_URL = "https://alphavantage.co"
    }
}