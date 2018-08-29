var Barnehageplass = React.createClass({
    displayName: 'Barnehageplass',

    render: function () {
        var BarnehageplassVerdier = {
            Nei: 'Nei',
            NeiHarFaatt: 'NeiHarFaatt',
            Ja: 'Ja',
            JaSkalSlutte: 'JaSkalSlutte',
            Ubesvart: 'Ubesvart'
        };

        var header = React.createElement(
            'h3',
            null,
            'Opplysning om barnehage:'
        );

        switch (this.props.barnehageplass.harBarnehageplass) {
            case BarnehageplassVerdier.Ja:
                return React.createElement(
                    'div',
                    null,
                    header,
                    React.createElement(
                        'ul',
                        null,
                        React.createElement(
                            OppsummeringsListeElement,
                            {
                                tekst: 'Barnet har barnehageplass'
                            },
                            React.createElement(
                                'h4',
                                null,
                                'Dato:'
                            ),
                            React.createElement(
                                'p',
                                null,
                                this.props.barnehageplass.dato
                            ),
                            React.createElement(
                                'h4',
                                null,
                                'Barnehageplass i kommunen:'
                            ),
                            React.createElement(
                                'p',
                                null,
                                this.props.barnehageplass.kommune
                            ),
                            React.createElement(
                                'h4',
                                null,
                                'Antall timer:'
                            ),
                            React.createElement(
                                'p',
                                null,
                                this.props.barnehageplass.antallTimer
                            )
                        )
                    )
                );
            case BarnehageplassVerdier.JaSkalSlutte:
                return React.createElement(
                    'div',
                    null,
                    header,
                    React.createElement(
                        'ul',
                        null,
                        React.createElement(
                            OppsummeringsListeElement,
                            {
                                tekst: 'Barnet går i barnehagen, men skal slutte'
                            },
                            React.createElement(
                                'h4',
                                null,
                                'Dato:'
                            ),
                            React.createElement(
                                'p',
                                null,
                                this.props.barnehageplass.dato
                            ),
                            React.createElement(
                                'h4',
                                null,
                                'Barnehageplass i kommunen:'
                            ),
                            React.createElement(
                                'p',
                                null,
                                this.props.barnehageplass.kommune
                            ),
                            React.createElement(
                                'h4',
                                null,
                                'Antall timer:'
                            ),
                            React.createElement(
                                'p',
                                null,
                                this.props.barnehageplass.antallTimer
                            )
                        )
                    )
                );
            case BarnehageplassVerdier.NeiHarFaatt:
                return React.createElement(
                    'div',
                    null,
                    header,
                    React.createElement(
                        'ul',
                        null,
                        React.createElement(
                            OppsummeringsListeElement,
                            {
                                tekst: 'Barnet har fått barnehageplass, men ikke begynt enda'
                            },
                            React.createElement(
                                'h4',
                                null,
                                'Dato:'
                            ),
                            React.createElement(
                                'p',
                                null,
                                this.props.barnehageplass.dato
                            ),
                            React.createElement(
                                'h4',
                                null,
                                'Barnehageplass i kommunen:'
                            ),
                            React.createElement(
                                'p',
                                null,
                                this.props.barnehageplass.kommune
                            )
                        )
                    )
                );
            case BarnehageplassVerdier.Nei:
                return React.createElement(
                    'div',
                    null,
                    header,
                    React.createElement(
                        'ul',
                        null,
                        React.createElement(OppsummeringsListeElement, {
                            tekst: 'Barnet har ikke barnehageplass'
                        })
                    )
                );
            default:
                return React.createElement('div', null);
        }
    }
});