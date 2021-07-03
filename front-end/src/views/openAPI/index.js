import React from "react";
import {Container, Button, Typography, Tooltip, CardContent, Card} from '@material-ui/core';
import Header from "../../components/header";
import Footer from "../../components/footer";

function OpenAPI(props) {

    return (
        <React.Fragment>
            <Header />
            <Container maxWidth="sm">
                <div style={{marginTop: "30px"}} className={"d-flex justify-content-center"}>
                    <Typography variant="h4" component="h3">Open API</Typography>
                </div>
                <div style={{marginTop: "15px"}} className={"d-flex justify-content-center"}>
                    <Card>
                        <CardContent>
                            <Typography variant="h5" component="h4">You can use simple API to create and execute short links!</Typography>
                        </CardContent>
                    </Card>
                </div>
                <Card style={{ marginTop: "15px", marginRight: "5px", marginLeft: "5px" }}>
                    <Typography variant="h5" component="h4">Create link:</Typography>
                    <CardContent>
                        <Typography><b>Method:</b> GET</Typography>
                        <Typography><b>URL:</b> {process.env.REACT_APP_APP_URL}/api/open/create/link?link=<i>[SITE LINK]</i></Typography>
                        <Typography><b>Response:</b> String</Typography>
                        <Typography><b>Response data:</b> <i>[SHORT LINK CODE]</i></Typography>
                    </CardContent>
                </Card>
                <Card style={{ marginTop: "15px", marginRight: "5px", marginLeft: "5px" }}>
                    <Typography variant="h5" component="h4">Execute link:</Typography>
                    <CardContent>
                        <Typography><b>Method:</b> GET</Typography>
                        <Typography><b>URL:</b> {process.env.REACT_APP_APP_URL}/api/open/execute?shortCode=<i>[SHORT LINK CODE]</i></Typography>
                        <Typography><b>Response:</b> Redirect</Typography>
                        <Typography><b>Response data:</b> Redirect content <i>(if redirects are allowed)</i></Typography>
                    </CardContent>
                </Card>
                <div style={{ marginTop: "10px" }} className={"d-flex justify-content-center"}>
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

export default OpenAPI