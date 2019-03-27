package no.nav.kontantstotte.innsending.oppsummering.html.mapping;

import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.VedleggMetadata;
import no.nav.kontantstotte.innsending.oppsummering.html.Bolk;
import no.nav.kontantstotte.innsending.oppsummering.html.Element;
import no.nav.kontantstotte.innsending.steg.Barnehageplass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import static no.nav.kontantstotte.innsending.oppsummering.html.mapping.Tekstnokkel.*;

public class BarnehageplassMapping extends BolkMapping {

    private static final Logger logger = LoggerFactory.getLogger(BarnehageplassMapping.class);

    public BarnehageplassMapping(Tekster tekster) {
        super(tekster);
    }

    @Override
    public Bolk map(Soknad soknad) {
        Bolk barnehageplassBolk = new Bolk();

        boolean erFlerling = "JA".equalsIgnoreCase(soknad.mineBarn.erFlerling);
        this.setBrukFlertall(erFlerling);

        Barnehageplass barnehageplass = soknad.barnehageplass;
        barnehageplassBolk.tittel = tekster.hentTekst(BARNEHAGEPLASS_TITTEL.getNokkel());
        barnehageplassBolk.elementer = new ArrayList<>();

        if("NEI".equalsIgnoreCase(barnehageplass.harBarnehageplass)){
            barnehageplassBolk.elementer.add(nyttElementMedTekstsvar.apply(HAR_BARNEHAGEPLASS, SVAR_NEI));
        } else {
            barnehageplassBolk.elementer.add(nyttElementMedTekstsvar.apply(HAR_BARNEHAGEPLASS, SVAR_JA));
        }

        if (barnehageplass.barnBarnehageplassStatus != null) {
            barnehageplassBolk.elementer.add(nyttElementMedTekstsvar.apply(BARN_BARNEHAGEPLASS_STATUS, barnehageplass.barnBarnehageplassStatus.getTekstNokkel()));

            switch (barnehageplass.barnBarnehageplassStatus) {
                case harSluttetIBarnehage:
                    barnehageplassBolk.elementer.addAll(
                        Arrays.asList(
                            nyttElementMedVerdisvar.apply(HAR_SLUTTET_I_BARNEHAGE_DATO, barnehageplass.harSluttetIBarnehageDato),
                            nyttElementMedVerdisvar.apply(HAR_SLUTTET_I_BARNEHAGE_ANTALL_TIMER, barnehageplass.harSluttetIBarnehageAntallTimer),
                            nyttElementMedVerdisvar.apply(HAR_SLUTTET_I_BARNEHAGE_KOMMUNE, barnehageplass.harSluttetIBarnehageKommune),
                            nyttElementMedListe.apply(HAR_SLUTTET_I_BARNEHAGE_VEDLEGG, barnehageplass.harSluttetIBarnehageVedlegg.stream().map(VedleggMetadata::getFilnavn).collect(Collectors.toList()))
                        )
                    );
                    break;
                case skalSlutteIBarnehage:
                    barnehageplassBolk.elementer.addAll(
                        Arrays.asList(
                            nyttElementMedVerdisvar.apply(SKAL_SLUTTE_I_BARNEHAGE_DATO, barnehageplass.skalSlutteIBarnehageDato),
                            nyttElementMedVerdisvar.apply(SKAL_SLUTTE_I_BARNEHAGE_ANTALL_TIMER, barnehageplass.skalSlutteIBarnehageAntallTimer),
                            nyttElementMedVerdisvar.apply(SKAL_SLUTTE_I_BARNEHAGE_KOMMUNE, barnehageplass.skalSlutteIBarnehageKommune),
                            nyttElementMedListe.apply(SKAL_SLUTTE_I_BARNEHAGE_VEDLEGG, barnehageplass.skalSlutteIBarnehageVedlegg.stream().map(VedleggMetadata::getFilnavn).collect(Collectors.toList()))
                        )
                    );
                    break;
                case harBarnehageplass:
                    Float antallTimer;
                    try {
                        antallTimer = Float.parseFloat(barnehageplass.harBarnehageplassAntallTimer);
                    } catch (NumberFormatException e) {
                        antallTimer = 0.0f;
                        logger.warn("Klarte ikke å konvertere harBarnehageplassAntalTimer med verdi %s til et tall", barnehageplass.harBarnehageplassAntallTimer);
                    }
                    Element harBarnehageplassAntallTimer = antallTimer > 33.0 ?
                            Element.nyttSvar(
                                    tekster.hentTekst(HAR_BARNEHAGEPLASS_ANTALL_TIMER.getNokkel(), erFlerling),
                                    barnehageplass.harBarnehageplassAntallTimer,
                                    tekster.hentTekst(BARNEHAGEPLASS_HOYT_TIMEANTALL_ADVARSEL.getNokkel(), erFlerling)
                            ) :
                            nyttElementMedVerdisvar.apply(HAR_BARNEHAGEPLASS_ANTALL_TIMER, barnehageplass.harBarnehageplassAntallTimer);

                    barnehageplassBolk.elementer.addAll(
                        Arrays.asList(
                            nyttElementMedVerdisvar.apply(HAR_BARNEHAGEPLASS_DATO, barnehageplass.harBarnehageplassDato),
                            harBarnehageplassAntallTimer,
                            nyttElementMedVerdisvar.apply(HAR_BARNEHAGEPLASS_KOMMUNE, barnehageplass.harBarnehageplassKommune)
                        )
                    );

                    break;
                case skalBegynneIBarnehage:
                    barnehageplassBolk.elementer.addAll(
                            Arrays.asList(
                                    nyttElementMedVerdisvar.apply(SKAL_BEGYNNE_I_BARNEHAGE_DATO, barnehageplass.skalBegynneIBarnehageDato),
                                    nyttElementMedVerdisvar.apply(SKAL_BEGYNNE_I_BARNEHAGE_ANTALL_TIMER, barnehageplass.skalBegynneIBarnehageAntallTimer),
                                    nyttElementMedVerdisvar.apply(SKAL_BEGYNNE_I_BARNEHAGE_KOMMUNE, barnehageplass.skalBegynneIBarnehageKommune)
                            )
                    );
                    break;
            }
        }

        return barnehageplassBolk;
    }
}
