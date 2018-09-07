const Arbeidsforhold = (props) => {
        return (
            <div>
                <h3>Opplysninger om arbeidsforhold og andre ytelser</h3>

                <OppsummeringsElement
                    sporsmal={'Mottar du eller den andre av barnets forelder ytelser fra utlandet?'}
                    svar={props.arbeidsforhold.mottarYtelserFraUtlandet}
                />
                {props.arbeidsforhold.mottarYtelserFraUtlandet === 'JA' &&
                    <OppsummeringsElement
                        sporsmal={'Tilleggsinformasjon:'}
                        svar={props.arbeidsforhold.mottarYtelserFraUtlandetForklaring}
                    />
                }

                <OppsummeringsElement
                    sporsmal={'Arbeider du eller den andre av barnets forelder i utlandet, på utenlandsk skip eller på utenlandsk kontinentalsokkel?'}
                    svar={props.arbeidsforhold.arbeiderIUtlandetEllerKontinentalsokkel}
                />
                {props.arbeidsforhold.arbeiderIUtlandetEllerKontinentalsokkel === 'JA' &&
                <OppsummeringsElement
                    sporsmal={'Tilleggsinformasjon:'}
                    svar={props.arbeidsforhold.arbeiderIUtlandetEllerKontinentalsokkelForklaring}
                />
                }

                <OppsummeringsElement
                    sporsmal={'Mottar du eller den andre av barnets forelder ytelser fra utlandet?'}
                    svar={props.arbeidsforhold.mottarKontantstotteFraAnnetEOS}
                />
                {props.arbeidsforhold.mottarKontantstotteFraAnnetEOS === 'JA' &&
                <OppsummeringsElement
                    sporsmal={'Tilleggsinformasjon:'}
                    svar={props.arbeidsforhold.mottarKontantstotteFraAnnetEOSForklaring}
                />
                }
            </div>
        );
};


