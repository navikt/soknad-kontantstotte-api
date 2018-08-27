var Barn = React.createClass({
    render: function () {
        return (
            <div>
                <h3>Opplysninger om barnet eller barna</h3>
                <ul>
                    <OppsummeringsListeElement
                        tekst={this.props.mineBarn.navn + ', ' + this.props.mineBarn.fodselsdato}
                    />
                </ul>
            </div>
        );
    }
});