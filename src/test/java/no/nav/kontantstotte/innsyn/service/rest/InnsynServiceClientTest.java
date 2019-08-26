package no.nav.kontantstotte.innsyn.service.rest;

import org.junit.Test;
import java.time.LocalDate;

import static no.nav.kontantstotte.innsyn.service.rest.InnsynServiceClient.erIKontantstotteAlder;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class InnsynServiceClientTest {

    @Test
    public void at_evaluaring_av_kontantstottealder_er_korrekt() {
        String forTidligFødselsdato = LocalDate.now().minusMonths(28).minusDays(1).toString();
        String forSenFødselsdato = LocalDate.now().minusMonths(10).plusDays(1).toString();
        String gyldigFødselsdato = LocalDate.now().minusMonths(18).toString();

        assertThat(erIKontantstotteAlder(forTidligFødselsdato)).isFalse();
        assertThat(erIKontantstotteAlder(forSenFødselsdato)).isFalse();
        assertThat(erIKontantstotteAlder(gyldigFødselsdato)).isTrue();
    }
}
