const Barnehageplass = (props) => {
    const date = new Date(props.barnehageplass.dato);
    const dateString = date.getDay() + "." + date.getMonth() + "." + date.getFullYear();

    return (
        <div>
            <h3>Opplysning om barnehage</h3>
            <OppsummeringsElement sporsmal={'Har barnet barnehageplass?'} svar={props.barnehageplass.harBarnehageplass}/>

            {props.barnehageplass.harBarnehageplass !== 'Nei' &&
                <>
                    <OppsummeringsElement
                        sporsmal={'Fra hvilken dato har barnet barnehageplass?'}
                        svar={dateString}
                    />
                    <OppsummeringsElement sporsmal={'Hvilken kommune har barnet barnehageplass?'} svar={props.barnehageplass.kommune} />
                </>
            }

            {props.barnehageplass.harBarnehageplass.indexOf('Ja') > -1 &&
                <OppsummeringsElement sporsmal={'Hvor mange timer går barnet i barnehagen?'} svar={props.barnehageplass.antallTimer} />
            }
        </div>
    )
};