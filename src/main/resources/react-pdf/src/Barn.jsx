const Barn = (props) => {
        return (
            <Bolk>
                <h3>{props.tekster['barn.tittel'].toUpperCase()}</h3>
                <OppsummeringsElement svar={props.mineBarn.navn + " - " + props.mineBarn.fodselsdato} />
            </Bolk>
        );
};