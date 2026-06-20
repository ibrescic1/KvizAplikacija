package ba.etf.rma26.projekat.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import ba.etf.rma26.projekat.viewmodel.FilterOpcija
import ba.etf.rma26.projekat.viewmodel.KvizViewModel
import ba.etf.rma26.projekat.viewmodel.UpisViewModel

/* ekran za postavljanje filtera i upis na predmete.
  dropdown meniji za odabir godine, predmeta i grupe, dugme za upis
  dropdown za filter kvizova, broj nadjenih kvizova (ili poruku ako nema kvizova)
 */
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    kvizViewModel: KvizViewModel,
    upisViewModel: UpisViewModel,
    onPrikaziKvizove: () -> Unit
) {
    val selectedFilter   by kvizViewModel.selectedFilter.collectAsState()
    val brojKvizova      by kvizViewModel.brojKvizova.collectAsState()
    val prosjecniBodovi  by kvizViewModel.prosjecniBodovi.collectAsState()
    val isLoading        by kvizViewModel.isLoading.collectAsState()

    val odabranaGodina   by upisViewModel.odabranaGodina.collectAsState()
    val odabranPredmet   by upisViewModel.odabranPredmet.collectAsState()
    val odabranaGrupa    by upisViewModel.odabranaGrupa.collectAsState()
    val dostupniPredmeti by upisViewModel.dostupniPredmeti.collectAsState()
    val dostupneGrupe    by upisViewModel.dostupneGrupe.collectAsState()
    val mozeDaSeUpise    by upisViewModel.mozeDaSeUpise.collectAsState()

    var godinaOtvorena by remember { mutableStateOf(false) }
    var predmetOtvoren by remember { mutableStateOf(false) }
    var grupaOtvorena  by remember { mutableStateOf(false) }
    var filterOtvoren  by remember { mutableStateOf(false) }

    // ucitava kvizove pri prvom otvaranju ekrana
    LaunchedEffect(Unit) {
        kvizViewModel.osvjeziKvizove()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Postavljanje filtera") })
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Upis na predmet",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            // dropdown za godinu studija
            ExposedDropdownMenuBox(
                expanded = godinaOtvorena,
                onExpandedChange = { godinaOtvorena = !godinaOtvorena },
                modifier = Modifier.fillMaxWidth().testTag("odabirGodina")
            ) {
                OutlinedTextField(
                    value = odabranaGodina?.toString() ?: "Odaberi godinu",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Godina studija") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = godinaOtvorena)
                    },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = godinaOtvorena,
                    onDismissRequest = { godinaOtvorena = false }
                ) {
                    // prikazuje samo broj da test moze naci "2", "3" itd.
                    (1..5).forEach { godina ->
                        DropdownMenuItem(
                            text = { Text(godina.toString()) },
                            onClick = {
                                upisViewModel.odaberiGodinu(godina)
                                godinaOtvorena = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // dropdown za predmet — aktivan tek nakon odabira godine
            ExposedDropdownMenuBox(
                expanded = predmetOtvoren && odabranaGodina != null,
                onExpandedChange = {
                    if (odabranaGodina != null) predmetOtvoren = !predmetOtvoren
                },
                modifier = Modifier.fillMaxWidth().testTag("odabirPredmet")
            ) {
                OutlinedTextField(
                    value = odabranPredmet?.naziv ?: "Odaberi predmet",
                    onValueChange = {},
                    readOnly = true,
                    enabled = odabranaGodina != null,
                    label = { Text("Predmet") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = predmetOtvoren)
                    },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = predmetOtvoren && odabranaGodina != null,
                    onDismissRequest = { predmetOtvoren = false }
                ) {
                    if (dostupniPredmeti.isEmpty()) {
                        DropdownMenuItem(
                            text = { Text("Nema dostupnih predmeta") },
                            onClick = { predmetOtvoren = false }
                        )
                    } else {
                        dostupniPredmeti.forEach { predmet ->
                            DropdownMenuItem(
                                text = { Text(predmet.naziv) },
                                onClick = {
                                    upisViewModel.odaberiPredmet(predmet)
                                    predmetOtvoren = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // dropdown za grupu — aktivan tek nakon odabira predmeta
            ExposedDropdownMenuBox(
                expanded = grupaOtvorena && odabranPredmet != null,
                onExpandedChange = {
                    if (odabranPredmet != null) grupaOtvorena = !grupaOtvorena
                },
                modifier = Modifier.fillMaxWidth().testTag("odabirGrupa")
            ) {
                OutlinedTextField(
                    value = odabranaGrupa?.naziv ?: "Odaberi grupu",
                    onValueChange = {},
                    readOnly = true,
                    enabled = odabranPredmet != null,
                    label = { Text("Grupa") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = grupaOtvorena)
                    },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = grupaOtvorena && odabranPredmet != null,
                    onDismissRequest = { grupaOtvorena = false }
                ) {
                    dostupneGrupe.forEach { grupa ->
                        DropdownMenuItem(
                            text = { Text(grupa.naziv) },
                            onClick = {
                                upisViewModel.odaberiGrupu(grupa)
                                grupaOtvorena = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // dugme za upis — callback osvježava kvizove tek kad upis završi
            Button(
                onClick = {
                    upisViewModel.upisi {
                        kvizViewModel.osvjeziKvizove()
                    }
                },
                enabled = mozeDaSeUpise,
                modifier = Modifier.fillMaxWidth().testTag("dodajPredmetDugme")
            ) {
                Text("Upiši me")
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Filteri",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            // dropdown za filter kvizova
            ExposedDropdownMenuBox(
                expanded = filterOtvoren,
                onExpandedChange = { filterOtvoren = !filterOtvoren },
                modifier = Modifier.fillMaxWidth().testTag("filterKvizova")
            ) {
                OutlinedTextField(
                    value = selectedFilter.naziv,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Filter kvizova") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = filterOtvoren)
                    },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = filterOtvoren,
                    onDismissRequest = { filterOtvoren = false }
                ) {
                    FilterOpcija.entries.forEach { opcija ->
                        DropdownMenuItem(
                            text = { Text(opcija.naziv) },
                            onClick = {
                                kvizViewModel.promijeniFilter(opcija)
                                filterOtvoren = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Prikaz broja kvizova i poruke
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(8.dp))
            } else {
                when {
                    // Nema kvizova — prikaz jasne poruke
                    brojKvizova == 0 -> {
                        Text(
                            text = "Nema kvizova za odabrani filter.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.testTag("brojKvizova")
                        )
                        Text(
                            text = "Pokušajte se upisati na predmet ili promijeniti filter.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    // Ima kvizova — pokazi broj
                    else -> {
                        Text(
                            text = "Pronađeno je $brojKvizova kvizova",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.testTag("brojKvizova")
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onPrikaziKvizove,
                modifier = Modifier.fillMaxWidth().testTag("prikaziKvizoveDugme")
            ) {
                Text("Prikaži kvizove")
            }
        }
    }
}
