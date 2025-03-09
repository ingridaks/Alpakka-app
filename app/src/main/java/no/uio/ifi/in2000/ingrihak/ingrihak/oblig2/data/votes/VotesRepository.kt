package no.uio.ifi.in2000.ingrihak.ingrihak.oblig2.data.votes

import no.uio.ifi.in2000.ingrihak.ingrihak.oblig2.model.votes.District
import no.uio.ifi.in2000.ingrihak.ingrihak.oblig2.model.votes.DistrictVotes


class VotesRepository(private val aggregatedVotesDataSource: AggregatedVotesDataSource, private val individualVotesDataSource: IndividualVotesDataSource) {

    suspend fun getVotes(district: District): List<DistrictVotes> {

        val votes = when (district) {
            District.district1 -> individualVotesDataSource.fetchVotes(district)
            District.district2 -> individualVotesDataSource.fetchVotes(district)
            District.district3 -> aggregatedVotesDataSource.fetchVotes()
        }

        return votes
    }

}




