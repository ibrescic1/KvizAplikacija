package ba.etfrma.spirala1.kvizaplikacija.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.viewmodel.compose.viewModel
import ba.etfrma.spirala1.kvizaplikacija.ui.components.FilterDropdown
import ba.etfrma.spirala1.kvizaplikacija.ui.components.KvizItem
import ba.etfrma.spirala1.kvizaplikacija.viewmodel.KvizViewModel

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ba.etfrma.spirala1.kvizaplikacija.ui.components.KvizItem
import ba.etfrma.spirala1.kvizaplikacija.viewmodel.FilterOpcija
import ba.etfrma.spirala1.kvizaplikacija.viewmodel.UpisViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KvizListScreen(
    kvizViewModel: KvizViewModel = viewModel(),
    upisViewModel: UpisViewModel = viewModel()
) {
    val kvizovi by kvizViewModel.kvizovi.collectAsState()
    val selectedFilter by kvizViewModel.selectedFilter.collectAsState()

    val odabranaGodina by upisViewModel.odabranaGodina.collectAsState()
    val odabranPredmet by upisViewModel.odabranPredmet.collectAsState()
    val odabranaGrupa by upisViewModel.odabranaGrupa.collectAsState()
    val dostupniPredmeti by upisViewModel.dostupniPredmeti.collectAsState()
    val dostupneGrupe by upisViewModel.dostupneGrupe.collectAsState()

    var godinaOtvorena by remember { mutableStateOf(false) }
    var predmetOtvoren by remember { mutableStateOf(false) }
    var grupaOtvorena by remember { mutableStateOf(false) }
    var filterOtvoren by remember { mutableStateOf(false) }

    val mozeDaSeUpise by upisViewModel.mozeDaSeUpise.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {

        // ============================
        // UPIS — 3 dropdowna + dugme
        // ============================

        Text(text = "Upis na predmet", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        // Dropdown 1 — Godina
        ExposedDropdownMenuBox(
            expanded = godinaOtvorena,
            onExpandedChange = { godinaOtvorena = !godinaOtvorena },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("odabirGodina")
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
                (1..5).forEach { godina ->
                    DropdownMenuItem(
                        text = { Text("$godina. godina") },
                        onClick = {
                            upisViewModel.odaberiGodinu(godina)
                            godinaOtvorena = false
                            kvizViewModel.promijeniFilter(kvizViewModel.selectedFilter.value)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Dropdown 2 — Predmet
        ExposedDropdownMenuBox(
            expanded = predmetOtvoren,
            onExpandedChange = {
                if (odabranaGodina != null) predmetOtvoren = !predmetOtvoren
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("odabirPredmet")
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
                expanded = predmetOtvoren,
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

        // Dropdown 3 — Grupa
        ExposedDropdownMenuBox(
            expanded = grupaOtvorena,
            onExpandedChange = {
                if (odabranPredmet != null) grupaOtvorena = !grupaOtvorena
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("odabirGrupa")
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
                expanded = grupaOtvorena,
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

        // Dugme Upiši me
        Button(
            onClick = {
                upisViewModel.upisi()
                kvizViewModel.promijeniFilter(kvizViewModel.selectedFilter.value)
            },
            enabled = mozeDaSeUpise,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("dodajPredmetDugme")
        ) {
            Text("Upiši me")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ============================
        // FILTER DROPDOWN
        // ============================

        ExposedDropdownMenuBox(
            expanded = filterOtvoren,
            onExpandedChange = { filterOtvoren = !filterOtvoren },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("filterKvizova")
        ) {
            OutlinedTextField(
                value = selectedFilter.naziv,
                onValueChange = {},
                readOnly = true,
                label = { Text("Filter") },
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

        Spacer(modifier = Modifier.height(8.dp))

        // ============================
        // LISTA KVIZOVA
        // ============================

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("listaKvizova")
        ) {
            items(kvizovi) { kviz ->
                KvizItem(kviz = kviz)
            }
        }
    }
}

