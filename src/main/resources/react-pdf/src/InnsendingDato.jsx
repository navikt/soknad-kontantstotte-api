const Dato = {
    display: 'inline-block',
    width: '20%',

};
const tekst = {
    whiteSpace: 'pre-wrap',
    wordBreak: 'break-word',
};


const InnsendingDato = (props) => {
    return (
      <div style={Dato}>
          <h4>{props.tekster['oppsummering.innsendingsdato']}</h4>
          <p style={tekst}>{props.dato}</p>
      </div>
    );
};