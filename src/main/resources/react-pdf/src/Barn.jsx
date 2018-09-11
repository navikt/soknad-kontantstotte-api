const Barn = (props) => {
        return (
            <div>
                <h3>Opplysninger om barnet eller barna</h3>
                <ul>
                    <OppsummeringsListeElement
                        tekst={props.mineBarn.navn + ', ' + props.mineBarn.fodselsdato}
                    />
                </ul>
            </div>
        );
};