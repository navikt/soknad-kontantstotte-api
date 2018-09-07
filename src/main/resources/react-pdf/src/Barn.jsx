const Barn = (props) => {
        return (
            <div>
                <h3>Opplysninger om barnet eller barna</h3>
                <OppsummeringsElement svar={props.mineBarn.navn + " - " + props.mineBarn.fodselsdato} />
            </div>
        );
};