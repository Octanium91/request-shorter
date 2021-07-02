import React, { Component }  from "react";
import { Card, CardContent, Typography, Link } from '@material-ui/core';
import "./index.css"

class Footer extends Component {
    render() {
        return (
            <>
                <Card style={{ marginTop: "15px", marginRight: "5px", marginLeft: "5px" }}>
                    <CardContent>
                        <Typography><b>Technologies used:</b> Java, MongoDB, ReactJS, Docker, GitHub</Typography>
                    </CardContent>
                </Card>
                <Card style={{ margin: "5px" }}>
                    <CardContent>
                        <Typography>Developed by Octanium91</Typography>
                        <Typography><b>Website: </b><Link href="https://octanium91.github.io/" target="_blank">octanium91.github.io</Link></Typography>
                    </CardContent>
                </Card>
            </>
        )
    }
}
export default Footer