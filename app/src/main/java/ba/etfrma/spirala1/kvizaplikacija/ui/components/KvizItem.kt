package ba.etfrma.spirala1.kvizaplikacija.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import ba.etfrma.spirala1.kvizaplikacija.model.Kviz
import ba.etfrma.spirala1.kvizaplikacija.model.KvizStatus
import ba.etfrma.spirala1.kvizaplikacija.model.getStatus
import java.time.format.DateTimeFormatter
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.sp
import ba.etfrma.spirala1.kvizaplikacija.data.referentniDatum
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun KvizItem(kviz: Kviz) {

    val status = kviz.getStatus(referentniDatum)

    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    val datumZaPrikaz = when (status) {
        KvizStatus.PLAVA  -> kviz.datumRada!!.format(formatter)
        KvizStatus.ZELENA -> kviz.datumKraj.format(formatter)
        KvizStatus.CRVENA -> kviz.datumKraj.format(formatter)
        KvizStatus.ZUTA   -> kviz.datumPocetak.format(formatter)
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
                Text(
                    text = kviz.nazivPredmeta,
                    fontSize = 18.sp
                )
                Text(text = kviz.naziv)
                Text(text = datumZaPrikaz)
                Text(text = "${kviz.trajanje} min")
            }
            Column(horizontalAlignment = Alignment.End) {
                kviz.osvojeniBodovi?.let {
                    Text(text = it.toString())
                }
                StatusIcon(status = status)
            }
        }
    }
}
