const Arbeidsforhold = (props) => {
        return (
            <Bolk>
                <h3>{props.tekster['arbeidsforhold.tittel'].toUpperCase()}</h3>

                <OppsummeringsElement
                    sporsmal={props.tekster['arbeidsforhold.mottarYtelserFraUtlandet.sporsmal']}
                    svar={props.arbeidsforhold.mottarYtelserFraUtlandet}
                />
                {props.arbeidsforhold.mottarYtelserFraUtlandet === 'JA' &&
                    <OppsummeringsElement
                        sporsmal={props.tekster['oppsummering.arbeidsforhold.tilleggsinformasjon.label']}
                        svar={props.arbeidsforhold.mottarYtelserFraUtlandetForklaring}
                    />
                }

                <OppsummeringsElement
                    sporsmal={props.tekster['arbeidsforhold.arbeiderIUtlandetEllerKontinentalsokkel.sporsmal']}
                    svar={props.arbeidsforhold.arbeiderIUtlandetEllerKontinentalsokkel}
                />
                {props.arbeidsforhold.arbeiderIUtlandetEllerKontinentalsokkel === 'JA' &&
                <OppsummeringsElement
                    sporsmal={props.tekster['oppsummering.arbeidsforhold.tilleggsinformasjon.label']}
                    svar={props.arbeidsforhold.arbeiderIUtlandetEllerKontinentalsokkelForklaring}
                />
                }

                <OppsummeringsElement
                    sporsmal={props.tekster['arbeidsforhold.mottarYtelserFraUtlandet.sporsmal']}
                    svar={props.arbeidsforhold.mottarKontantstotteFraAnnetEOS}
                />
                {props.arbeidsforhold.mottarKontantstotteFraAnnetEOS === 'JA' &&
                <OppsummeringsElement
                    sporsmal={props.tekster['oppsummering.arbeidsforhold.tilleggsinformasjon.label']}
                    svar={props.arbeidsforhold.mottarKontantstotteFraAnnetEOSForklaring}
                />
                }
            </Bolk>
        );
};


