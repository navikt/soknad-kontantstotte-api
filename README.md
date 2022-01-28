soknad-kontantstotte-api
========================
Backend for ny [kontantstøtte-søknad](https://github.com/navikt/soknad-kontantstotte)

## Lokalt oppsett

For å kjøre opp løsningen lokalt:

### Med mock: 
* Edit Configurations -> VM Options: ``-Dspring.profiles.active=dev,mockgen-pdf,mockgen-pdl`` evt lage springboot run-config med profile `dev,mockgen-pdf,mockgen-tps`
* Kommer til å resultere i tvillinger uthentet i søknad (``mockgen-pdl``) og output av en TEST.pdf fil som kun er dummy-malen (``mockgen-pdf``).
* Apper som må kjøre i tillegg til api-et: _soknad-kontantstotte-proxy_

### Uten mock
* Edit Configurations -> VM Options: ``-Dspring.profiles.active=dev`` evt lage springboot run-config med profile `dev`
* TEST.pdf innholder data fylt inn fra frontenden
* Apper som må kjøre i tillegg til api-et: _soknad-kontantstotte-proxy, soknad-html-generator, pdfgen_


#### Lokal innlogging 
* Kalle cookie-endepunktet til ```no.nav.security.oidc.test.support.jersey.TestTokenGeneratorResource``` som er http://localhost:8080/api/local/cookie

## Bygging og publisering

For å bygge imaget, kjør `sh build.sh`. Se `sh build.sh --help` for alternativer.
