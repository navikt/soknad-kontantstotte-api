package no.nav.kontantstotte.innsyn.pdl.domene;

import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.familie.kontrakter.felles.personopplysning.ForelderBarnRelasjon;
import no.nav.familie.kontrakter.felles.personopplysning.Statsborgerskap;

import java.util.List;

public class PdlPersonData {

    @JsonProperty("foedsel")
    private List<PdlFødselsdato> fødsel;
    private List<PdlNavn> navn;
    @JsonProperty("kjoenn")
    private List<PdlKjønn> kjønn;
    private List<Statsborgerskap> statsborgerskap;
    private List<ForelderBarnRelasjon> forelderBarnRelasjon;

    PdlPersonData() {
        // for Jackson mapping
    }

    public PdlPersonData(List<PdlFødselsdato> fødsel,
                         List<PdlNavn> navn,
                         List<PdlKjønn> kjønn,
                         List<Statsborgerskap> statsborgerskap,
                         List<ForelderBarnRelasjon> forelderBarnRelasjon
    ) {
        this.fødsel = fødsel;
        this.navn = navn;
        this.kjønn = kjønn;
        this.statsborgerskap = statsborgerskap;
        this.forelderBarnRelasjon = forelderBarnRelasjon;
    }

    public List<PdlFødselsdato> getFødsel() {
        return fødsel;
    }

    public List<PdlNavn> getNavn() {
        return navn;
    }

    public List<PdlKjønn> getKjønn() {
        return kjønn;
    }

    public List<Statsborgerskap> getStatsborgerskap() {
        return statsborgerskap;
    }

    public List<ForelderBarnRelasjon> getForelderBarnRelasjon() {
        return forelderBarnRelasjon;
    }
}
