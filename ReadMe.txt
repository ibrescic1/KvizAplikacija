Spirala 4 - Poboljšanja aplikacije


KORISNIČKA PERSPEKTIVA

1. Ekran detalja kviza prije rješavanja (KvizDetaljiScreen)
    Klik na "Detalji" otvara ekran s informacijama (predmet, datum, trajanje, broj pitanja, status) prije nego počne rješavanje.
    Korisnik može vidjeti detalje i svjesno odlučiti da li želi početi.
    Kvizovi koji nisu aktivni imaju takodjer dugme s detaljima.

2. Poruka kada nema kvizova (KvizoviScreen)
    Kada je lista kvizova prazna, prikazuje se jasna poruka da nema odgovarajucih kvizova.
    Prije je korisnik vidio samo prazan ekran i nije znao zašto nema kvizova, a sada ima tekst "Nema kvizova za odabrani filter. Pokušajte se upisati na predmet ili promijeniti filter".

3. Prikaz tačnog odgovora nakon odgovaranja (KvizTakeScreen, KvizTakeViewModel)
    Nakon odabira odgovora prikazuje se tekst "Tačno!" ili "Netačno", u zavisnosti od odgovora.
    Korisnik odmah dobija povratnu informaciju i može učiti iz grešaka tokom samog kviza.

4. Dark mode podrška (Theme.kt)
    Aplikacija automatski prati sistemsku postavku tamne/svijetle teme. Definisane su lightColorScheme i darkColorScheme u Theme.kt.
    Aplikacija koristi isSystemInDarkTheme() da automatski odabere paletu prema sistemskoj postavci.




RAZVOJNA PERSPEKTIVA

1. Konstante na jednom mjestu (Konstante.kt)
    Sve hardkodirane vrijednosti (referentni datum, default hash, base URL) premještene u Konstante.kt.
    LocalDateTime.of(2026, 4, 10, 12, 0) se pojavljivao na više mjesta. Sada promjena jedne vrijednosti mijenja je svugdje. Olakšano je održavanje koda.

2. Business logika odvojena od ViewModela (KvizFilterHelper)
    Logika filtriranja, parsiranja datuma i provjeru vremenskih opsega kvizova
          premještena u KvizFilterHelper u domain paketu.
    KvizViewModel prije imao je  parseDate, kvizJeBuduci, kvizJeProsli,
            kvizjeAktivan, sve u jednom fajlu.
    ViewModel je sada čitljiviji, logika se može testirati nezavisno.

3. Komentari na svim ključnim klasama
    Dodani komentari koji objašnjavaju svrhu svake klase i metode. Prije nije bilo komentara koji bi olakšali razumijevanje.
    Novi developer sada može bolje razumjeti kod bez pitanja.

