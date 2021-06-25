import React, { useState, useEffect } from "react";
import { Select, TextField, Tooltip, Grid, Divider, Input, CircularProgress, Container, Typography, Button, Fab } from '@material-ui/core';
import { FileCopy } from '@material-ui/icons';
import Header from "../../components/header";
import '../../App.css';
import apiService from "../../service/ApiService";

function Home() {

  const [createLinkLoading, setCreateLinkLoading] = useState(false)
  const [showProtocol, setShowProtocol] = useState(false)
  const [clientLink, setClientLink] = useState('');
  const [createdLink, setCreatedLink] = useState('')
  const [linkProtocol, setLinkProtocol] = useState(`HTTPS`)

  const handlerLinkOnChange = (event) => {
    setClientLink(event.target.value)
  }

  const handlerLinkProtocolOnChange = (event) => {
    setLinkProtocol(event.target.value)
  }

  const handlerCreateOnClick = () => {
    if (clientLink) {
      let httpPrefix = ``
      if (clientLink.toLowerCase().indexOf(`http`) === -1) {
        httpPrefix = `${linkProtocol.toLowerCase()}://`
      }
      setCreateLinkLoading(true)
      apiService.createShortLink("LINK", `${httpPrefix}${clientLink}`).then((req) => {
        if (req.data) {
          setCreatedLink(req.data)
          setCreateLinkLoading(false)
        }
      })
    }
  }

  useEffect(() => {
    console.log("site url", process.env.REACT_APP_APP_URL)
  }, [])

  useEffect(() => {
    if (clientLink) {
      setShowProtocol(clientLink.toLowerCase().indexOf(`http`) === -1)
    } else {
      setShowProtocol(false)
    }
  }, [clientLink])

  return (
      <React.Fragment>
        <Header />
        <Container maxWidth="sm">
          <div style={{ display: "flax", alignItems: "center", justifyContent: "center" }}>
            <Grid container justify="center" alignItems="center">
              <Typography color={'textPrimary'} variant={'subtitle1'}>{"For create link, enter your long link in input and press on 'CREATE LINK!' button and its all!"}</Typography>
            </Grid>
            <Divider style={{ marginTop: "15px", marginBottom: "20px" }} />
            <Grid container spacing={1} justify="center" alignItems="center">
              <Grid style={{ width: "99%" }} item>
                {showProtocol&&<Select
                    native
                    value={linkProtocol}
                    onChange={handlerLinkProtocolOnChange}
                  >
                  <option value={"HTTPS"}>HTTPS</option>
                  <option value={"HTTP"}>HTTP</option>
                </Select>}
                <Input defaultValue={clientLink} placeholder={'https://exemple.com/AKSHGDKSAGDG'} style={{width: `${showProtocol?85:100}%` }} onChange={handlerLinkOnChange}/>
              </Grid>
              <Grid item>
                <Button variant="outlined" disabled={createLinkLoading} startIcon={createLinkLoading&&<CircularProgress color="inherit" size={16} />} onClick={handlerCreateOnClick}>Create link!</Button>
              </Grid>
            </Grid>
            {createdLink&&<React.Fragment>
              <Divider style={{ marginTop: "15px", marginBottom: "15px" }} />
              <Grid container justify="center" alignItems="center">
              <Grid style={{ marginBottom: 10 }} container justify="center" alignItems="center" spacing={2}>
                <TextField
                    id="standard-full-width"
                    label="Your short link:"
                    style={{ margin: 8, width: "80%" }}
                    placeholder="Placeholder"
                    margin="normal"
                    InputLabelProps={{
                      shrink: true,
                    }}
                    value={`${process.env.REACT_APP_APP_URL}/e/${createdLink}`}
                />
                <Tooltip title="Copy short link" aria-label="Copy short link tip">
                  <Fab
                      aria-label="Copy short link action"
                      size={"small"}
                      onClick={() => {navigator.clipboard.writeText(`${process.env.REACT_APP_APP_URL}/e/${createdLink}`)}}
                  >
                    <FileCopy />
                  </Fab>
                </Tooltip>
              </Grid>
              <Grid container justify="center" alignItems="center" spacing={2}>
              <TextField
                  id="standard-full-width"
                  label="Your statistic link:"
                  style={{ margin: 8, width: "80%" }}
                  placeholder="Placeholder"
                  fullWidth
                  margin="normal"
                  InputLabelProps={{
                    shrink: true,
                  }}
                  value={`${process.env.REACT_APP_APP_URL}/s/${createdLink}`}
              />
              <Tooltip title="Copy statistic link" aria-label="CCopy statistic link tip">
                <Fab
                    aria-label="Copy statistic link action"
                    size={"small"}
                    onClick={() => {navigator.clipboard.writeText(`${process.env.REACT_APP_APP_URL}/s/${createdLink}`)}}
                >
                  <FileCopy />
                </Fab>
              </Tooltip>
              </Grid>
            </Grid>
            </React.Fragment>}
            <Divider style={{ marginTop: "15px", marginBottom: "15px" }} />
          </div>
        </Container>
      </React.Fragment>
  );
}

export default Home;
