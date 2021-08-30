import React, { Component }  from "react";
import GitHubButton from 'react-github-btn'
import { Card, CardContent, Typography, Link } from '@material-ui/core';
import "./index.css"
import apiService from "../../service/ApiService";

class Footer extends Component {

    state = {
        preDayLinksCreated: 0,
        preDayLinksExecuted: 0,
        totalLinksCreated: 0
    }

    componentDidMount() {
        apiService.serviceStatistic().then((req) => {
            if (req.data) {
                this.setState(req.data)
            }
        })
    }

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
                        <Typography><b>Website: </b><Link href="https://octanium91.github.io/" rel="noreferrer" target="_blank">octanium91.github.io</Link></Typography>
                    </CardContent>
                </Card>
                <Card style={{ marginTop: "15px", marginRight: "5px", marginLeft: "5px" }}>
                    <CardContent>
                        <Typography className={"d-flex justify-content-center"}><b>GitHub</b></Typography>
                        <div className={"d-flex justify-content-center"}>
                            <div style={{margin: "5px"}}>
                                <GitHubButton href="https://github.com/Octanium91/request-shorter" data-size="large"
                                              aria-label="Source Code">Source Code</GitHubButton>
                            </div>
                            <div style={{margin: "5px"}}>
                                <GitHubButton href="https://github.com/octanium91/request-shorter/subscription"
                                              data-icon="octicon-eye" data-size="large"
                                              aria-label="Watch octanium91/request-shorter on GitHub">Watch</GitHubButton>
                            </div>
                            <div style={{margin: "5px"}}>
                                <GitHubButton href="https://github.com/sponsors/octanium91" data-icon="octicon-heart"
                                              data-size="large"
                                              aria-label="Sponsor @octanium91 on GitHub">Sponsor</GitHubButton>
                            </div>
                        </div>
                    </CardContent>
                </Card>
                <Card style={{ marginTop: "15px", marginRight: "5px", marginLeft: "5px" }}>
                    <CardContent>
                        <span className={"d-flex justify-content-center"}>{`${this.state.totalLinksCreated}/${this.state.preDayLinksCreated}/${this.state.preDayLinksExecuted}`}</span>
                    </CardContent>
                </Card>
            </>
        )
    }
}
export default Footer