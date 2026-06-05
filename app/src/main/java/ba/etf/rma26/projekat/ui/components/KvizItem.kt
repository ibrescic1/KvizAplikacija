package ba.etf.rma26.projekat.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ba.etf.rma26.projekat.data.models.Kviz
import ba.etf.rma26.projekat.data.models.KvizStatus
import ba.etf.rma26.projekat.data.models.getStatus
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun KvizItem(kviz: Kviz) {

    val referentniDatum = LocalDateTime.of(2026, 4, 10, 12, 0)
    val status = kviz.getStatus(referentniDatum)

    val isoFormatter = DateTimeFormatter.ISO_DATE_TIME
    val prikazFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    val datumZaPrikaz = when (status) {
        KvizStatus.PLAVA  -> LocalDateTime.parse(kviz.datumPocetka, isoFormatter).format(prikazFormatter)
        KvizStatus.ZELENA -> LocalDateTime.parse(kviz.datumKraj, isoFormatter).format(prikazFormatter)
        KvizStatus.CRVENA -> LocalDateTime.parse(kviz.datumKraj, isoFormatter).format(prikazFormatter)
        KvizStatus.ZUTA   -> LocalDateTime.parse(kviz.datumPocetka, isoFormatter).format(prikazFormatter)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .semantics { testTag = "kviz_item_${kviz.naziv}" }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = kviz.nazivPredmeta ?: "", fontSize = 18.sp)
                Text(text = kviz.naziv)
                Text(text = datumZaPrikaz)
                Text(text = "${kviz.trajanje} min")
            }
            Column(horizontalAlignment = Alignment.End) {
                StatusIcon(status = status)
            }
        }
    }
}