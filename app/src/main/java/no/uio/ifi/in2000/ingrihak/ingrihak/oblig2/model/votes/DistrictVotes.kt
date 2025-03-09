package no.uio.ifi.in2000.ingrihak.ingrihak.oblig2.model.votes

data class DistrictVotes(
    val district: District,
    val alpacaPartyId: String,
    var numberOfVotesForParty: Int
)

