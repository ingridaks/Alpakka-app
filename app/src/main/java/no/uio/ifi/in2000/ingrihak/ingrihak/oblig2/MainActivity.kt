package no.uio.ifi.in2000.ingrihak.ingrihak.oblig2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import no.uio.ifi.in2000.ingrihak.ingrihak.oblig2.ui.home.HomeScreen
import no.uio.ifi.in2000.ingrihak.ingrihak.oblig2.ui.party.PartyScreen
import no.uio.ifi.in2000.ingrihak.ingrihak.oblig2.ui.theme.Ingrihak_oblig2Theme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            Ingrihak_oblig2Theme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                ) { innerPadding ->

                    NavHost(navController = navController, startDestination = "home", builder = {

                        composable("home") {
                            HomeScreen(
                                modifier = Modifier.fillMaxSize().padding(innerPadding),
                                viewModel = viewModel(),
                                navController
                            )
                        }

                        composable("party/{partyId}") { backStackEntry ->
                            val partyId = backStackEntry.arguments?.getString("partyId")
                            if (partyId != null) {
                                PartyScreen(
                                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                                    viewModel = viewModel(),
                                    navController,
                                    partyId
                                )
                            }
                        }

                    })
                }
            }
        }
    }
}



