package ba.etf.rma26.projekat.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ba.etf.rma26.projekat.Konstante
import ba.etf.rma26.projekat.data.models.KvizStatus
import ba.etf.rma26.projekat.data.models.getStatus
import ba.etf.rma26.projekat.data.repositories.KvizRepository
import ba.etf.rma26.projekat.data.repositories.PitanjeKvizRepository
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import ba.etf.rma26.projekat.domain.KvizFilterHelper

/*
  Ekran koji prikazuje detalje kviza prije nego korisnik pocne rjesavati.
  Korisnik vidi naziv, predmet, datum, trajanje i broj pitanja, i moze odluciti hoce li poceti kviz.
   idKviza ID kviza ciji se detalji prikazuju
   onZapocniKviz Callback za navigaciju na ekran rješavanja
   onBack Callback za povratak na listu kvizova
 */
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KvizDetaljiScreen(
    idKviza: Int,
    onZapocniKviz: (Int) -> Unit,
    onBack: () -> Unit
) {
    // Lokalno stanje ekrana
    var kvizNaziv by remember { mutableStateOf("") }
    var kvizPredmet by remember { mutableStateOf("") }
    var kvizDatum by remember { mutableStateOf("") }
    var kvizTrajanje by remember { mutableStateOf(0) }
    var kvizStatus by remember { mutableStateOf<KvizStatus?>(null) }
    var brojPitanja by remember { mutableStateOf<Int?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var greska by remember { mutableStateOf<String?>(null) }

    val prikazFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
    val scope = rememberCoroutineScope()

    // ucitavanje detalja kviza pri otvaranju ekrana
    LaunchedEffect(idKviza) {
        scope.launch {
            try {
                val kviz = KvizRepository.getById(idKviza)
                if (kviz != null) {
                    kvizNaziv = kviz.naziv
                    kvizPredmet = kviz.nazivPredmeta ?: ""
                    kvizDatum = KvizFilterHelper
                        .parseDate(kviz.datumPocetka)
                        .format(prikazFormatter)
                    kvizTrajanje = kviz.trajanje
                    kvizStatus = kviz.getStatus(Konstante.REFERENTNI_DATUM)

                    // dohvacanje broja pitanja
                    val pitanja = PitanjeKvizRepository.getPitanja(idKviza)
                    brojPitanja = pitanja.size
                } else {
                    greska = "Kviz nije pronađen."
                }
            } catch (e: Exception) {
                greska = "Greška pri učitavanju kviza."
            } finally {
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalji kviza") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Nazad")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {
            when {
                isLoading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )

                greska != null -> Text(
                    text = greska!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )

                else -> Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Naziv kviza
                    Text(
                        text = kvizNaziv,
                        style = MaterialTheme.typography.headlineMedium
                    )

                    HorizontalDivider()

                    // informacije o kvizu u karticama
                    InfoRed(label = "Predmet", vrijednost = kvizPredmet)
                    InfoRed(label = "Datum početka", vrijednost = kvizDatum)
                    InfoRed(label = "Trajanje", vrijednost = "$kvizTrajanje minuta")

                    brojPitanja?.let {
                        InfoRed(label = "Broj pitanja", vrijednost = "$it pitanja")
                    }

                    // status kviza sa bojom
                    kvizStatus?.let { status ->
                        val (statusTekst, statusBoja) = when (status) {
                            KvizStatus.ZELENA -> "Aktivan — možete rješavati" to
                                    MaterialTheme.colorScheme.primary
                            KvizStatus.ZUTA   -> "Još nije počeo" to
                                    MaterialTheme.colorScheme.secondary
                            KvizStatus.CRVENA -> "Istekao — rok je prošao" to
                                    MaterialTheme.colorScheme.error
                            KvizStatus.PLAVA  -> "Završen" to
                                    MaterialTheme.colorScheme.tertiary
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Status: ",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = statusTekst,
                                style = MaterialTheme.typography.bodyLarge,
                                color = statusBoja
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // dugme je aktivno samo ako je kviz aktivan (zeleni status ili plavi)
                    val mozePoceti = kvizStatus == KvizStatus.ZELENA ||
                            kvizStatus == KvizStatus.PLAVA

                    Button(
                        onClick = { onZapocniKviz(idKviza) },
                        enabled = mozePoceti,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            if (kvizStatus == KvizStatus.PLAVA)
                                "Nastavi kviz" else "Započni kviz"
                        )
                    }

                    // poruka zasto dugme nije aktivno
                    if (!mozePoceti) {
                        Text(
                            text = when (kvizStatus) {
                                KvizStatus.ZUTA   -> "Kviz još nije počeo."
                                KvizStatus.CRVENA -> "Rok za ovaj kviz je prošao."
                                else -> ""
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}


 //pomocni composable koji prikazuje jedan red sa labelom i vrijednoscu

@Composable
private fun InfoRed(label: String, vrijednost: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = vrijednost,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
