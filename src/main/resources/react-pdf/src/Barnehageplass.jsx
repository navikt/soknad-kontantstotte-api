const Barnehageplass = (props) => {
    const date = new Date(props.barnehageplass.dato);
    const dateString = date.getDay() + "." + date.getMonth() + "." + date.getFullYear();

    return (
        <div>
            <h3>{props.tekster['barnehageplass.tittel']}</h3>
            <OppsummeringsElement
                sporsmal={props.tekster['barnehageplass.sporsmal']}
                svar={props.barnehageplass.harBarnehageplass}
            />

            {props.barnehageplass.harBarnehageplass !== 'Nei' &&
                <>
                    <OppsummeringsElement
                        sporsmal={props.tekster['barnehageplass.harFaattPlassDato']}
                        svar={dateString}
                    />
                    <OppsummeringsElement
                        sporsmal={props.tekster['barnehageplass.kommune']}
                        svar={props.barnehageplass.kommune}
                    />
                </>
            }

            {props.barnehageplass.harBarnehageplass.indexOf('Ja') > -1 &&
                <OppsummeringsElement
                    sporsmal={props.tekster['barnehageplass.antallTimer']}
                    svar={props.barnehageplass.antallTimer}
                />
            }
        </div>
    )
};