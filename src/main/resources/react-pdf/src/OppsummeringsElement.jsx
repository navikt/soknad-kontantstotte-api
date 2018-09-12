const OppsummeringsElement = (props) => {
    return (
      <div>
          { props.sporsmal &&
            <h4>{props.sporsmal}</h4>
          }
          <p>{props.svar}</p>
      </div>
    );
};