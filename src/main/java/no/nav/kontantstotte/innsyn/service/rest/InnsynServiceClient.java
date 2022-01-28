package no.nav.kontantstotte.innsyn.service.rest;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import no.nav.familie.kontrakter.felles.personopplysning.FORELDERBARNRELASJONROLLE;
import no.nav.familie.kontrakter.felles.personopplysning.ForelderBarnRelasjon;
import no.nav.kontantstotte.innsyn.domain.Barn;
import no.nav.kontantstotte.innsyn.domain.InnsynService;
import no.nav.kontantstotte.innsyn.domain.Person;
import no.nav.kontantstotte.innsyn.pdl.PDLClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

import static no.nav.kontantstotte.innsyn.service.rest.InnsynConverter.pdlHentPersonBolkToBarn;
import static no.nav.kontantstotte.innsyn.service.rest.InnsynConverter.pdlPersonDataToPerson;

@Profile("!mockgen-tps")
@Component
class InnsynServiceClient implements InnsynService {

    private static final Logger secureLogger = LoggerFactory.getLogger("secureLogger");
    private static final Logger logger = LoggerFactory.getLogger(InnsynServiceClient.class);
    private static final String CONSUMER_ID = "soknad-kontantstotte-api";

    private static final Integer MIN_ALDER_I_MANEDER = 10;

    private static final Integer MAKS_ALDER_I_MANEDER = 28;
    private final Counter sokerErIkkeKvalifisert = Metrics.counter("soknad.kontantstotte.kvalifisert", "status", "NEI");
    private final Counter sokerErKvalifisert = Metrics.counter("soknad.kontantstotte.kvalifisert", "status", "JA");
    private final PDLClient pdlClient;

    @Autowired
    InnsynServiceClient(PDLClient pdlClient) {
        this.pdlClient = pdlClient;
    }

    static boolean erIKontantstotteAlder(String fødselsdato) {
        Period diff = Period.between(LocalDate.parse(fødselsdato), LocalDate.now());
        Integer alderIManeder = diff.getYears() * 12 + diff.getMonths();
        return (alderIManeder >= MIN_ALDER_I_MANEDER) &&
               (alderIManeder <= MAKS_ALDER_I_MANEDER) &&
               !(alderIManeder.equals(MAKS_ALDER_I_MANEDER) && diff.getDays() > 0);
    }

    @Override
    public Person hentPersonInfo(String fnr) {
        return pdlPersonDataToPerson.apply(pdlClient.hentPersoninfo(fnr));
    }

    @Override
    public List<Barn> hentBarnInfo(String fnr) {
        List<String> barnIdenter = pdlClient.hentPersoninfoMedRelasjoner(fnr).stream()
                                            .filter(relasjon -> relasjon.getRelatertPersonsRolle() ==
                                                                FORELDERBARNRELASJONROLLE.BARN)
                                            .map(ForelderBarnRelasjon::getRelatertPersonsIdent)
                                            .collect(Collectors.toList());
        List<Barn> barna = pdlClient.hentPersonerMedBolk(barnIdenter)
                                    .stream()
                                    .map(pdlHentPersonBolk -> pdlHentPersonBolkToBarn.apply(pdlHentPersonBolk))
                                    .collect(Collectors.toList());
        if (barna.isEmpty()) {
            sokerErIkkeKvalifisert.increment();
        } else {
            sokerErKvalifisert.increment();
        }

        return barna;
    }

    @Override
    public void ping() {
        pdlClient.ping();
    }
}
