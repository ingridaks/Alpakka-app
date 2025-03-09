package no.uio.ifi.in2000.ingrihak.ingrihak.oblig2.data.votes

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.gson.gson
import no.uio.ifi.in2000.ingrihak.ingrihak.oblig2.model.votes.District
import no.uio.ifi.in2000.ingrihak.ingrihak.oblig2.model.votes.DistrictVotes
import no.uio.ifi.in2000.ingrihak.ingrihak.oblig2.model.votes.IndividualVotes


class IndividualVotesDataSource {

    private val client = HttpClient {
        install(ContentNegotiation){
            gson()
        }
    }


    suspend fun fetchVotes(district: District) : List<DistrictVotes> {

        val numberString = when(district) {
            District.district1 -> "1"
            District.district2 -> "2"
            District.district3 -> "1"
        }

        val path = "https://in2000-proxy.ifi.uio.no/alpacaapi/v2/district$numberString"

        try {
            val response : List<IndividualVotes> = client.get(path).body()
            Log.d("IndividualVotesDataSource", "$response")

            val byVote = response.groupingBy { it.id }.eachCount()
            val listDistrictVote = byVote.map{ (id, count) ->
                DistrictVotes(district, id, count)
            }

            return listDistrictVote

        } catch (e: Exception) {
            Log.i("indVotesDataSource", "Listen er tom", e)
            return emptyList()
        }

    }
}


