const ArbeidIUtlandet = (props) => {
    return (
        <Bolk>
            <h3 style={Uppercase}>{props.tekster['arbeidIUtlandet.tittel']}</h3>

            <div>
                <OppsummeringsElement
                    sporsmal={props.tekster['arbeidIUtlandet.arbeiderIUtlandetEllerKontinentalsokkel.sporsmal']}
                    svar={props.arbeidIUtlandet.arbeiderIUtlandetEllerKontinentalsokkel}
                />
                {props.arbeidIUtlandet.arbeiderIUtlandetEllerKontinentalsokkelForklaring !== '' &&
                    <OppsummeringsElement
                        sporsmal={props.tekster['arbeidIUtlandet.forklaring.hjelpetekst']}
                        svar={props.arbeidIUtlandet.arbeiderIUtlandetEllerKontinentalsokkelForklaring}
                    />
                }
            </div>

            {props.familieforhold.borForeldreneSammenMedBarnet === 'JA' &&
                <div>
                    <OppsummeringsElement
                        sporsmal={props.tekster['arbeidIUtlandet.arbeiderAnnenForelderIUtlandet.sporsmal']}
                        svar={props.arbeidIUtlandet.arbeiderAnnenForelderIUtlandet}
                    />

                    {props.arbeidIUtlandet.arbeiderAnnenForelderIUtlandetForklaring !== '' &&
                        <OppsummeringsElement
                            sporsmal={props.tekster['arbeidIUtlandet.forklaring.hjelpetekst']}
                            svar={props.arbeidIUtlandet.arbeiderAnnenForelderIUtlandetForklaring}
                        />
                    }
                </div>
            }
        </Bolk>
    );
};