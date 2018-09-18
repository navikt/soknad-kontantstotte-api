const UtenlandskeYtelser = (props) => {
    return (
        <Bolk>
            <h3 style={Uppercase}>{props.tekster['utenlandskeYtelser.tittel']}</h3>

            <div>
                <OppsummeringsElement
                    sporsmal={props.tekster['utenlandskeYtelser.mottarYtelserFraUtland.sporsmal']}
                    svar={props.utenlandskeYtelser.mottarYtelserFraUtland}
                />
                {props.utenlandskeYtelser.mottarYtelserFraUtlandForklaring !== '' &&
                    <OppsummeringsElement
                        sporsmal={props.tekster['oppsummering.utenlandskeYtelser.forklaring.label']}
                        svar={props.utenlandskeYtelser.mottarYtelserFraUtlandForklaring}
                    />
                }
            </div>

            {props.familieforhold.borForeldreneSammenMedBarnet === 'JA' &&
                <div>
                    <OppsummeringsElement
                        sporsmal={props.tekster['oppsummering.utenlandskeYtelser.mottarAnnenForelderYtelserFraUtland']}
                        svar={props.utenlandskeYtelser.mottarAnnenForelderYtelserFraUtland}
                    />

                    {props.utenlandskeYtelser.mottarAnnenForelderYtelserFraUtlandForklaring !== '' &&
                        <OppsummeringsElement
                            sporsmal={props.tekster['oppsummering.utenlandskeYtelser.forklaring.label']}
                            svar={props.utenlandskeYtelser.mottarAnnenForelderYtelserFraUtlandForklaring}
                        />
                    }
                </div>
            }
        </Bolk>
    );
};