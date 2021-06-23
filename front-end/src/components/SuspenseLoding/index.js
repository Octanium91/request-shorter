import React, { Component }  from "react";
import { CircularProgress, Container } from '@material-ui/core';
import "./index.css"

class Loading extends Component {
    render() {
        return (
            <Container maxWidth="sm">
                <div className={`loading`}>
                    <CircularProgress />
                </div>
            </Container>
        )
    }
}
export default Loading