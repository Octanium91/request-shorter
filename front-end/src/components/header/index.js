import React, { Component }  from "react";
import { AppBar, Typography } from '@material-ui/core';
import "./index.css"

class Header extends Component {
    render() {
        return (
            <AppBar style={{ marginBottom: "15px" }} position="static">
                <Typography style={{ margin: "5px" }} display={`inline`} align={'center'} variant="h4">
                    Link shorter! Fast and free!
                </Typography>
            </AppBar>
        )
    }
}
export default Header