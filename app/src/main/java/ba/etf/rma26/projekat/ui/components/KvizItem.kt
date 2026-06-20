package ba.etf.rma26.projekat.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ba.etf.rma26.projekat.Konstante
import ba.etf.rma26.projekat.data.models.Kviz
import ba.etf.rma26.projekat.data.models.KvizStatus
import ba.etf.rma26.projekat.data.models.getStatus
import ba.etf.rma26.projekat.domain.KvizFilterHelper
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun KvizItem(kviz: Kviz, onClick: () -> Unit = {}, onDetaljiKliknut: () -> Unit = {}) {

    val referentniDatum = Konstante.REFERENTNI_DATUM
    val status = kviz.getStatus(referentniDatum)

    val isoFormatter = DateTimeFormatter.ISO_DATE_TIME
    val prikazFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    val datumZaPrikaz = when (status) {
        KvizStatus.PLAVA  -> KvizFilterHelper.parseDate(kviz.datumPocetka).format(prikazFormatter)
        KvizStatus.ZELENA -> KvizFilterHelper.parseDate(kviz.datumKraj).format(prikazFormatter)
        KvizStatus.CRVENA -> KvizFilterHelper.parseDate(kviz.datumKraj).format(prikazFormatter)
        KvizStatus.ZUTA   -> KvizFilterHelper.parseDate(kviz.datumPocetka).format(prikazFormatter)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .semantics { testTag = "kviz_item_${kviz.naziv}" }
            .clickable { onClick() }
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
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                StatusIcon(status = status)
                Spacer(modifier = Modifier.height(8.dp))
                FilledTonalButton(
                    onClick = onDetaljiKliknut,
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = when (status) {
                            KvizStatus.ZELENA -> "Detalji"
                            KvizStatus.PLAVA  -> "Detalji"
                            KvizStatus.ZUTA   -> "Detalji"
                            KvizStatus.CRVENA -> "Detalji"
                        }
                    )
                }
            }
        }
    }
}