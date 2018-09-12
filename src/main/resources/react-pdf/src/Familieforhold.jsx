const Familieforhold = (props) => {
    return (
        <Bolk>
            <h3 style={Uppercase}>{props.tekster['familieforhold.tittel']}</h3>

            <OppsummeringsElement
                sporsmal={props.tekster['familieforhold.borForeldreneSammenMedBarnet.sporsmal']}
                svar={props.familieforhold.borForeldreneSammenMedBarnet}
            />

            {props.familieforhold.borForeldreneSammenMedBarnet === 'JA' &&
                <>
                    <OppsummeringsElement
                        sporsmal={props.tekster['oppsummering.familieforhold.annenForelderNavn.label']}
                        svar={props.familieforhold.annenForelderNavn}
                    />
                    <OppsummeringsElement
                    sporsmal={props.tekster['oppsummering.familieforhold.annenForelderFodselsnummer.label']}
                    svar={props.familieforhold.annenForelderFodselsnummer}
                    />
                    <OppsummeringsElement
                        sporsmal={props.tekster['familieforhold.annenForelderYrkesaktivINorgeEOSIMinstFemAar.sporsmal']}
                        svar={props.familieforhold.annenForelderYrkesaktivINorgeEOSIMinstFemAar}
                    />
                </>
            }

            {props.familieforhold.borForeldreneSammenMedBarnet === 'NEI' &&
                <OppsummeringsElement
                    sporsmal={props.tekster['familieforhold.erAvklartDeltBosted.sporsmal']}
                    svar={props.familieforhold.erAvklartDeltBosted}
                />
            }
        </Bolk>
    );
};