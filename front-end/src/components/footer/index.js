import React, { Component }  from "react";
import { Card, CardContent } from '@material-ui/core';
import "./index.css"

class Footer extends Component {
    render() {
        return (
            <>
                <Card style={{ marginTop: "15px", marginRight: "5px", marginLeft: "5px" }}>
                    <CardContent>
                        <span>Technologies used: Java (Groovy), MongoDB, ReactJS</span>
                    </CardContent>
                </Card>
                <Card style={{ margin: "5px" }}>
                    <CardContent>
                        <span>Developed by Octanium91</span>
                    </CardContent>
                </Card>
            </>
        )
    }
}
export default Footer