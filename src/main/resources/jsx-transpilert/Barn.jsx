var Barn = React.createClass({
    displayName: 'Barn',

    render: function () {
        return React.createElement(
            'div',
            null,
            React.createElement(
                'h3',
                null,
                'Opplysninger om barnet eller barna'
            ),
            React.createElement(
                'ul',
                null,
                React.createElement(OppsummeringsListeElement, {
                    tekst: this.props.mineBarn.navn + ', ' + this.props.mineBarn.fodselsdato
                })
            )
        );
    }
});