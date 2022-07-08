package com.devcommop.joaquin.stockapp.data.csv

import com.devcommop.joaquin.stockapp.domain.model.CompanyListing
import com.opencsv.CSVReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

//here though the constructor is empty yet we use @Inject so that user can create this class' object. E! why not provides was used ?
@Singleton
class CompanyListingsParser @Inject constructor(): CSVParser<CompanyListing> {
    override suspend fun parse(stream: InputStream): List<CompanyListing> {
        val csvReader = CSVReader(InputStreamReader(stream))
        return withContext(Dispatchers.IO){
            csvReader
                .readAll()
                .drop(1)//since api documentation me first row is names of columns & not actual data so we ignore that by using drop()
                .mapNotNull { line ->
                    val symbol = line.getOrNull(0)
                    val name = line.getOrNull(1)
                    val exchange = line.getOrNull(2)
                    CompanyListing(
                        name = name ?: return@mapNotNull null,//agar name nahi hua toh poora object ko null kar do i.e. ignore kar do real map se [ sab mapNotNull ka kamaal hai ]
                        symbol = symbol ?: return@mapNotNull  null,
                        exchange = exchange ?: return@mapNotNull null
                    )
                }//E! --> How and When will also block work. E!--> Won't csvReader be garbage collected automatically ?
                .also {
                    csvReader.close()
                }
        }
    }
}