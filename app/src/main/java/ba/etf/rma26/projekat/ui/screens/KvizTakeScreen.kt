package ba.etf.rma26.projekat.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import ba.etf.rma26.projekat.viewmodel.KvizTakeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KvizTakeScreen(
    idKviza: Int,
    viewModel: KvizTakeViewModel,
    onBack: () -> Unit
) {
    val pitanja by viewModel.pitanja.collectAsState()
    val kvizTaken by viewModel.kvizTaken.collectAsState()
    val ukupniBodovi by viewModel.ukupniBodovi.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val greska by viewModel.greska.collectAsState()

    val odabraniOdgovori by viewModel.odabraniOdgovori.collectAsState()
    val porukeOdgovora by viewModel.porukeOdgovora.collectAsState()

    LaunchedEffect(idKviza) {
        viewModel.zapocniKviz(idKviza)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rješavanje kviza") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Nazad")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            when {
                isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                greska != null -> Text("Greška: $greska", color = MaterialTheme.colorScheme.error)
                kvizTaken != null -> {
                    ukupniBodovi?.let {
                        Text(
                            "Ukupno bodova: $it",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier
                                .padding(bottom = 16.dp)
                                .testTag("ukupniBodovi")
                        )
                    }

                    LazyColumn(modifier = Modifier.testTag("listaPitanja")) {
                        items(pitanja) { pitanje ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .testTag("pitanje_${pitanje.id}")
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        pitanje.tekstPitanja,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))

                                    val odabraniIndeks = odabraniOdgovori[pitanje.id]

                                    pitanje.opcije.forEachIndexed { index, opcija ->
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            RadioButton(
                                                selected = odabraniIndeks == index,
                                                onClick = {
                                                    // dozvoljavamo promjenu ako je kliknuta bilo koja druga opcija
                                                    if (odabraniIndeks != index) {
                                                        viewModel.posaljiOdgovor(pitanje.id, index)
                                                    }
                                                },
                                                modifier = Modifier.testTag("odgovor_${pitanje.id}_$index")
                                            )
                                            Text(opcija)
                                        }
                                    }

                                    porukeOdgovora[pitanje.id]?.let { poruka ->
                                        Spacer(modifier = Modifier.height(8.dp))
                                        val bojaTeksta = if (poruka == "Tačno!")
                                            MaterialTheme.colorScheme.primary
                                        else
                                            MaterialTheme.colorScheme.error

                                        Text(
                                            text = poruka,
                                            color = bojaTeksta,
                                            style = MaterialTheme.typography.bodyMedium,
                                            modifier = Modifier.padding(start = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}