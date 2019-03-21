# En liten intro til prometheus-metrikker

## Ulike typer metrikker
Prometheus støtter fire forskjellige metrikk-typer: Counter, Gauge, Summary og Histogram. Avsnittene under har en kort forklaring,
samt kodesnutter laget med Prometheus-klienten vi bruker i kontantstøtte, *Micrometer*.

En counter er en monotont økende teller som kun blir resatt når applikasjonen redeployes. Vi har flere countere i
kontantstøtte, de kan feks brukes til å telle antall feilede kall. En counter kan bare økes med 1 hver gang den blir
trigget i koden. 
```java
//Opprette counter med labels
private final Counter tpsInnysynSuccess = Metrics.counter("tps.innsyn.health", "response", "success");
private final Counter tpsInnsynFailure = Metrics.counter("tps.innsyn.health", "response", "failure");

//Øke verdi av counter
tpsInnysynSuccess.increment();
```

En gauge er ganske lik en counter, men kan også minke. Denne kan for eksempel brukes for å måle hvor mange poder som
kjører. Foreløpig bruker vi ikke gauges i kontantstøtte-metrikkene.

Et summary kan brukes til å måle observasjoner, ikke bare telle (som counter og gauge). Her kan man feks registrere
meldingsstørrelser eller responstider.
```java
//Opprette summary
private final DistributionSummary dokmotMeldingStorrelse = Metrics.summary("dokmot.melding.storrelse");

//Registrere ny verdi
dokmotMeldingStorrelse.record(soknadXML.length());
```

Et histogram er ganske likt som et summary, men her kan man også lage buckets for målingene. Vi har foreløpig ikke tatt i bruk
histogram i kontantstotte. 

## Bruk av labels
I prometheus trenger man ikke alltid lage en helt ny metrikk for å måle et nytt scenarie. Man kan ha én metrikk med flere
labels for å telle feks antall suksess og feil for et gitt kall, eller antall norske og utenlandske søkere. Man kan
registrere en måling for en enkelt label av gangen. 

I tillegg til custom labels du kan sette på metrikkene dine i koden, kommer NAIS med en rekke default-labels for hver metrikk.
Dette inkluderer miljø, podenavn mm. 

### Best practices
I prometheus vil hvert enkelt key-value-par for labels representere en vektor/serie med tidspunkter. Dette skalerer ganske dårlig,
og dersom man kan få mange ulike verdier for en label, vil det øke hvor mye data som lagres og sannsynligvis smelle ganske fort.
Et eksempel på dette er bruker-id/fødselsnummer. Skal man lagre det som en label i prometheus, vil du få en time series med 
tilhørende indeks, det er en rimelig dårlig idé. 

Dersom du ønsker å måle eller overvåke noe, er det bra å tenke igjennom om det faktisk bør være en metrikk, eller kanskje det
heller bør være feks et logginnslag. Metrikkene er ikke ment å gi et fullstendig bilde over alle mulige situasjoner/scenarier for
 appene eller brukerne våre.

## Endepunkt
Prometheus-metrikkene blir eksponert på `localhost:8080/internal/prometheus`. Hvis du har lagd en ny metrikk, bør du finne
den igjen her. Her kan man også se formatet på metrikk-navnene - punktum i koden blir formatert til underscores, og de ulike
metrikktypene har ulike suffikser (`_total`, `_sum` etc). Navnene på endepunktet er identisk som det apper eksponerer til 
grafana, så man bruker de samme navnene i quieriene der. Dette endepunktet kan også nås i miljø. 

## Kombinere prometheus med grafana
Med influx/sensu-metrikker, kan man ofte bare gjøre en count i grafana og få fornuftige tall ut fra det. Prometheus-metrikker er 
grunnleggende "dummere" enn influx/sensu-metrikker. Dersom man prøver å summere opp en counter, vil man fort se at tallet bare vokser
og vokser, før det plutselig blir 0 igjen når appen restartes. Prometheus klarer f.eks. ikke automatisk å ta hensyn til tidsintervallet
på grafana. 

Når man skal skrive queries for å få ut fornuftige tall på prometheus-metrikker i grafana, må man ofte tenke seg litt om. 
La oss ta telling av antall feil til en tjeneste som eksempel. Da har du en counter, og hver gang du fanger en exception
mot denne tjenesten i koden din, øker du counteren med én. I grafana vil du vise antall feil den siste timen. Som nevnt over
er prometheus grunnleggende "dum", men det finnes mange funksjoner man kan bruke på prometheus-metrikker for å visualisere
metrikkene man ønsker. I dette tilfellet kan vi ta den nåværende totalverdien av counteren og trekke fra verdien av counteren
for én time siden. Da vil man få ut antall feil som ble registrert den siste timen. Dette skriver man slik i grafana:
`sum(increase(<navn_paa_prometheus_metrikk>_total{<labels>}[1h]))`. Videre kan man gå inn i Options-taben på grafana og 
velge ulike stat-verdier for metrikken. Her kan man typisk velge mellom max, average, total eller current. Som oftest ønsker
du å velge current for å få ut den siste gjeldende summen, mens velger du max, vil du få den høyeste registrerte verdien den siste
timen. Velger du total, vil grafana typisk summere opp alle sum-verdiene, og det ønsker du ikke (det blir "dobbelt opp"). 

Det finnes mange tilsvarende funksjoner man kan bruke på ulike typer prometheus-metrikker, de er dokumentert her:
https://prometheus.io/docs/prometheus/latest/querying/functions/

I grafana er det veldig nyttig å bruke Query Inspectoren for å se verdiene du får fra prometheus når du bruker disse funksjonene
på metrikkene dine. 

## Om push vs pull og ekstrapolering i query-funksjoner
I et push-basert metrikksystem som feks Influx/Sensu, vil en metrikk *pushes* til serveren umiddelbart etter den inntreffer (økes/registreres etc).
Prometheus-metrikker blir derimot *pullet* med jevne mellomrom av serveren. Det betyr at dersom metrikken oppdateres på klientsiden,
vil det kunne gå litt tid før den blir registrert i monitorerings-serveren. 

Sjansen for at tidsintervallet man setter i grafana treffer perfekt med tidsintervallene metrikkene scrapes/pulles på, er ganske liten.
Derfor må man ofte ekstrapolere dataene for å få de til å passe med det definerte grafana-intervallet. Man skulle tro at når man 
regner increase for en counter, vil man bare få heltall ut. Det stemmer ikke, og grunnen til det er denne ekstrapoleringen. 

Ta for eksempel denne dataen:

| Tid | Verdi |
|-----|-------|
| t=1 |  10   |
| t=6 |  12   |
| t=11|  13   |


Hvis man på grafana gjør en `increase` for 15s, får man en increase på 3 over 10s, og når man ekstrapolerer det til 15s,
får man en increase på 4.5. 

Poenget er at man må merke seg at disse metrikkene er *estimater* og ikke nødvendigvis eksakte verdier for alle tidsintervall.

