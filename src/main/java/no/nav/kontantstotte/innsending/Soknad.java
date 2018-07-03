package no.nav.kontantstotte.innsending;

import java.time.LocalDateTime;
import java.util.Optional;

public class Soknad {

    public LocalDateTime innsendingTimestamp;
    public SokerKrav sokerKrav;
    public Familieforhold familieforhold;
    public Barnehageplass barnehageplass;
    public Arbeidsforhold arbeidsforhold;
    public Barn barn;
    public Optional<AnnenForelder> annenForelder;

    public Soknad() {
        sokerKrav = new SokerKrav();
        familieforhold = new Familieforhold();
        barnehageplass = new Barnehageplass();
        arbeidsforhold = new Arbeidsforhold();
    }
}
