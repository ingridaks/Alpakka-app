package no.uio.ifi.in2000.ingrihak.ingrihak.oblig2.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import no.uio.ifi.in2000.ingrihak.ingrihak.oblig2.model.alpacas.PartyInfo
import no.uio.ifi.in2000.ingrihak.ingrihak.oblig2.model.votes.District


@Composable
fun HomeScreen(modifier: Modifier, viewModel: HomeScreenViewModel, navController: NavController) {

    viewModel.loadPartyInfo()

    val parties by viewModel.parties.collectAsState()
    val chosenDistrict by viewModel.chosenDistrict.collectAsState()
    val showVoteList by viewModel.showVoteList.collectAsState()
    val votes by viewModel.votes.collectAsState()

    Column {
        Cards(modifier, navController, parties)

        Spacer(modifier = Modifier.height(20.dp))

        DropDown(viewModel, chosenDistrict)

        Spacer(modifier = Modifier.height(10.dp))

        VoteList(votes, showVoteList)
    }
}



@Composable
fun Cards(modifier: Modifier, navController: NavController, parties: List<PartyInfo>){

    val pairedParties = parties.chunked(2)

    Box(
        Modifier.height(560.dp)
    ) {
        LazyColumn(modifier = modifier) {
            items(pairedParties) { pair ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    for (party in pair) {
                        AlpacaCard(modifier.weight(1f), party, navController)
                    }
                }
            }
        }
    }
}



@Composable
fun AlpacaCard(modifier: Modifier, partyInfo: PartyInfo, navController: NavController) {

    val partyId  = partyInfo.id

    fun toARGBcolor(hexColor: String): Color {
        val cleanHex = hexColor.replace("#", "").let { "FF$it" }
        return Color(cleanHex.toLong(16))
    }

    Card(
        onClick = {navController.navigate("party/$partyId")},
        colors = CardDefaults.cardColors(containerColor = toARGBcolor(partyInfo.color)),
        modifier = modifier
            .padding(0.dp)
    ) {
        Column(modifier = Modifier
            .padding(15.dp)
            .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(5.dp))

            Text(text = partyInfo.name, fontSize = 22.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(20.dp))

            Image(
                painter = rememberAsyncImagePainter(partyInfo.img),
                contentDescription = null,
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (partyInfo.leader != "") Text(text = "Leder: ${partyInfo.leader}")

            Spacer(modifier = Modifier.height(7.dp))
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDown(viewModel: HomeScreenViewModel, chosenDistrict: District?) {

    var expanded by remember { mutableStateOf(false) }
    var tekst by remember { mutableStateOf("Se stemmer") }
    val options = listOf(District.district1, District.district2, District.district3)
    val showToUser: HashMap<District, String> = hashMapOf(
        District.district1 to "Distrikt 1",
        District.district2 to "Distrikt 2",
        District.district3 to "Distrikt 3",
    )

    if (chosenDistrict != null) tekst = showToUser[chosenDistrict].toString()


    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {

        TextField(
            value = tekst,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
                .clip(RoundedCornerShape(15.dp)),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { showToUser[option]?.let { Text(it) } },
                    onClick = {
                        expanded = false
                        viewModel.loadVotes(option)
                    }
                )
            }
        }
    }
}



@Composable
fun VoteList(votes: HashMap<String, Int>, showContent: Boolean){

    val headers = listOf("Parti", "Antall stemmer")

    if (showContent) {


        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)

        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            ) {
                headers.forEach { columnText ->
                    TableCell(
                        text = columnText,
                        modifier = Modifier.weight(1f),
                        textStyle = TextStyle(fontWeight = FontWeight.Bold, fontSize = 17.sp)
                    )
                }
            }

            votes.forEach { (key, value) ->
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(1.dp)
                ) {
                    TableCell(text = key, modifier = Modifier.weight(1f))
                    TableCell(text = value.toString(), modifier = Modifier.weight(1f))
                }
            }
        }
    }}
}



@Composable
fun TableCell(text: String, modifier: Modifier = Modifier, textStyle: TextStyle = TextStyle()) {
    Box(
        modifier = modifier
            .padding(4.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        ) {
        BasicText(text, style = textStyle)
    }
}


