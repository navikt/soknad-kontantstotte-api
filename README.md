soknad-kontantstotte-api
========================
Backend for ny [kontantstøtte-søknad](https://github.com/navikt/soknad-kontantstotte)

## Lokalt oppsett

For å kjøre opp løsningen lokalt:

For å kjøre opp løsningen lokalt, start [TestLauncher](src/test/java/no/nav/kontantstotte/api/TestLauncher.java). Her må man sette VM Options til `-Dspring.profiles.active=dev`
eller lage en ny spring boot run-config med profile `dev`.


Lokal innlogging gjøre ved å kalle cookie-endepunktet til ```no.nav.security.oidc.test.support.jersey.TestTokenGeneratorResource```

## Bygging og publisering

For å bygge imaget, kjør `sh build.sh`. Se `sh build.sh --help` for alternativer.

## Se PDF lokalt

- I [PDFService](src/main/java/no/nav/kontantstotte/service/PdfService.java) legg til `skrivTilFil(response.readEntity(byte[].class));` før funksjonen returnerer
- Kjør `cd src/main/resources/react-pdf/` i terminalen
- Kjør  `npm i` også `npx babel src --out-file dist/bundle.js --presets=@babel/preset-env,@babel/preset-react`
- Kjør opp TestLauncher som normalt.

For hver endring i koden må man kjøre babel (kommando #2 i punkt 3) og restarte launcheren for å få med alle endringene.