// TODO: Beregne alder fra fødselsdato

var Barn = React.createClass({
    render: function () {
        return (
            <div>
                <h3>Opplysninger om barnet eller barna</h3>
                <ul>
                    <li>{this.props.mineBarn.navn}, {this.props.mineBarn.fodselsdato}</li>
                </ul>
            </div>
        );
    }
});