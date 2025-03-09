package no.uio.ifi.in2000.ingrihak.ingrihak.oblig2.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.ingrihak.ingrihak.oblig2.data.alpacas.AlpacaPartiesDataSource
import no.uio.ifi.in2000.ingrihak.ingrihak.oblig2.data.alpacas.AlpacaPartiesRepository
import no.uio.ifi.in2000.ingrihak.ingrihak.oblig2.model.alpacas.PartyInfo
import no.uio.ifi.in2000.ingrihak.ingrihak.oblig2.model.votes.District


class HomeScreenViewModel : ViewModel() {

    private val alpacaPartiesRepository = AlpacaPartiesRepository(alpacaPartiesDataSource = AlpacaPartiesDataSource())

    private val _parties = MutableStateFlow<List<PartyInfo>>(emptyList())
    val parties: StateFlow<List<PartyInfo>> get() = _parties

    private val _chosenDistrict = MutableStateFlow<District?>(null)
    val chosenDistrict: MutableStateFlow<District?> get() = _chosenDistrict

    private val _showVoteList = MutableStateFlow(false)
    val showVoteList: MutableStateFlow<Boolean> get() = _showVoteList

    private val _votes = MutableStateFlow(HashMap<String, Int>())
    val votes: StateFlow<HashMap<String, Int>> get() = _votes

    private var getPartyInfoCalled = false
    private var stemmerOgPartinavnCalledD1 = false
    private var stemmerOgPartinavnCalledD2 = false
    private var stemmerOgPartinavnCalledD3 = false

    var votesD1 = (HashMap<String, Int>())
    var votesD2 = (HashMap<String, Int>())
    var votesD3 = (HashMap<String, Int>())


    fun loadPartyInfo() {
        if (getPartyInfoCalled == false) {
            viewModelScope.launch {
                val response = alpacaPartiesRepository.getPartyInfo()
                _parties.value = response
                getPartyInfoCalled = true
            }
        }
    }


    fun loadVotes(district: District) {
        _chosenDistrict.value = district
        when (district) {
            District.district1 -> {
                if (stemmerOgPartinavnCalledD1 == false) loadVotesD(district)
                _votes.value = votesD1
            }
            District.district2 -> {
                if (stemmerOgPartinavnCalledD2 == false) loadVotesD(district)
                _votes.value = votesD2
            }
            District.district3 -> {
                if (stemmerOgPartinavnCalledD3 == false) loadVotesD(district)
                _votes.value = votesD3
            }
        }
    }


    private fun loadVotesD(district: District) {
        viewModelScope.launch {
            val response = alpacaPartiesRepository.stemmerOgPartinavn(district)
            when (district) {
                District.district1 -> {
                    stemmerOgPartinavnCalledD1 = true
                    votesD1 = response
                    _votes.value = votesD1
                }
                District.district2 -> {
                    stemmerOgPartinavnCalledD2 = true
                    votesD2 = response
                    _votes.value = votesD2
                }
                District.district3 -> {
                    stemmerOgPartinavnCalledD3 = true
                    votesD3 = response
                    _votes.value = votesD3
                }
            }
            _showVoteList.value = true
        }
    }
}


