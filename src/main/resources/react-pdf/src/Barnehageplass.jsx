const Barnehageplass = (props) => {
    const date = new Date(props.barnehageplass.dato);
    const dateString = date.getDay() + "." + date.getMonth() + "." + date.getFullYear();

    let barnBarnehageplassStatusKey = 'Ubesvart';

    const BarnehageplassStatus = {
        GAR_IKKE_I_BARNEHAGE : 'garIkkeIBarnehage',
        HAR_BARNEHAGEPLASS : 'harBarnehageplass',
        HAR_SLUTTET_I_BARNEHAGE : 'harSluttetIBarnehage',
        SKAL_BEGYNNE_I_BARNEHAGE : 'skalBegynneIBarnehage',
        SKAL_SLUTTE_I_BARNEHAGE : 'skalSlutteIBarnehage',
    };

    switch (props.barnehageplass.barnBarnehageplassStatus) {
        case BarnehageplassStatus.GAR_IKKE_I_BARNEHAGE:
            barnBarnehageplassStatusKey = 'barnehageplass.garIkkeIBarnehage';
            break;
        case BarnehageplassStatus.HAR_BARNEHAGEPLASS:
            barnBarnehageplassStatusKey = 'barnehageplass.harBarnehageplass';
            break;
        case BarnehageplassStatus.HAR_SLUTTET_I_BARNEHAGE:
            barnBarnehageplassStatusKey = 'barnehageplass.harSluttetIBarnehage';
            break;
        case BarnehageplassStatus.SKAL_BEGYNNE_I_BARNEHAGE:
            barnBarnehageplassStatusKey = 'barnehageplass.skalBegynneIBarnehage';
            break;
        case BarnehageplassStatus.SKAL_SLUTTE_I_BARNEHAGE:
            barnBarnehageplassStatusKey = 'barnehageplass.skalSlutteIBarnehage';
            break;
    }

    return (
        <Bolk>
            <h3 style={Uppercase}>{props.tekster['oppsummering.barnehageplass.tittel']}</h3>
            <OppsummeringsElement
                sporsmal={props.tekster['oppsummering.barnehageplass.harBarnehageplass']}
                svar={props.barnehageplass.harBarnehageplass}
            />

            <OppsummeringsElement
                sporsmal={props.tekster['barnehageplass.barnBarnehageplassStatus']}
                svar={props.tekster[barnBarnehageplassStatusKey]}
            />

            {props.barnehageplass.barnBarnehageplassStatus === BarnehageplassStatus.HAR_SLUTTET_I_BARNEHAGE &&
                <>
                    <OppsummeringsElement
                        sporsmal={props.tekster['barnehageplass.harSluttetIBarnehage.dato.sporsmal']}
                        svar={props.barnehageplass.harSluttetIBarnehageDato}
                    />
                    <OppsummeringsElement
                        sporsmal={props.tekster['barnehageplass.harSluttetIBarnehage.kommune.sporsmal']}
                        svar={props.barnehageplass.harSluttetIBarnehageKommune}
                    />
                    <OppsummeringsElement
                        sporsmal={props.tekster['barnehageplass.harSluttetIBarnehage.antallTimer.sporsmal']}
                        svar={props.barnehageplass.harSluttetIBarnehageAntallTimer}
                    />
                </>
            }

            {props.barnehageplass.barnBarnehageplassStatus === BarnehageplassStatus.SKAL_SLUTTE_I_BARNEHAGE &&
            <>
                <OppsummeringsElement
                    sporsmal={props.tekster['barnehageplass.skalSlutteIBarnehage.dato.sporsmal']}
                    svar={props.barnehageplass.skalSlutteIBarnehageDato}
                />
                <OppsummeringsElement
                    sporsmal={props.tekster['barnehageplass.skalSlutteIBarnehage.kommune.sporsmal']}
                    svar={props.barnehageplass.skalSlutteIBarnehageKommune}
                />
                <OppsummeringsElement
                    sporsmal={props.tekster['barnehageplass.skalSlutteIBarnehage.antallTimer.sporsmal']}
                    svar={props.barnehageplass.skalSlutteIBarnehageAntallTimer}
                />
            </>
            }

            {props.barnehageplass.barnBarnehageplassStatus === BarnehageplassStatus.SKAL_BEGYNNE_I_BARNEHAGE &&
            <>
                <OppsummeringsElement
                    sporsmal={props.tekster['barnehageplass.skalBegynneIBarnehage.dato.sporsmal']}
                    svar={props.barnehageplass.skalBegynneIBarnehageDato}
                />
                <OppsummeringsElement
                    sporsmal={props.tekster['barnehageplass.skalBegynneIBarnehage.kommune.sporsmal']}
                    svar={props.barnehageplass.skalBegynneIBarnehageKommune}
                />
                <OppsummeringsElement
                    sporsmal={props.tekster['barnehageplass.skalBegynneIBarnehage.antallTimer.sporsmal']}
                    svar={props.barnehageplass.skalBegynneIBarnehageAntallTimer}
                />
            </>
            }

            {props.barnehageplass.barnBarnehageplassStatus === BarnehageplassStatus.HAR_BARNEHAGEPLASS &&
            <>
                <OppsummeringsElement
                    sporsmal={props.tekster['barnehageplass.harBarnehageplass.antallTimer.sporsmal']}
                    svar={props.barnehageplass.harBarnehageplassAntallTimer}
                />
                <OppsummeringsElement
                    sporsmal={props.tekster['barnehageplass.harBarnehageplass.dato.sporsmal']}
                    svar={props.barnehageplass.harBarnehageplassDato}
                />
                <OppsummeringsElement
                    sporsmal={props.tekster['barnehageplass.harBarnehageplass.kommune.sporsmal']}
                    svar={props.barnehageplass.harBarnehageplassKommune}
                />
            </>
            }
        </Bolk>
    )
};