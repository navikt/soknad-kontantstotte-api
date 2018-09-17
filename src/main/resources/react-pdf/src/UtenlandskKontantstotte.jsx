const UtenlandskKontantstotte = (props) => {
        return (
            <Bolk>
                <h3 style={Uppercase}>{props.tekster['utenlandskKontantstotte.tittel']}</h3>

                <OppsummeringsElement
                    sporsmal={props.tekster['utenlandskKontantstotte.mottarKontantstotteFraUtlandet.sporsmal']}
                    svar={props.utenlandskKontantstotte.mottarKontantstotteFraUtlandet}
                />
            </Bolk>
        );
};


