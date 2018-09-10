var Familieforhold = React.createClass({
    render: function () {
        return (
            <div>
                <h3>Opplysning om familieforhold:</h3>
                <ul>
                    {this.props.familieforhold.borForeldreneSammenMedBarnet === 'JA' &&
                        <OppsummeringsListeElement
                            tekst={'Jeg bor sammen med den andre forelderen'}
                        >
                            <h4>Navnet til den andre forelderen:</h4>
                            <p>{this.props.familieforhold.annenForelderNavn}</p>
                            <h4>Fodselsnummeret til den andre forelderen:</h4>
                            <p>{this.props.familieforhold.annenForelderFodselsnummer}</p>
                        </OppsummeringsListeElement>
                    }

                    {this.props.familieforhold.borForeldreneSammenMedBarnet === 'NEI' &&
                    <OppsummeringsListeElement
                        tekst={'Jeg bor ikke sammen med den andre forelderen'}
                    />
                    }
                </ul>
            </div>
        );
    }
});