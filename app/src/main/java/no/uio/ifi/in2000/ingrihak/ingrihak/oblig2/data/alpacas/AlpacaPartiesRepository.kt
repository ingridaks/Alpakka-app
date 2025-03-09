package no.uio.ifi.in2000.ingrihak.ingrihak.oblig2.data.alpacas

import no.uio.ifi.in2000.ingrihak.ingrihak.oblig2.data.votes.AggregatedVotesDataSource
import no.uio.ifi.in2000.ingrihak.ingrihak.oblig2.data.votes.IndividualVotesDataSource
import no.uio.ifi.in2000.ingrihak.ingrihak.oblig2.data.votes.VotesRepository
import no.uio.ifi.in2000.ingrihak.ingrihak.oblig2.model.alpacas.PartyInfo
import no.uio.ifi.in2000.ingrihak.ingrihak.oblig2.model.votes.District


class AlpacaPartiesRepository(private val alpacaPartiesDataSource: AlpacaPartiesDataSource) {

    suspend fun getPartyInfo(): List<PartyInfo> {
        return alpacaPartiesDataSource.fetchPartyInfo()
    }


    suspend fun getSinglePartyInfo(partyId : String): PartyInfo {
        val liste = alpacaPartiesDataSource.fetchPartyInfo()
        val partyInfoObject = liste.firstOrNull { item ->
            item.id == partyId
        }
        var partyInfoObjectIkkeNull = liste.first()
        if (partyInfoObject != null) partyInfoObjectIkkeNull = partyInfoObject
        return partyInfoObjectIkkeNull
    }


    suspend fun stemmerOgPartinavn(district: District) : HashMap<String, Int> {
        val votesRepository = VotesRepository(
            aggregatedVotesDataSource = AggregatedVotesDataSource(),
            individualVotesDataSource = IndividualVotesDataSource())
        val listDistrictVotes = votesRepository.getVotes(district)
        val listePartyInfo = alpacaPartiesDataSource.fetchPartyInfo()
        val hashMap = listDistrictVotes
            .mapNotNull { districtVotes ->
                val partiNavn = listePartyInfo.find { partyInfo -> partyInfo.id == districtVotes.alpacaPartyId }?.name
                if (partiNavn != null) {
                    partiNavn to districtVotes.numberOfVotesForParty
                } else {
                    null
                }
            }
            .toMap(HashMap())
        return hashMap
    }
}


