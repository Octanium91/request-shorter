import React, { useEffect, useState } from "react";
import { useParams } from 'react-router-dom';
import StatItem from "./StatItem";
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
                <div className={`Stat-click`}>
                    <span>{`Total clicks: ${clicksCount}`}</span>
                </div>
                <div>
                    {loading && <span>Loading ...</span>}
                    {!loading && <StatItem title={"Country"} data={countryList} />}
                    {!loading && <StatItem title={"Operating system"} data={osList} />}
                </div>
            </header>
        </div>
    )
}

export default S