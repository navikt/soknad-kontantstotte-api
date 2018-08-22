var Barn = React.createClass({
    render: function () {
        return (
            <div>
                <h3>Opplysninger om barnet eller barna</h3>
                <ul>
                    <li>{this.props.barn.navn}, {this.props.barn.fodselsdato}</li>
                </ul>
            </div>
        );
    }
});