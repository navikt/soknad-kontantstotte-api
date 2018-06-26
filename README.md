soknad-kontantstotte-api
========================
Backend for ny [kontantstøtte-søknad](https://github.com/navikt/soknad-kontantstotte)

## Lokalt oppsett

For å kjøre opp løsningen lokalt:

Kjør [main-metoden](src/test/java/no/nav/kontantstotte/api/TestLauncher.java)

Lokal innlogging gjøre ved å kalle cookie-endepunktet til ```no.nav.security.oidc.test.support.jersey.TestTokenGeneratorResource```

## Bygging og publisering

For å bygge imaget, kjør `sh build.sh`. Se `sh build.sh --help` for alternativer.