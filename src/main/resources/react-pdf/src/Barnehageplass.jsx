const Barnehageplass = (props) => {
    const date = new Date(props.barnehageplass.dato);
    const dateString = date.getDay() + "." + date.getMonth() + "." + date.getFullYear();

    return (
        <Bolk>
            <h3 style={Uppercase}>{props.tekster['oppsummering.barnehageplass.tittel']}</h3>
            <OppsummeringsElement
                sporsmal={props.tekster['oppsummering.barnehageplass.harBarnehageplass']}
                svar={props.barnehageplass.harBarnehageplass}
            />

            <OppsummeringsElement
                sporsmal={props.tekster['barnehageplass.barnBarnehageplassStatus']}
                svar={props.tekster['barnehageplass.'.concat(props.barnehageplass.barnBarnehageplassStatus)]}
            />
        </Bolk>
    )
};