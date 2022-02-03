package no.nav.kontantstotte.innsyn;

import no.nav.kontantstotte.innsyn.domain.Barn;
import no.nav.kontantstotte.innsyn.domain.InnsynService;
import no.nav.kontantstotte.innsyn.domain.Person;
import org.springframework.context.annotation.*;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Configuration
public class InnsynTestConfiguration {
    @Bean
    @Profile("mockgen-pdl")
    @Primary
    InnsynService innsynServiceGen() {
        InnsynService innsynServiceClient = mock(InnsynService.class);
        when(innsynServiceClient.hentPersonInfo(any())).thenReturn(new Person.Builder().fornavn("FORNAVN").etternavn("SLEKTSNAVN").statsborgerskap("NOR").build());
        when(innsynServiceClient.hentBarnInfo(any())).thenReturn(new ArrayList<Barn>() {{
            add(new Barn.Builder()
                    .fødselsnummer("11111111111")
                    .fulltnavn("NAVNESEN TVILLING1")
                    .fødselsdato("01.01.2018")
                    .build());
            add(new Barn.Builder()
                    .fødselsnummer("22222222222")
                    .fulltnavn("NAVNESEN TVILLING2")
                    .fødselsdato("01.01.2018")
                    .build());
        }});
        return innsynServiceClient;
    }
}
