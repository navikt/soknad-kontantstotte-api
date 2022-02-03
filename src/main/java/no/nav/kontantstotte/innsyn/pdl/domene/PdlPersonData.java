package no.nav.kontantstotte.innsyn.pdl.domene;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.familie.kontrakter.felles.personopplysning.ForelderBarnRelasjon;
import no.nav.familie.kontrakter.felles.personopplysning.Statsborgerskap;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PdlPersonData {

    @JsonProperty("foedsel")
    private List<PdlFødselsdato> fødsel;
    private List<PdlNavn> navn;
    @JsonProperty("kjoenn")
    private List<PdlKjønn> kjønn;
    private List<PdlStatsborgerskap> statsborgerskap;
    private List<PdlForelderBarnRelasjon> forelderBarnRelasjon;

    PdlPersonData() {
        // for Jackson mapping
    }

    public PdlPersonData(List<PdlFødselsdato> fødsel,
                         List<PdlNavn> navn,
                         List<PdlKjønn> kjønn,
                         List<PdlStatsborgerskap> statsborgerskap,
                         List<PdlForelderBarnRelasjon> forelderBarnRelasjon
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

    public List<PdlStatsborgerskap> getStatsborgerskap() {
        return statsborgerskap;
    }

    public List<PdlForelderBarnRelasjon> getForelderBarnRelasjon() {
        return forelderBarnRelasjon;
    }
}
