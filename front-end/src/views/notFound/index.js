import React from "react";
import { useParams } from 'react-router-dom';
import { Container, Button, Typography, Tooltip } from '@material-ui/core';
import Header from "../../components/header";
import Footer from "../../components/footer";

function NotFound(props) {

    const code = useParams().code

    return (
        <React.Fragment>
            <Header />
            <Container maxWidth="sm">
                <div style={{marginTop: "30px"}} className={"d-flex justify-content-center"}>
                    <Typography variant="h4" component="h3"> Oops short link '<b>{code}</b>' not found!</Typography>
                </div>
                <div style={{ marginTop: "30px" }} className={"d-flex justify-content-center"}>
                    <Tooltip title="Go to home page" aria-label="Go to home page">
                        <Button variant="contained" color="primary" onClick={() => {
                            props.history.push("/")
                        }}>
                            Go on home page
                        </Button>
                    </Tooltip>
                </div>
                <Footer />
            </Container>
        </React.Fragment>
    )
}

export default NotFound