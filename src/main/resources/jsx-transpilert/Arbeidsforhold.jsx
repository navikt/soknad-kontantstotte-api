var Arbeidsforhold = React.createClass({
    displayName: 'Arbeidsforhold',

    render: function () {
        return React.createElement(
            'div',
            null,
            React.createElement(
                'h3',
                null,
                'Opplysninger om arbeidsforhold og andre ytelser'
            ),
            React.createElement(
                'ul',
                null,
                React.createElement(OppsummeringsListeElement, { tekst: this.props.arbeidsforhold.mottarYtelserFraUtlandet === 'JA' ? 'Jeg eller barnets andre forelder mottar ytelser fra utlandet' : 'Ingen av foreldrene mottar andre ytelser fra utlandet', children: this.props.arbeidsforhold.mottarYtelserFraUtlandet === 'JA' && React.createElement(
                        'div',
                        null,
                        React.createElement(
                            'h4',
                            null,
                            'Tilleggsinformasjon:'
                        ),
                        React.createElement(
                            'p',
                            null,
                            this.props.arbeidsforhold.mottarYtelserFraUtlandetForklaring
                        )
                    ) }),
                React.createElement(OppsummeringsListeElement, { tekst: this.props.arbeidsforhold.arbeiderIUtlandetEllerKontinentalsokkel === 'JA' ? 'Jeg eller barnets andre forelder jobber på utenlandsk kontinentalsokkel' : 'Ingen av foreldrene jobber på utenlandsk kontinentalsokkel', children: this.props.arbeidsforhold.arbeiderIUtlandetEllerKontinentalsokkel === 'JA' && React.createElement(
                        'div',
                        null,
                        React.createElement(
                            'h4',
                            null,
                            'Tilleggsinformasjon:'
                        ),
                        React.createElement(
                            'p',
                            null,
                            this.props.arbeidsforhold.arbeiderIUtlandetEllerKontinentalsokkelForklaring
                        )
                    ) }),
                React.createElement(OppsummeringsListeElement, { tekst: this.props.arbeidsforhold.mottarKontantstotteFraAnnetEOS === 'JA' ? 'Jeg eller barnets andre forelder mottar kontantstøtte fra et EØS-land' : 'Ingen av foreldrene mottar kontantstøtte fra et EØS-land', children: this.props.arbeidsforhold.mottarKontantstotteFraAnnetEOS === 'JA' && React.createElement(
                        'div',
                        null,
                        React.createElement(
                            'h4',
                            null,
                            'Tilleggsinformasjon:'
                        ),
                        React.createElement(
                            'p',
                            null,
                            this.props.arbeidsforhold.mottarKontantstotteFraAnnetEOSForklaring
                        )
                    ) })
            )
        );
    }
});