const Barn = (props) => {
        return (
            <div>
                <h3>{props.tekster['barn.tittel']}</h3>
                <OppsummeringsElement svar={props.mineBarn.navn + " - " + props.mineBarn.fodselsdato} />
            </div>
        );
};