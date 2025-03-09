package no.uio.ifi.in2000.ingrihak.ingrihak.oblig2.ui.party

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.ingrihak.ingrihak.oblig2.data.alpacas.AlpacaPartiesDataSource
import no.uio.ifi.in2000.ingrihak.ingrihak.oblig2.data.alpacas.AlpacaPartiesRepository
import no.uio.ifi.in2000.ingrihak.ingrihak.oblig2.model.alpacas.PartyInfo


class PartyViewModel : ViewModel() {

    private val alpacaPartiesRepository = AlpacaPartiesRepository(alpacaPartiesDataSource = AlpacaPartiesDataSource())

    private val _party = MutableStateFlow(PartyInfo("", "", "", "", "", ""))
    val party: StateFlow<PartyInfo> get() = _party

    private var loadSinglePartyInfoCalledP1 = false
    private var loadSinglePartyInfoCalledP2 = false
    private var loadSinglePartyInfoCalledP3 = false
    private var loadSinglePartyInfoCalledP4 = false

    var party1 = PartyInfo("", "", "", "", "", "")
    var party2 = PartyInfo("", "", "", "", "", "")
    var party3 = PartyInfo("", "", "", "", "", "")
    var party4 = PartyInfo("", "", "", "", "", "")



    fun loadSinglePartyInfo(partyId: String) {
        when (partyId) {
            "1" -> {
                if (loadSinglePartyInfoCalledP1 == false) loadSinglePartyInfoP(partyId)
                _party.value = party1
            }
            "2" -> {
                if (loadSinglePartyInfoCalledP2 == false) loadSinglePartyInfoP(partyId)
                _party.value = party2
            }
            "3" -> {
                if (loadSinglePartyInfoCalledP3 == false) loadSinglePartyInfoP(partyId)
                _party.value = party3
            }
            "4" -> {
                if (loadSinglePartyInfoCalledP4 == false) loadSinglePartyInfoP(partyId)
                _party.value = party4
            }
        }
    }


    fun loadSinglePartyInfoP(partyId: String) {
        viewModelScope.launch {
            val response = alpacaPartiesRepository.getSinglePartyInfo(partyId)
            when (partyId) {
                "1" -> {
                    loadSinglePartyInfoCalledP1 = true
                    party1 = response
                    _party.value = party1
                }
                "2" -> {
                    loadSinglePartyInfoCalledP2 = true
                    party2 = response
                    _party.value = party2
                }
                "3" -> {
                    loadSinglePartyInfoCalledP3 = true
                    party3 = response
                    _party.value = party3
                }
                "4" -> {
                    loadSinglePartyInfoCalledP4 = true
                    party4 = response
                    _party.value = party4
                }
            }
        }
    }
}

