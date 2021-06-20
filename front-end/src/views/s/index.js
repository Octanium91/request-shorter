import React, { useEffect, useState } from "react";
import { useParams } from 'react-router-dom';
import '../../App.css';
import apiService from "../../service/ApiService";

function S() {

    const code = useParams().code
    const [clicksCount, setClicksCount] = useState(0)
    const [osList, setOsList] = useState([])
    const [countryList, setCountryList] = useState([])
    const [loading, setLoading] = useState(true)

    useEffect(() => {
        apiService.shortLinkStatClickCount(code).then((req) => {
            if (req.data) {
                setClicksCount(req.data)
            }
        })
        apiService.clickStatistic(code).then((req) => {
            if (req.data) {
                setCountryList(req.data.country)
                setOsList(req.data.os)
                setLoading(false)
            }
        })
    }, [code])

    return (
        <div className="App">
            <header className="App-header">
                <div>
                    <span> Short link statistic for: </span>
                    <b>{code}</b>
                </div>
                <div>
                    <span>{`Clicks count: ${clicksCount}`}</span>
                </div>
                <div>
                    {loading && <span>Loading ...</span>}
                    {!loading && <div></div>}
                    {!loading && <div>
                        <span>Country:</span>
                        {countryList.map((it) => <div><span>{`* ${it.value}: ${it.count}`}</span></div>)}
                    </div>}
                    {!loading && <div>
                        <span>OS:</span>
                        {osList.map((it) => <div><span>{`* ${it.value}: ${it.count}`}</span></div>)}
                    </div>}
                </div>
            </header>
        </div>
    )
}

export default S;