const Familieforhold = (props) => {
        return (
            <div>
                <h3>Opplysning om familieforhold:</h3>

                <OppsummeringsElement
                    sporsmal={'Bor begge foreldrene med barnet?'}
                    svar={props.familieforhold.borForeldreneSammenMedBarnet}
                />

                {props.familieforhold.borForeldreneSammenMedBarnet === 'JA' &&
                    <>
                        <OppsummeringsElement sporsmal={'Navnet til den andre forelderen:'} svar={props.familieforhold.annenForelderNavn} />
                        <OppsummeringsElement
                        sporsmal={'Navnet til den andre forelderen:'}
                        svar={props.familieforhold.annenForelderFodselsnummer}
                        />
                        <OppsummeringsElement
                            sporsmal={'Har barnets andre forelder bodd eller vært yrkesaktiv i Norge eller et annet EØS-land i minst 5 år?'}
                            svar={props.familieforhold.annenForelderYrkesaktivINorgeEOSIMinstFemAar}
                        />
                    </>
                }

                {props.familieforhold.borForeldreneSammenMedBarnet === 'NEI' &&
                    <OppsummeringsElement
                        sporsmal={'Er det avklart delt bosted for barnet?'}
                        svar={props.familieforhold.erAvklartDeltBosted}
                    />
                }
            </div>
        );
};