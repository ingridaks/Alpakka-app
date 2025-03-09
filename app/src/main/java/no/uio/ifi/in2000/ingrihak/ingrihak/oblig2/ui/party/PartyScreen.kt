package no.uio.ifi.in2000.ingrihak.ingrihak.oblig2.ui.party

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter


@Composable
fun PartyScreen(modifier: Modifier, viewModel: PartyViewModel, navController: NavController, partyId: String) {

    viewModel.loadSinglePartyInfo(partyId)

    val party by viewModel.party.collectAsState()

    fun toARGBcolor(hexColor: String): Color {
        val cleanHex = hexColor.replace("#", "").let { "FF$it" }
        return Color(cleanHex.toLong(16))
    }

    Scaffold (
        topBar = { ApplicationTopAppBar(navController) }

    ){  innerPadding ->

        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            Text(text = party.name, fontSize = 50.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(15.dp))

            Image(
                painter = rememberAsyncImagePainter(party.img),
                contentDescription = null,
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .border(
                        BorderStroke(8.dp, toARGBcolor(party.color)),
                        CircleShape
                    ),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(40.dp))

            Card{
                Column(modifier = Modifier.padding(25.dp)) {

                    if (party.leader != "") Text(text = "Leder: ${party.leader}", fontWeight = FontWeight.Bold)

                    Spacer(modifier = Modifier.height(30.dp))

                    Text(text = party.description, fontSize = 12.sp, modifier = Modifier.padding(0.dp))
                }
            }
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicationTopAppBar(navController: NavController) {
    TopAppBar(
        title = {
            Button(
                onClick = {navController.popBackStack()},
                colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Black
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Tilbake"
                )
            }
        }
    )
}



