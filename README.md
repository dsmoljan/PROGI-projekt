# Upute za lokalno pokretanje

Aplikacija je podijeljena na backend i frontend. Trenutno su oba dijela pripremljena za kontinuirani deploy na Heroku,
tako da se lokalnim pokretanjem frontenda zahtjevi se šalju na deployani backend, osim ako se ekplicitno ne preusmjere na lokalni backend.

## Pokretanje frontenda

Za pokretanje iz naredbenog retka potrebno se pozicionirati u direktorij IzvorniKod\frontend\app\src i u njemu izvesti sljedeće naredbe.

### `npm install`

Naredba instalira sve potrebne node_modules potrebne za pokretanje aplikacije.

### `npm start`

Pokreće aplikaciju na [http://localhost:3000](http://localhost:3000).

Stranica se ponovno učitava svakom spremljenom promjenom u kodu.\
You will also see any lint errors in the console.

### Slanje zahtjeva na lokalni backend

U slučaju slanja zahtjeva na lokalni backend, u kodu se moraju zamijeniti svi dijelovi u kojem se zahtjevi šalju na adresu deployanog backenda na [http://localhost:8080](http://localhost:8080).

## Pokretanje backenda

Za pokretanje iz naredbenog retka potrebno se pozicionirati u direktorij IzvorniKod\backend\src\main\java\hr\fer\progi\dogGO i u njemu izvesti sljedeću naredbu.

### `mvn spring-boot:run`

Pokreće aplikaciju na [http://localhost:8080](http://localhost:8080).
