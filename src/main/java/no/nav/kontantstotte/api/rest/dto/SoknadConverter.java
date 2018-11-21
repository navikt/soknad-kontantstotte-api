package no.nav.kontantstotte.api.rest.dto;

import no.nav.kontantstotte.innsending.Soknad;
import no.nav.kontantstotte.innsending.steg.*;

import javax.ws.rs.BadRequestException;

public class SoknadConverter {

    public Soknad convert(SoknadDto dto) {
        return new Soknad.Builder()
                .person(new Person(dto.person.fnr))
                .kravTilSoker(new SokerKrav(
                        dto.kravTilSoker.barnIkkeHjemme,
                        dto.kravTilSoker.boddEllerJobbetINorgeSisteFemAar,
                        dto.kravTilSoker.borSammenMedBarnet,
                        dto.kravTilSoker.ikkeAvtaltDeltBosted,
                        dto.kravTilSoker.norskStatsborger,
                        dto.kravTilSoker.skalBoMedBarnetINorgeNesteTolvMaaneder))
                .familieforhold(new Familieforhold(
                        dto.familieforhold.borForeldreneSammenMedBarnet,
                        dto.familieforhold.annenForelderNavn,
                        dto.familieforhold.annenForelderFodselsnummer))
                .barnehageplass(from(dto.barnehageplass))
                .arbeidIUtlandet(new ArbeidIUtlandet(
                        dto.arbeidIUtlandet.arbeiderIUtlandetEllerKontinentalsokkel,
                        dto.arbeidIUtlandet.arbeiderIUtlandetEllerKontinentalsokkelForklaring,
                        dto.arbeidIUtlandet.arbeiderAnnenForelderIUtlandet,
                        dto.arbeidIUtlandet.arbeiderAnnenForelderIUtlandetForklaring
                ))
                .utenlandskKontantstotte(new UtenlandskKontantstotte(
                        dto.utenlandskKontantstotte.mottarKontantstotteFraUtlandet,
                        dto.utenlandskKontantstotte.mottarKontantstotteFraUtlandetTilleggsinfo
                ))
                .mineBarn(new Barn(
                        dto.mineBarn.navn,
                        dto.mineBarn.fodselsdato
                ))
                .tilknytningTilUtland(new TilknytningTilUtland(
                        dto.tilknytningTilUtland.annenForelderBoddEllerJobbetINorgeMinstFemAar,
                        dto.tilknytningTilUtland.annenForelderBoddEllerJobbetINorgeMinstFemAarForklaring,
                        dto.tilknytningTilUtland.boddEllerJobbetINorgeMinstFemAar,
                        dto.tilknytningTilUtland.boddEllerJobbetINorgeMinstFemAarForklaring
                ))
                .utenlandskeYtelser(new UtenlandskeYtelser(
                        dto.utenlandskeYtelser.mottarYtelserFraUtland,
                        dto.utenlandskeYtelser.mottarYtelserFraUtlandForklaring,
                        dto.utenlandskeYtelser.mottarAnnenForelderYtelserFraUtland,
                        dto.utenlandskeYtelser.mottarAnnenForelderYtelserFraUtlandForklaring
                ))
                .oppsummering(new Oppsummering(
                        dto.oppsummering.bekreftelse))
                .sprak(dto.sprak)
                .build();

    }

    private Barnehageplass from(SoknadDto.Barnehageplass dto) {
        Barnehageplass barnehageplass = new Barnehageplass();
        barnehageplass.harBarnehageplass = dto.harBarnehageplass;
        barnehageplass.barnBarnehageplassStatus = from(dto.barnBarnehageplassStatus);
        barnehageplass.harBarnehageplassAntallTimer = dto.harBarnehageplassAntallTimer;
        barnehageplass.harBarnehageplassDato = dto.harBarnehageplassDato;
        barnehageplass.harBarnehageplassKommune = dto.harBarnehageplassKommune;
        barnehageplass.harSluttetIBarnehageKommune = dto.harSluttetIBarnehageKommune;
        barnehageplass.harSluttetIBarnehageAntallTimer = dto.harSluttetIBarnehageAntallTimer;
        barnehageplass.harSluttetIBarnehageDato = dto.harSluttetIBarnehageDato;
        barnehageplass.skalBegynneIBarnehageKommune = dto.skalBegynneIBarnehageKommune;
        barnehageplass.skalBegynneIBarnehageAntallTimer = dto.skalBegynneIBarnehageAntallTimer;
        barnehageplass.skalBegynneIBarnehageDato = dto.skalBegynneIBarnehageDato;
        barnehageplass.skalSlutteIBarnehageKommune = dto.skalSlutteIBarnehageKommune;
        barnehageplass.skalSlutteIBarnehageAntallTimer = dto.skalSlutteIBarnehageAntallTimer;
        barnehageplass.skalSlutteIBarnehageDato = dto.skalSlutteIBarnehageDato;
        return barnehageplass;
    }

    private Barnehageplass.BarnehageplassVerdier from(SoknadDto.Barnehageplass.BarnehageplassVerdier dto) {
        if(dto == null)
            return null;

        switch (dto) {
            case garIkkeIBarnehage:
                return Barnehageplass.BarnehageplassVerdier.garIkkeIBarnehage;
            case harBarnehageplass:
                return Barnehageplass.BarnehageplassVerdier.harBarnehageplass;
            case harSluttetIBarnehage:
                return Barnehageplass.BarnehageplassVerdier.harSluttetIBarnehage;
            case skalBegynneIBarnehage:
                return Barnehageplass.BarnehageplassVerdier.skalBegynneIBarnehage;
            case skalSlutteIBarnehage:
                return Barnehageplass.BarnehageplassVerdier.skalSlutteIBarnehage;
            default:
                throw new BadRequestException("Ugyldig barnehageplassverdi");
        }

    }
}