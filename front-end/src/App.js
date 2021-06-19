import React, { useState, useEffect } from "react";
// import logo from './logo.svg';
import './App.css';
import apiService from "./ApiService";

function App() {

  const [createLinkLoading, setCreateLinkLoading] = useState(false)
  const [clientLink, setClientLink] = useState('');
  const [createdLink, setCreatedLink] = useState('')

  const handlerLinkOnChange = (e) => {
    setClientLink(e.target.value)
  }

  const handlerCreateOnClick = () => {
    if (clientLink) {
      setCreateLinkLoading(true)
      apiService.createShortLink("LINK", clientLink).then((req) => {
        if (req.data) {
          console.log("link created", req.data)
          setCreatedLink(req.data)
          setCreateLinkLoading(false)
        }
      })
    }
  }

  useEffect(() => {
    console.log("Loading, ok")
  }, [])

  return (
    <div className="App">
      <header className="App-header">
        <div>
          <div>
            <input defaultValue={clientLink} onChange={handlerLinkOnChange} />
            <button disabled={createLinkLoading} onClick={handlerCreateOnClick}> Create link! </button>
          </div>
          {createdLink&&<div>
            <a href={`http://localhost:8667/e/${createdLink}`} target="_blank" rel="noreferrer" >{`http://localhost:8667/e/${createdLink}`}</a>
          </div>}
        </div>
      </header>
    </div>
  );
}

export default App;
