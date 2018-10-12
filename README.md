soknad-kontantstotte-api
========================
Backend for ny [kontantstøtte-søknad](https://github.com/navikt/soknad-kontantstotte)

## Lokalt oppsett

For å kjøre opp løsningen lokalt:

For å kjøre opp løsningen lokalt, start [TestLauncher](src/test/java/no/nav/kontantstotte/api/TestLauncher.java). Her må man sette VM Options til `-Dspring.profiles.active=dev`
eller lage en ny spring boot run-config med profile `dev`.


Lokal innlogging gjøre ved å kalle cookie-endepunktet til ```no.nav.security.oidc.test.support.jersey.TestTokenGeneratorResource``` som er http://localhost:8080/api/local/cookie

## Bygging og publisering

For å bygge imaget, kjør `sh build.sh`. Se `sh build.sh --help` for alternativer.
