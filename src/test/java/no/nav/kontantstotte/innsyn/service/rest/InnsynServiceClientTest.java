package no.nav.kontantstotte.innsyn.service.rest;

import org.junit.Test;
import java.time.LocalDate;

import static no.nav.kontantstotte.innsyn.service.rest.InnsynServiceClient.erIKontantstotteAlder;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class InnsynServiceClientTest {

    @Test
    public void at_evaluaring_av_kontantstottealder_er_korrekt() {
        String forTidligFodselsdato = LocalDate.now().minusMonths(28).minusDays(1).toString();
        String forSenFodselsdato = LocalDate.now().minusMonths(10).plusDays(1).toString();
        String gyldigFodselsdato = LocalDate.now().minusMonths(18).toString();

        assertThat(erIKontantstotteAlder(forTidligFodselsdato)).isFalse();
        assertThat(erIKontantstotteAlder(forSenFodselsdato)).isFalse();
        assertThat(erIKontantstotteAlder(gyldigFodselsdato)).isTrue();
    }
}
