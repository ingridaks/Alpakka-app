package no.uio.ifi.in2000.ingrihak.ingrihak.oblig2.data.alpacas

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.gson.gson
import no.uio.ifi.in2000.ingrihak.ingrihak.oblig2.model.alpacas.Parties
import no.uio.ifi.in2000.ingrihak.ingrihak.oblig2.model.alpacas.PartyInfo


class AlpacaPartiesDataSource {

    private val path = "https://in2000-proxy.ifi.uio.no/alpacaapi/v2/alpacaparties"

    private val client = HttpClient {
        install(ContentNegotiation){
            gson()
        }
    }


    suspend fun fetchPartyInfo(): List<PartyInfo> {

        try {
            val response : Parties = client.get(path).body()
            return response.parties

        } catch (e: Exception) {
            Log.i("AlpacaPartiesDataSource", "Listen er tom", e)
            return listOf(PartyInfo("", "Vi har problemer med å hente ut informasjon om partiene", "", "", "", ""))
        }
    }
}


