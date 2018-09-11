const spacing = {
    margin: '0 0 10px 0',
    padding: '0 0 10px 0',
    textAlign: 'center'
};

const Personalia = (props) => {
    return (
        <div style={spacing}>
            <h2>{props.person.fnr}</h2>
        </div>
    );
};