const PersonaliaOgBarnOppsummering = (props) => {
        return (
            <div>
                <h4>Det søkes kontantstøtte for:</h4>
                <p>{props.barn.navn}</p>
                <p>{'Fødselsnummer: ' + props.barn.fodselsdato}</p>
                <h4>Av forelder:</h4>
                <p>{'Fødselsnummer: ' + props.person.fnr}</p>
            </div>
        )
};