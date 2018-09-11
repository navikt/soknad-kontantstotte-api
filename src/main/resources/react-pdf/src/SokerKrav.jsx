const SokerKrav = (props) => {
        return (
            <div>
                <h3>Kravene av elektronisk søknad</h3>
                <ul>
                    <OppsummeringsListeElement
                        tekst={'Jeg er statsborger i Norge eller i et annet EØS-land'}
                    />
                    <OppsummeringsListeElement
                        tekst={'Jeg har bodd eller vært yrkesaktiv i Norge eller et annet EØS-land i tilsammen minst fem år'}
                    />
                    <OppsummeringsListeElement
                        tekst={'Barnet er ikke adoptert, asylsøker i fosterhjem eller på institusjon'}
                    />
                    <OppsummeringsListeElement
                        tekst={'Jeg bor i Norge sammen med barnet'}
                    />
                    <OppsummeringsListeElement
                        tekst={'Jeg og den andre forelderen har ikke avtalt delt bosted'}
                    />
                    <OppsummeringsListeElement
                        tekst={'Jeg og barnet skal ikke oppholde dere i utlandet mer enn 3 måneder de neste 12 månedene'}
                    />
                </ul>
            </div>
        );
};