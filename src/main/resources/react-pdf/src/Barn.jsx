const Barn = (props) => {
        return (
            <Bolk>
                <h3 style={Uppercase}>{props.tekster['barn.tittel']}</h3>
                <h3>{props.tekster['oppsummering.barn.subtittel']}</h3>
                <OppsummeringsElement
                    sporsmal={props.tekster['barn.navn']}
                    svar={props.mineBarn.navn}
                />
                <OppsummeringsElement
                    sporsmal={props.tekster['barn.fodselsdato']}
                    svar={props.mineBarn.fodselsdato}
                />
            </Bolk>
        );
};