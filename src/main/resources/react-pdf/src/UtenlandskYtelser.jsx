const UtenlandskYtelser = (props) => {
    return (
        <Bolk>
            <h3 style={Uppercase}>{props.tekster['utenlandskYtelser.tittel']}</h3>

            <div>
                <OppsummeringsElement
                    sporsmal={props.tekster['utenlandskYtelser.mottarYtelserFraUtland.sporsmal']}
                    svar={props.utenlandskYtelser.mottarYtelserFraUtland}
                />
                {props.utenlandskYtelser.mottarYtelserFraUtlandForklaring !== '' &&
                    <OppsummeringsElement
                        sporsmal={props.tekster['oppsummering.utenlandskYtelser.forklaring.label']}
                        svar={props.utenlandskYtelser.mottarYtelserFraUtlandForklaring}
                    />
                }
            </div>

            {props.familieforhold.borForeldreneSammenMedBarnet === 'JA' &&
                <div>
                    <OppsummeringsElement
                        sporsmal={props.tekster['oppsummering.utenlandskYtelser.mottarAnnenForelderYtelserFraUtland']}
                        svar={props.utenlandskYtelser.mottarAnnenForelderYtelserFraUtland}
                    />

                    {props.utenlandskYtelser.mottarAnnenForelderYtelserFraUtlandForklaring !== '' &&
                        <OppsummeringsElement
                            sporsmal={props.tekster['oppsummering.utenlandskYtelser.forklaring.label']}
                            svar={props.utenlandskYtelser.mottarAnnenForelderYtelserFraUtlandForklaring}
                        />
                    }
                </div>
            }
        </Bolk>
    );
};