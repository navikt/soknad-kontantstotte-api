var Arbeidsforhold = React.createClass({
    render: function () {
        return (
            <div>
                <h3>Opplysninger om arbeidsforhold og andre ytelser</h3>
                <ul>
                    <OppsummeringsListeElement tekst={
                        this.props.arbeidsforhold.mottarYtelserFraUtlandet === 'JA'
                        ? 'Jeg eller barnets andre forelder mottar ytelser fra utlandet'
                        : 'Ingen av foreldrene mottar andre ytelser fra utlandet'} children={
                            this.props.arbeidsforhold.mottarYtelserFraUtlandet === 'JA' && (
                                <div>
                                    <h4>Tilleggsinformasjon:</h4>
                                    <p>{this.props.arbeidsforhold.mottarYtelserFraUtlandetForklaring}</p>
                                </div>
                            )
                        } />
                    <OppsummeringsListeElement tekst={
                        this.props.arbeidsforhold.arbeiderIUtlandetEllerKontinentalsokkel === 'JA'
                            ? 'Jeg eller barnets andre forelder jobber på utenlandsk kontinentalsokkel'
                            : 'Ingen av foreldrene jobber på utenlandsk kontinentalsokkel'} children={
                        this.props.arbeidsforhold.arbeiderIUtlandetEllerKontinentalsokkel === 'JA' && (
                            <div>
                                <h4>Tilleggsinformasjon:</h4>
                                <p>{this.props.arbeidsforhold.arbeiderIUtlandetEllerKontinentalsokkelForklaring}</p>
                            </div>
                        )
                    } />
                    <OppsummeringsListeElement tekst={
                        this.props.arbeidsforhold.mottarKontantstotteFraAnnetEOS === 'JA'
                            ? 'Jeg eller barnets andre forelder mottar kontantstøtte fra et EØS-land'
                            : 'Ingen av foreldrene mottar kontantstøtte fra et EØS-land'} children={
                        this.props.arbeidsforhold.mottarKontantstotteFraAnnetEOS === 'JA' && (
                            <div>
                                <h4>Tilleggsinformasjon:</h4>
                                <p>{this.props.arbeidsforhold.mottarKontantstotteFraAnnetEOSForklaring}</p>
                            </div>
                        )
                    } />
                </ul>
            </div>
        );
    }
});