# Weather App

Weather App je Android aplikacija za prikaz trenutnih vremenskih uslova za određene lokacije. Korisnici mogu videti trenutnu temperaturu, dodavati gradove u omiljene i pretraživati vremenske uslove za različite lokacije.

## Funkcionalnosti

- Prikaz trenutne temperature i vremenskih uslova za izabrani grad.
- Dodavanje i uklanjanje gradova iz liste omiljenih.
- Pretraga vremenskih uslova putem OpenWeather API-ja.
- Prikaz lokacije na mapi.

## Korišćene tehnologije

- **Java** - Primarni programski jezik aplikacije.
- **Android Studio** - Okruženje za razvoj aplikacije.
- **Room Database** - Skladištenje omiljenih gradova.
- **OpenWeather API** - Dohvatanje vremenskih podataka.
- **RecyclerView** - Prikaz liste omiljenih gradova.

## Struktura projekta

### Glavne komponente

- `MainActivity.java` - Početni ekran aplikacije.
- `WeatherFragment.java` - Fragment za prikaz trenutnih vremenskih podataka.
- `FavoriteCityAdapter.java` - Adapter za prikaz liste omiljenih gradova.
- `WeatherService.java` - Klasa za komunikaciju sa OpenWeather API-jem.
- `AppDatabase.java` - Room Database konfiguracija.
- `FavoriteCityDAO.java` - Data Access Object (DAO) za omiljene gradove.

## Permisije

Aplikacija koristi sledeće permisije:

- `android.permission.INTERNET` - Pristup internetu za dobijanje vremenskih podataka.

## Instalacija i pokretanje

1. Klonirajte repozitorijum.
2. Otvorite projekt u **Android Studio**.
3. Pokrenite aplikaciju na emulatoru ili fizičkom uređaju.

## Napomena

Za ispravan rad aplikacije potreban je API ključ sa **OpenWeather** servisa, koji treba dodati u aplikaciju (detalji zavise od implementacije).

---

## Autor
Ime: [Vukašin Patković]
GitHub: [https://github.com/patkovicvukasin]
Email: [vukasinpatkovic@gmail.com]
