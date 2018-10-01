const spacing = {
    margin: '0 auto 10px auto',
    padding: '0 0 10px 0',
    textAlign: 'center',
    display: 'inline-block',
    width: '60%'
};

const Personalia = (props) => {
    return (
        <div style={spacing}>
            <h2>{props.person.fnr}</h2>
        </div>
    );
};