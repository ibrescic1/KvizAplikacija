package ba.etf.rma26.projekat.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import ba.etf.rma26.projekat.R
import ba.etf.rma26.projekat.data.models.KvizStatus

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StatusIcon(status: KvizStatus) {

    val slikaId = when (status) {
        KvizStatus.PLAVA   -> R.drawable.plava
        KvizStatus.ZELENA  -> R.drawable.zelena
        KvizStatus.ZUTA    -> R.drawable.zuta
        KvizStatus.CRVENA  -> R.drawable.crvena
    }

    val opis = when (status) {
        KvizStatus.PLAVA   -> "Plava"
        KvizStatus.ZELENA  -> "Zelena"
        KvizStatus.ZUTA    -> "Žuta"
        KvizStatus.CRVENA  -> "Crvena"
    }

    Image(
        painter = painterResource(id = slikaId),
        contentDescription = opis,
        modifier = Modifier.semantics { contentDescription = opis }
            .testTag("kviz_status_icon")
    )
}

