const renderApp = (soknad) => {
    ReactDOMServer.renderToStaticMarkup(
        React.render(<SoknadPdf soknad={soknad} />, document.getElementById('app'))
    )
};
