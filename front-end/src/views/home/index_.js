import React, { useState } from "react";
import '../../App.css';
import apiService from "../../service/ApiService";

function Home() {

  const [createLinkLoading, setCreateLinkLoading] = useState(false)
  const [clientLink, setClientLink] = useState('');
  const [createdLink, setCreatedLink] = useState('')
  const [linkProtocol, setLinkProtocol] = useState(`HTTPS`)

  const handlerLinkOnChange = (e) => {
    setClientLink(e.target.value)
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
          console.log("link created", req.data)
          setCreatedLink(req.data)
          setCreateLinkLoading(false)
        }
      })
    }
  }

  return (
    <div className="App">
      <header className="App-header">
        <div className={`Shorter-content`}>
          <span>Link shorter! Fast and free!</span>
        </div>
        <div>
          <div>
            <span>Enter link and press 'Create link' for create short link!</span>
          </div>
          <div className={`Create-link-block`}>
            <select className={`Protocol`} defaultValue={linkProtocol} onChange={(event) => {setLinkProtocol(event.target.value)}} >
              <option>HTTP</option>
              <option>HTTPS</option>
            </select>
            <input defaultValue={clientLink} placeholder={`https://exemple.com/AKSHGDKSAGDG`} onChange={handlerLinkOnChange} />
            <button className={`Create-link-button`} disabled={createLinkLoading} onClick={handlerCreateOnClick}> Create link </button>
          </div>
          {createdLink&&<div className={`Link-content`}>
            <span>Wow, link created!</span>
            <div>
              <span>Your short link: </span>
              <a href={`${process.env.REACT_APP_APP_URL}/e/${createdLink}`} target="_blank" rel="noreferrer" >{`${process.env.REACT_APP_APP_URL}/e/${createdLink}`}</a>
            </div>
            <div>
              <span>Your statistic link: </span>
              <a href={`${process.env.REACT_APP_APP_URL}/s/${createdLink}`} target="_blank" rel="noreferrer" >{`${process.env.REACT_APP_APP_URL}/s/${createdLink}`}</a>
            </div>
          </div>}
        </div>
      </header>
    </div>
  );
}

export default Home;
