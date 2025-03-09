package no.uio.ifi.in2000.ingrihak.ingrihak.oblig2.data.votes

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.gson.gson
import no.uio.ifi.in2000.ingrihak.ingrihak.oblig2.model.votes.AggregatedVoteObject
import no.uio.ifi.in2000.ingrihak.ingrihak.oblig2.model.votes.AggregatedVotes
import no.uio.ifi.in2000.ingrihak.ingrihak.oblig2.model.votes.District
import no.uio.ifi.in2000.ingrihak.ingrihak.oblig2.model.votes.DistrictVotes


class AggregatedVotesDataSource {

    private val path = "https://in2000-proxy.ifi.uio.no/alpacaapi/v2/district3"

    private val client = HttpClient {
        install(ContentNegotiation){
            gson()
        }
    }


    suspend fun fetchVotes() : List<DistrictVotes> {

        try {
            val response : AggregatedVotes = client.get(path).body()
            val listVoteObject : List<AggregatedVoteObject> = response.parties

            val listDistrictVotes = listVoteObject.map { voteObject ->
                DistrictVotes(
                    district = District.district3,
                    alpacaPartyId = voteObject.partyId,
                    numberOfVotesForParty = voteObject.votes
                )
            }

            return listDistrictVotes

        } catch (e: Exception) {
            return emptyList()
        }

    }
}


