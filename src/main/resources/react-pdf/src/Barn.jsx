const Barn = (props) => {
        return (
            <Bolk>
                <h3 style={Uppercase}>{props.tekster['barn.tittel']}</h3>
                <OppsummeringsElement svar={props.mineBarn.navn + " - " + props.mineBarn.fodselsdato} />
            </Bolk>
        );
};