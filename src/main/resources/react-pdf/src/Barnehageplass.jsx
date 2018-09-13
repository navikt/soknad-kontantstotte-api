const Barnehageplass = (props) => {
    const date = new Date(props.barnehageplass.dato);
    const dateString = date.getDay() + "." + date.getMonth() + "." + date.getFullYear();

    let barnBarnehageplassStatusKey = 'Ubesvart';

    switch (props.barnehageplass.barnBarnehageplassStatus) {
        case 'garIkkeIBarnehage':
            barnBarnehageplassStatusKey = 'barnehageplass.garIkkeIBarnehage';
            break;
        case 'harBarnehageplass':
            barnBarnehageplassStatusKey = 'barnehageplass.harBarnehageplass';
            break;
        case 'harSluttetIBarnehage':
            barnBarnehageplassStatusKey = 'barnehageplass.harSluttetIBarnehage';
            break;
        case 'skalBegynneIBarnehage':
            barnBarnehageplassStatusKey = 'barnehageplass.skalBegynneIBarnehage';
            break;
        case 'skalSlutteIBarnehage':
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
        </Bolk>
    )
};