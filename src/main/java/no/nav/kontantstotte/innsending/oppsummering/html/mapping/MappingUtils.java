package no.nav.kontantstotte.innsending.oppsummering.html.mapping;

import no.nav.familie.kontrakter.ks.søknad.AktørArbeidYtelseUtland;
import no.nav.familie.kontrakter.ks.søknad.AktørTilknytningUtland;
import no.nav.familie.kontrakter.ks.søknad.Søknad;

import java.util.ArrayList;

public class MappingUtils {

    public static AktørTilknytningUtland hentUtenlandsTilknytningForSøker(Søknad søknad) {
        return new ArrayList<>(søknad.getOppgittUtlandsTilknytning().getAktørerTilknytningTilUtlandet()).stream()
                .filter(person -> person.getFødselsnummer().equals(søknad.getSøkerFødselsnummer()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Finner ikke utenlandstilknytning for søker"));
    }

    public static AktørTilknytningUtland hentUtenlandsTilknytningForAnnenPart(Søknad søknad) {
        return new ArrayList<>(søknad.getOppgittUtlandsTilknytning().getAktørerTilknytningTilUtlandet()).stream()
                .filter(person -> person.getFødselsnummer().equals(søknad.getOppgittAnnenPartFødselsnummer()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Finner ikke utenlandstilknytning for annen part"));
    }

    public static AktørArbeidYtelseUtland hentArbeidYtelseUtlandForSøker(Søknad søknad) {
        return new ArrayList<>(søknad.getOppgittUtlandsTilknytning().getAktørerArbeidYtelseIUtlandet()).stream()
                .filter(person -> person.getFødselsnummer().equals(søknad.getSøkerFødselsnummer()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Finner ikke arbeidytelseutland for søker"));
    }

    public static AktørArbeidYtelseUtland hentArbeidYtelseUtlandForAnnenPart(Søknad søknad) {
        return new ArrayList<>(søknad.getOppgittUtlandsTilknytning().getAktørerArbeidYtelseIUtlandet()).stream()
                .filter(person -> person.getFødselsnummer().equals(søknad.getOppgittAnnenPartFødselsnummer()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Finner ikke arbeidytelseutland for annen part"));
    }
}
