package ba.etf.rma26.projekat

import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Rule
import org.junit.Test
import java.lang.AssertionError

class Spirala1GradingTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()



    private fun isFlexibleMatch(actual: String?, expected: String): Boolean {
        if (actual == null) return false
        val normalize = { str: String ->
            str.lowercase()
                .replace(Regex("[\\s\\-_.\\(\\)]+"), "")
                .replace("č", "c").replace("ć", "c")
                .replace("ž", "z").replace("š", "s")
                .replace("đ", "d").replace("dj", "d")
        }
        val normActual = normalize(actual)
        val normExpected = normalize(expected)
        return normActual.contains(normExpected) || normExpected.contains(normActual)
    }

    private fun hasFlexibleText(expectedText: String): SemanticsMatcher =
        SemanticsMatcher("Fleksibilan tekst: $expectedText") { node ->
            val texts = node.config.getOrNull(SemanticsProperties.Text)?.map { it.text } ?: emptyList()
            texts.any { isFlexibleMatch(it, expectedText) }
        }

    private fun hasFlexibleContentDescription(expectedDesc: String): SemanticsMatcher =
        SemanticsMatcher("Fleksibilan opis: $expectedDesc") { node ->
            val descs = node.config.getOrNull(SemanticsProperties.ContentDescription) ?: emptyList()
            descs.any { isFlexibleMatch(it, expectedDesc) }
        }

    private fun hasFlexibleTestTag(expectedTag: String): SemanticsMatcher =
        SemanticsMatcher("Fleksibilan tag: $expectedTag") { node ->
            val tag = node.config.getOrNull(SemanticsProperties.TestTag)
            if (tag == null) false
            else {
                val normalize = { str: String -> str.lowercase().replace(Regex("[\\s\\-_.]"), "") }
                val normActual = normalize(tag)
                val normExpected = normalize(expectedTag)
                normActual.contains(normExpected) || normExpected.contains(normActual)
            }
        }

    private fun hasAnyText(): SemanticsMatcher = SemanticsMatcher("Sadrži tekst") { node ->
        val texts = node.config.getOrNull(SemanticsProperties.Text)?.map { it.text } ?: emptyList()
        texts.any { it.trim().isNotEmpty() }
    }


    private fun najfleksibilnijiMatcher(tagId: String, kljucnaRijec: String): SemanticsMatcher {
        return hasFlexibleTestTag(tagId) or
                hasFlexibleText(kljucnaRijec) or
                hasFlexibleContentDescription(kljucnaRijec) or
                SemanticsMatcher("Sadrži riječ $kljucnaRijec u bilo kojoj formi") { node ->
                    val tag = node.config.getOrNull(SemanticsProperties.TestTag) ?: ""
                    val text = node.config.getOrNull(SemanticsProperties.Text)?.joinToString(" ") { it.text } ?: ""
                    val desc = node.config.getOrNull(SemanticsProperties.ContentDescription)?.joinToString(" ") ?: ""
                    val combine = "$tag $text $desc".lowercase()
                    combine.contains(kljucnaRijec.lowercase())
                }
    }

    private fun naPrvi(matcher: SemanticsMatcher): SemanticsNodeInteraction {
        return composeTestRule.onAllNodes(matcher, useUnmergedTree = true).onFirst()
    }

    private fun osigurajPrikazListe() {
        composeTestRule.waitForIdle()
        val listaPostoji = composeTestRule.onAllNodes(hasFlexibleTestTag("listaKvizova")).fetchSemanticsNodes().isNotEmpty()
        if (!listaPostoji) {
            val navigacijaDugme = composeTestRule.onAllNodes(najfleksibilnijiMatcher("prikaziKvizoveDugme", "prikazi"))
            if (navigacijaDugme.fetchSemanticsNodes().isNotEmpty()) {
                navigacijaDugme.onFirst().performClick()
                composeTestRule.waitForIdle()
            }
        }
    }

    private fun popupImaTekst(): Boolean {
        return composeTestRule.onAllNodes(hasAnyText() and hasAnyAncestor(isPopup()), useUnmergedTree = true)
            .fetchSemanticsNodes()
            .isNotEmpty()
    }

    private fun sacekajPopupDaSeZatvori() {
        try {
            composeTestRule.waitUntil(timeoutMillis = 1500) {
                !popupImaTekst()
            }
        } catch (_: Exception) {
        }
    }

    private fun sacekajPopupDaSeOtvori(): Boolean {
        for (i in 0..15) {
            if (popupImaTekst()) return true
            Thread.sleep(200)
            composeTestRule.waitForIdle()
        }
        return false
    }

    private fun klikniOpcijuDropdowna(opcijaMatcher: SemanticsMatcher): Boolean {
        val opcije = composeTestRule.onAllNodes(opcijaMatcher and hasAnyAncestor(isPopup()), useUnmergedTree = true)
        if (opcije.fetchSemanticsNodes().isEmpty()) return false

        val opcija = opcije.onFirst()
        try { opcija.performScrollTo(); composeTestRule.waitForIdle() } catch (_: Throwable) {}
        try {
            opcija.performClick()
        } catch (e: Exception) {
            opcija.performSemanticsAction(SemanticsActions.OnClick)
        }
        composeTestRule.waitForIdle()
        sacekajPopupDaSeZatvori()
        return true
    }

    private fun odaberiOpcijuDropdowna(anchorMatcher: SemanticsMatcher, opcijaMatcher: SemanticsMatcher, opis: String): Boolean {
        val anchorNodes = composeTestRule.onAllNodes(anchorMatcher, useUnmergedTree = true)
        if (anchorNodes.fetchSemanticsNodes().isEmpty()) {
            throw Exception("Dropdown '$opis' nije pronađen na ekranu.")
        }

        val anchor = anchorNodes.onFirst()
        try { anchor.performScrollTo(); composeTestRule.waitForIdle() } catch (_: Throwable) {}

        anchor.performClick()
        composeTestRule.waitForIdle()

        if (!sacekajPopupDaSeOtvori()) {
            try { anchor.performClick(); composeTestRule.waitForIdle() } catch (e: Exception) {}
            return false
        }

        return klikniOpcijuDropdowna(opcijaMatcher)
    }


    private fun siguranKlikNaDropdown(tag: String, kljucnaRijec: String): Boolean {
        return odaberiOpcijuDropdowna(
            anchorMatcher = najfleksibilnijiMatcher(tag, kljucnaRijec),
            opcijaMatcher = hasAnyText(),
            opis = kljucnaRijec
        )
    }

    private fun odaberiGodinuPoIndeksu(indeksGodine: Int): Boolean {
        val anchorNodes = composeTestRule.onAllNodes(najfleksibilnijiMatcher("odabirGodina", "godin"), useUnmergedTree = true)
        if (anchorNodes.fetchSemanticsNodes().isEmpty()) {
            throw Exception("Element za odabir godine nije pronađen na ekranu.")
        }

        val anchor = anchorNodes.onFirst()
        try { anchor.performScrollTo(); composeTestRule.waitForIdle() } catch (_: Throwable) {}

        anchor.performClick()
        composeTestRule.waitForIdle()

        if (!sacekajPopupDaSeOtvori()) {
            try { anchor.performClick(); composeTestRule.waitForIdle() } catch (e: Exception) {}
            return false
        }

        val opcijeGodine = composeTestRule.onAllNodes(hasAnyText() and hasAnyAncestor(isPopup()), useUnmergedTree = true)
        val brojOpcija = opcijeGodine.fetchSemanticsNodes().size
        if (indeksGodine >= brojOpcija) {
            try { anchor.performClick(); composeTestRule.waitForIdle() } catch (e: Exception) {}
            return false
        }

        val opcijaGodine = opcijeGodine[indeksGodine]
        try { opcijaGodine.performScrollTo(); composeTestRule.waitForIdle() } catch (_: Throwable) {}
        try {
            opcijaGodine.performClick()
        } catch (e: Exception) {
            opcijaGodine.performSemanticsAction(SemanticsActions.OnClick)
        }

        composeTestRule.waitForIdle()
        sacekajPopupDaSeZatvori()
        return true
    }

    private fun popuniFormuZaUpis(): Pair<Boolean, String> {
        for (indeksGodine in 0..4) {
            try {
                if (!odaberiGodinuPoIndeksu(indeksGodine)) {
                    return false to "Nema više opcija u meniju za godine."

                }

                Thread.sleep(300)
                composeTestRule.waitForIdle()

                if (!siguranKlikNaDropdown("odabirPredmet", "predm")) {
                    continue
                }

                Thread.sleep(300)
                composeTestRule.waitForIdle()

                if (!siguranKlikNaDropdown("odabirGrupa", "grup")) {
                    continue
                }

                composeTestRule.onNode(hasFlexibleTestTag("dodajPredmetDugme")).assertIsEnabled()
                return true to "OK"
            } catch (e: AssertionError) {
                return false to (e.message ?: "Dugme nije omogućeno nakon odabira svih vrijednosti.")
            } catch (e: Exception) {
                val poruka = e.message ?: "Nepoznata greška"
                if (poruka.contains("nije pronađen", ignoreCase = true)) {
                    return false to poruka
                }
                sacekajPopupDaSeZatvori()
                continue
            }
        }

        return false to "Nije pronađena kombinacija godine, predmeta i grupe koja omogućava dugme."
    }

    // =========================================================================
    // TESTOVI ZADATAK 1
    // =========================================================================

    @Test
    fun test01_osnovniUiElementiPostoje() {
        try {
            naPrvi(hasFlexibleTestTag("filterKvizova")).assertExists()
            osigurajPrikazListe()
            naPrvi(hasFlexibleTestTag("listaKvizova")).assertExists()
        } catch (e: AssertionError) {
            throw AssertionError("PAD: Nisu pronađeni 'listaKvizova' ili 'filterKvizova'.")
        }
    }

    @Test
    fun test02_filterSadrziSveOpcije() {
        val anchor = naPrvi(hasFlexibleTestTag("filterKvizova"))
        anchor.performClick()
        composeTestRule.waitForIdle()

        val obavezneOpcije = listOf("Svi moji kvizovi", "Svi kvizovi", "Urađeni kvizovi", "Budući kvizovi", "Prošli kvizovi")

        obavezneOpcije.forEach { opcija ->
            try {
                composeTestRule.onNode(hasFlexibleText(opcija), useUnmergedTree = true).assertExists()
            } catch (e: AssertionError) {
                try { anchor.performClick(); composeTestRule.waitForIdle() } catch (ignore: Exception) {}
                throw AssertionError("PAD: Opcija '$opcija' nije pronađena u meniju filtera.")
            }
        }
        try { anchor.performClick(); composeTestRule.waitForIdle() } catch (ignore: Exception) {}
    }

    @Test
    fun test03_elementiListeImajuIspravanTestTagFormat() {
        osigurajPrikazListe()
        val hasKvizItemTag = SemanticsMatcher("Tag kviz_item_") { node ->
            node.config.getOrNull(SemanticsProperties.TestTag)?.lowercase()?.replace("_", "")?.startsWith("kvizitem") == true
        }

        try {
            composeTestRule.waitUntil(timeoutMillis = 3000) {
                composeTestRule.onAllNodes(hasKvizItemTag).fetchSemanticsNodes().isNotEmpty()
            }
        } catch (e: Exception) {
            throw AssertionError("PAD: Nije pronađen nijedan element sa tagom koji počinje sa 'kviz_item_'.")
        }
    }

    @Test
    fun ftest04_statusniIndikatoriImajuIspravanContentDescription() {
        try {
            val odabranFilter = odaberiOpcijuDropdowna(
                anchorMatcher = hasFlexibleTestTag("filterKvizova"),
                opcijaMatcher = hasFlexibleText("Svi kvizovi"),
                opis = "Svi kvizovi"
            )
            if (!odabranFilter) {
                throw AssertionError("PAD: Nije moguće odabrati filter 'Svi kvizovi'.")
            }
        } catch (e: AssertionError) {
            throw e
        } catch (e: Exception) {
            throw AssertionError("PAD: Nije moguće odabrati filter 'Svi kvizovi' prije provjere statusa.")
        }

        osigurajPrikazListe()
        val obavezneBoje = listOf("Plava", "Zelena", "Žuta", "Crvena")

        obavezneBoje.forEach { boja ->
            try {
                val matcher = hasFlexibleContentDescription(boja) and hasFlexibleTestTag("kviz_status_icon")
                composeTestRule.onNode(hasFlexibleTestTag("listaKvizova")).performScrollToNode(matcher)
                composeTestRule.waitForIdle()
                naPrvi(matcher).assertExists()
            } catch (e: Exception) {
                throw AssertionError("PAD: Nije pronađen indikator (kviz_status_icon) sa contentDescription '$boja'.")
            }
        }
    }

    // =========================================================================
    // TESTOVI ZADATAK 2
    // =========================================================================

    @Test
    fun test05_formaZaUpisSadrziSveElemente() {
        try {
            naPrvi(najfleksibilnijiMatcher("odabirGodina", "godin")).assertExists()
            naPrvi(najfleksibilnijiMatcher("odabirPredmet", "predm")).assertExists()
            naPrvi(najfleksibilnijiMatcher("odabirGrupa", "grup")).assertExists()
            naPrvi(hasFlexibleTestTag("dodajPredmetDugme")).assertExists()
        } catch (e: AssertionError) {
            throw AssertionError("PAD: Nedostaju UI elementi forme za upis (godina, predmet, grupa).")
        }
    }

    @Test
    fun test06_dugmeZaUpisOnemogucenoNaPocetku() {
        try {
            naPrvi(hasFlexibleTestTag("dodajPredmetDugme")).assertIsNotEnabled()
        } catch (e: AssertionError) {
            throw AssertionError("PAD: Dugme 'Upiši me' je odmah omogućeno na početku.")
        }
    }

    @Test
    fun test07_odabirPodatakaOmogucavaDugme() {
        val (uspjelo, razlog) = popuniFormuZaUpis()
        if (!uspjelo) {
            throw AssertionError("PAD: Neuspješna simulacija odabira godine, predmeta i grupe. Razlog: $razlog")
        }
    }

    // =========================================================================
    // DODATNI TESTOVI (AKCIJE I PRIKAZ)
    // =========================================================================

    @Test
    fun test08_klikNaUpisiMeNeRusiAplikacijuIAzuriraListu() {
        val (uspjelo, razlog) = popuniFormuZaUpis()
        if (!uspjelo) {
            throw AssertionError("PAD: Nemoguće testirati upis jer forma ne omogućava dugme 'Upiši me'. Razlog: $razlog")
        }

        val dugme = composeTestRule.onNode(hasFlexibleTestTag("dodajPredmetDugme"))
        dugme.assertIsEnabled()
        dugme.performClick()
        composeTestRule.waitForIdle()

        osigurajPrikazListe()
        try {
            val odabranFilter = odaberiOpcijuDropdowna(
                anchorMatcher = hasFlexibleTestTag("filterKvizova"),
                opcijaMatcher = hasFlexibleText("Svi moji kvizovi"),
                opis = "Svi moji kvizovi"
            )
            if (!odabranFilter) {
                throw AssertionError("Filter 'Svi moji kvizovi' nije moguće odabrati.")
            }

            naPrvi(hasFlexibleTestTag("listaKvizova")).assertExists()
        } catch (e: Exception) {
            throw AssertionError("PAD: Aplikacija pukla nakon klika na 'Upiši me' ili filter 'Svi moji kvizovi' ne postoji.")
        }
    }

    @Test
    fun test09_kvizKarticaSadrziTekstualnePodatke() {
        try {
            val filter = naPrvi(hasFlexibleTestTag("filterKvizova"))
            filter.performClick()
            composeTestRule.waitForIdle()
            naPrvi(hasFlexibleText("Svi kvizovi") and hasAnyAncestor(isPopup())).performClick()
            composeTestRule.waitForIdle()
            try { filter.performClick() } catch (e: Exception) {}
        } catch (e: Exception) {}

        osigurajPrikazListe()

        val hasKvizItemTag = SemanticsMatcher("Tag počinje sa kviz_item_") { node ->
            node.config.getOrNull(SemanticsProperties.TestTag)?.lowercase()?.replace("_", "")?.startsWith("kvizitem") == true
        }

        try {
            val prviKvizNode = composeTestRule.onAllNodes(hasKvizItemTag).onFirst()
            prviKvizNode.assertExists()
            prviKvizNode.assert(hasAnyDescendant(hasAnyText()))
        } catch (e: Exception) {
            throw AssertionError("PAD: Element kviza ne sadrži nikakav tekst! Mora prikazivati naziv, predmet, datum.")
        }
    }
}