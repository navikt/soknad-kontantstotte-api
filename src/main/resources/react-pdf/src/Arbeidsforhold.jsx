const Arbeidsforhold = (props) => {
        return (
            <div>
                <h3>Opplysninger om arbeidsforhold og andre ytelser</h3>
                <ul>
                    <OppsummeringsListeElement tekst={
                        props.arbeidsforhold.mottarYtelserFraUtlandet === 'JA'
                        ? 'Jeg eller barnets andre forelder mottar ytelser fra utlandet'
                        : 'Ingen av foreldrene mottar andre ytelser fra utlandet'} children={
                            props.arbeidsforhold.mottarYtelserFraUtlandet === 'JA' && (
                                <div>
                                    <h4>Tilleggsinformasjon:</h4>
                                    <p>{props.arbeidsforhold.mottarYtelserFraUtlandetForklaring}</p>
                                </div>
                            )
                        } />
                    <OppsummeringsListeElement tekst={
                        props.arbeidsforhold.arbeiderIUtlandetEllerKontinentalsokkel === 'JA'
                            ? 'Jeg eller barnets andre forelder jobber på utenlandsk kontinentalsokkel'
                            : 'Ingen av foreldrene jobber på utenlandsk kontinentalsokkel'} children={
                        props.arbeidsforhold.arbeiderIUtlandetEllerKontinentalsokkel === 'JA' && (
                            <div>
                                <h4>Tilleggsinformasjon:</h4>
                                <p>{props.arbeidsforhold.arbeiderIUtlandetEllerKontinentalsokkelForklaring}</p>
                            </div>
                        )
                    } />
                    <OppsummeringsListeElement tekst={
                        props.arbeidsforhold.mottarKontantstotteFraAnnetEOS === 'JA'
                            ? 'Jeg eller barnets andre forelder mottar kontantstøtte fra et EØS-land'
                            : 'Ingen av foreldrene mottar kontantstøtte fra et EØS-land'} children={
                        props.arbeidsforhold.mottarKontantstotteFraAnnetEOS === 'JA' && (
                            <div>
                                <h4>Tilleggsinformasjon:</h4>
                                <p>{props.arbeidsforhold.mottarKontantstotteFraAnnetEOSForklaring}</p>
                            </div>
                        )
                    } />
                </ul>
            </div>
        );
};


