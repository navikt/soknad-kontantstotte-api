
## Lokalt oppsett

For å kjøre opp løsningen lokalt:

### Fra kommandolinje:
```
mvn package && java -jar target/soknad-api.jar
```

### Fra IntelliJ
Kjør [main-metoden](src/main/java/no/nav/kontantstotte/api/Launcher.java)

### Via dockerimaget

Erstatt `unversioned` med versjonen du ønsker å kjøre eller bygg unversioned-taggen ved å kjøre `docker build .`

```
docker run -p 8080:8080 repo.adeo.no:5443/soknad/soknad-kontantstotte-api:unversioned
```

Kommandoen tilgjengeliggjør serveren på localhost:8080


## Bygging og publisering

For å bygge imaget, kjør `sh build.sh`. Se `sh build.sh --help` for alternativer.