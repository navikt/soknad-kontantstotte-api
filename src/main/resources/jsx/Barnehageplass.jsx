var Barnehageplass = React.createClass({
    render: function () {
        var BarnehageplassVerdier = {
            Nei: 'Nei',
            NeiHarFaatt: 'NeiHarFaatt',
            Ja: 'Ja',
            JaSkalSlutte: 'JaSkalSlutte',
            Ubesvart: 'Ubesvart',
        };

        var header = <h3>Opplysning om barnehage:</h3>;

        switch (this.props.barnehageplass.harBarnehageplass) {
            case BarnehageplassVerdier.Ja:
                return (
                    <div>
                        {header}
                        <ul>
                            <OppsummeringsListeElement
                                tekst={'Barnet har barnehageplass'}
                            >
                                <h4>Dato:</h4>
                                <p>{this.props.barnehageplass.dato}</p>
                                <h4>Barnehageplass i kommunen:</h4>
                                <p>{this.props.barnehageplass.kommune}</p>
                                <h4>Antall timer:</h4>
                                <p>{this.props.barnehageplass.antallTimer}</p>
                            </OppsummeringsListeElement>
                        </ul>
                    </div>
                );
            case BarnehageplassVerdier.JaSkalSlutte:
                return (
                    <div>
                        {header}
                        <ul>
                            <OppsummeringsListeElement
                                tekst={'Barnet går i barnehagen, men skal slutte'}
                            >
                                <h4>Dato:</h4>
                                <p>{this.props.barnehageplass.dato}</p>
                                <h4>Barnehageplass i kommunen:</h4>
                                <p>{this.props.barnehageplass.kommune}</p>
                                <h4>Antall timer:</h4>
                                <p>{this.props.barnehageplass.antallTimer}</p>
                            </OppsummeringsListeElement>
                        </ul>
                    </div>
                );
            case BarnehageplassVerdier.NeiHarFaatt:
                return (
                    <div>
                        {header}
                        <ul>
                            <OppsummeringsListeElement
                                tekst={'Barnet har fått barnehageplass, men ikke begynt enda'}
                            >
                                <h4>Dato:</h4>
                                <p>{this.props.barnehageplass.dato}</p>
                                <h4>Barnehageplass i kommunen:</h4>
                                <p>{this.props.barnehageplass.kommune}</p>
                            </OppsummeringsListeElement>
                        </ul>
                    </div>
                );
            case BarnehageplassVerdier.Nei:
                return (
                    <div>
                        {header}
                        <ul>
                            <OppsummeringsListeElement
                                tekst={'Barnet har ikke barnehageplass'}
                            />
                        </ul>
                    </div>
                );
            default:
                return <div/>;
        }
    }
});