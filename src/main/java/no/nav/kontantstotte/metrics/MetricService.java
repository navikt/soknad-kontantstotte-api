package no.nav.kontantstotte.metrics;


import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;

public class MetricService {

    private static Counter soknadApnet;
    private static Counter soknadSendtInn;
    private static Gauge tpsInnsynHealth;

    public MetricService() {
        CollectorRegistry registry = new CollectorRegistry();

        soknadApnet = Counter.build()
                .name("soknad_kontantstotte_apnet")
                .help("Antall påbegynte søknader")
                .register(registry);
        soknadSendtInn = Counter.build()
                .name("soknad_kontantstotte_innsending")
                .help("Metrikker for innsending av søknader")
                .labelNames("status")
                .register(registry);
        tpsInnsynHealth = Gauge.build()
                .name("tps_innsyn_health")
                .help("Metrikk for å se om TPS er oppe eller ikke")
                .register(registry);
    }

    public Counter getSoknadApnet() {
        return soknadApnet;
    }

    public Counter getSoknadSendtInn() { return soknadSendtInn; }

    public Gauge getTpsInnsynHealth() { return tpsInnsynHealth; }
}
