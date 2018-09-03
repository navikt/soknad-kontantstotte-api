const Familieforhold = (props) => {
        return (
            <div>
                <h3>Opplysning om familieforhold:</h3>
                <ul>
                    <li>Har foreldrene delt bosted: {props.familieforhold.borForeldreneSammenMedBarnet}</li>
                    {props.familieforhold.borForeldreneSammenMedBarnet === 'JA' &&
                        <OppsummeringsListeElement
                            tekst={'Jeg bor sammen med den andre forelderen'}
                        >
                            <h4>Navnet til den andre forelderen:</h4>
                            <p>{props.familieforhold.annenForelderNavn}</p>
                            <h4>Fodselsnummeret til den andre forelderen:</h4>
                            <p>{props.familieforhold.annenForelderFodselsnummer}</p>
                        </OppsummeringsListeElement>
                    }
                </ul>
            </div>
        );
};