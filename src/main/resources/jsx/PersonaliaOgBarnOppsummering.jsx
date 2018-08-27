var PersonaliaOgBarnOppsummering = React.createClass({
    render: function () {
        return (
            <div>
                <h4>Det søkes kontantstøtte for:</h4>
                <p>{this.props.barn.navn}</p>
                <p>{'Fødselsnummer: ' + this.props.barn.fodselsdato}</p>
                <h4>Av forelder:</h4>
                <p>{this.props.person.navn}</p>
                <p>{'Fødselsnummer: ' + this.props.person.fnr}</p>
            </div>
        )
    }
});