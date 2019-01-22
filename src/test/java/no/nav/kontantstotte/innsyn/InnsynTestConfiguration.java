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
    @Profile("mockgen-tps")
    @Primary
    InnsynService innsynServiceGen() {
        InnsynService innsynServiceClient = mock(InnsynService.class);
        when(innsynServiceClient.hentPersonInfo(any())).thenReturn(new Person.Builder().fornavn("FORNAVN").slektsnavn("SLEKTSNAVN").build());
        when(innsynServiceClient.hentBarnInfo(any())).thenReturn(new ArrayList<Barn>() {{
            add(new Barn.Builder()
                    .fulltnavn("NAVNESEN TVILLING1")
                    .fodselsdato("01.01.2018")
                    .build());
            add(new Barn.Builder()
                    .fulltnavn("NAVNESEN TVILLING2")
                    .fodselsdato("01.01.2018")
                    .build());
        }});
        return innsynServiceClient;
    }
}
