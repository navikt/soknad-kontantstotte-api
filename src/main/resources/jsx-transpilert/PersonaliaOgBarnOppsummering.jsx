var PersonaliaOgBarnOppsummering = React.createClass({
    displayName: 'PersonaliaOgBarnOppsummering',

    render: function () {
        return React.createElement(
            'div',
            null,
            React.createElement(
                'h4',
                null,
                'Det s\xF8kes kontantst\xF8tte for:'
            ),
            React.createElement(
                'p',
                null,
                this.props.barn.navn
            ),
            React.createElement(
                'p',
                null,
                'Fødselsnummer: ' + this.props.barn.fodselsdato
            ),
            React.createElement(
                'h4',
                null,
                'Av forelder:'
            ),
            React.createElement(
                'p',
                null,
                'Fødselsnummer: ' + this.props.person.fnr
            )
        );
    }
});