var Familieforhold = React.createClass({
    displayName: 'Familieforhold',

    render: function () {
        return React.createElement(
            'div',
            null,
            React.createElement(
                'h3',
                null,
                'Opplysning om familieforhold:'
            ),
            React.createElement(
                'ul',
                null,
                React.createElement(
                    'li',
                    null,
                    'Har foreldrene delt bosted: ',
                    this.props.familieforhold.borForeldreneSammenMedBarnet
                ),
                this.props.familieforhold.borForeldreneSammenMedBarnet === 'JA' && React.createElement(
                    OppsummeringsListeElement,
                    {
                        tekst: 'Jeg bor sammen med den andre forelderen'
                    },
                    React.createElement(
                        'h4',
                        null,
                        'Navnet til den andre forelderen:'
                    ),
                    React.createElement(
                        'p',
                        null,
                        this.props.familieforhold.annenForelderNavn
                    ),
                    React.createElement(
                        'h4',
                        null,
                        'Fodselsnummeret til den andre forelderen:'
                    ),
                    React.createElement(
                        'p',
                        null,
                        this.props.familieforhold.annenForelderFodselsnummer
                    )
                )
            )
        );
    }
});