import React, { Component }  from "react";
import { AppBar, Typography } from '@material-ui/core';
import { Alert, AlertTitle } from '@material-ui/lab';
import "./index.css"

class Header extends Component {
    render() {
        return (
            <React.Fragment>
                <Alert severity="warning" style={{ marginBottom: "8px" }}>
                    <AlertTitle>Domain change notice</AlertTitle>
                    Due to registrar issues outside our control, this service is moving to{' '}
                    <a href="https://slink.earth/" style={{ fontWeight: 'bold' }}>https://slink.earth/</a>.
                    The old <b>slink.sbs</b> domain will be shut down soon and stop working. Please update your bookmarks.
                </Alert>
                <AppBar style={{ marginBottom: "15px" }} position="static">
                    <Typography style={{ margin: "5px" }} display={`inline`} align={'center'} variant="h4">
                        Link shorter! Fast and free!
                    </Typography>
                </AppBar>
            </React.Fragment>
        )
    }
}
export default Header
