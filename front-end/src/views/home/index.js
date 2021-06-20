import React, { useState, useEffect } from "react";
import '../../App.css';
import apiService from "../../service/ApiService";

function Home() {

  const [createLinkLoading, setCreateLinkLoading] = useState(false)
  const [clientLink, setClientLink] = useState('');
  const [createdLink, setCreatedLink] = useState('')
  const [linkProtocol, setLinkProtocol] = useState(`https`)

  const handlerLinkOnChange = (e) => {
    setClientLink(e.target.value)
  }

  const handlerCreateOnClick = () => {
    if (clientLink) {
      let httpPrefix = ``
      if (clientLink.indexOf(`http`) === -1) {
        httpPrefix = `${linkProtocol}://`
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

  useEffect(() => {
    console.log("Loading, ok")
  }, [])

  return (
    <div className="App">
      <header className="App-header">
        <div>
          <span>Link shorter! Fast and free!</span>
        </div>
        <div>
          <span>Enter link and press 'Create link' for create short link!</span>
          <div>
            <select defaultValue={linkProtocol} onChange={(event) => {setLinkProtocol(event.target.value)}} >
              <option>http</option>
              <option>https</option>
            </select>
            <input defaultValue={clientLink} onChange={handlerLinkOnChange} />
            <button disabled={createLinkLoading} onClick={handlerCreateOnClick}> Create link </button>
          </div>
          {createdLink&&<div>
            <div>
              <span>Your short link:</span>
              <a href={`${process.env.REACT_APP_APP_URL}/e/${createdLink}`} target="_blank" rel="noreferrer" >{`${process.env.REACT_APP_APP_URL}/e/${createdLink}`}</a>
            </div>
            <div>
              <span>Your statistic link:</span>
              <a href={`${process.env.REACT_APP_APP_URL}/s/${createdLink}`} target="_blank" rel="noreferrer" >{`${process.env.REACT_APP_APP_URL}/s/${createdLink}`}</a>
            </div>
          </div>}
        </div>
      </header>
    </div>
  );
}

export default Home;
