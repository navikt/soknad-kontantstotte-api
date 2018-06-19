soknad-kontantstotte-api
========================
Backend for ny [kontantstøtte-søknad](https://github.com/navikt/soknad-kontantstotte)

## Lokalt oppsett

For å kjøre opp løsningen lokalt:

### Fra kommandolinje:
```
mvn package && java -jar target/soknad-kontantstotte-api.jar -Dspring.profiles.active=dev
```

### Fra IntelliJ
Kjør [main-metoden](src/test/java/no/nav/kontantstotte/api/TestLauncher.java). Her må man sette VM Options til `-Dspring.profiles.active=dev`
eller lage en ny spring boot run-config med profile `dev`.

### Via dockerimaget

Erstatt `unversioned` med versjonen du ønsker å kjøre eller bygg unversioned-taggen ved å kjøre `docker build .`

```
docker login repo.adeo.no:5443
docker run -p 8080:8080 repo.adeo.no:5443/soknad/soknad-kontantstotte-api:unversioned
```

Kommandoen tilgjengeliggjør serveren på localhost:8080


## Bygging og publisering

For å bygge imaget, kjør `sh build.sh`. Se `sh build.sh --help` for alternativer.