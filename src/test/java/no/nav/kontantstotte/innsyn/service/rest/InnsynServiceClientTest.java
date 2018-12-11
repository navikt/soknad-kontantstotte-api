package no.nav.kontantstotte.innsyn.service.rest;

import org.junit.Test;
import java.time.LocalDate;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class InnsynServiceClientTest {

    @Test
    public void at_evaluaring_av_kontantstottealder_er_korrekt() {
        String forTidligFodselsdato = LocalDate.now().minusMonths(28).minusDays(1).toString();
        String forSenFodselsdato = LocalDate.now().minusMonths(10).plusDays(1).toString();
        String gyldigFodselsdato = LocalDate.now().minusMonths(10).toString();

        assertThat(InnsynServiceClient.erIKontantstotteAlder(forTidligFodselsdato)).isFalse();
        assertThat(InnsynServiceClient.erIKontantstotteAlder(forSenFodselsdato)).isFalse();
        assertThat(InnsynServiceClient.erIKontantstotteAlder(gyldigFodselsdato)).isTrue();
    }
}
