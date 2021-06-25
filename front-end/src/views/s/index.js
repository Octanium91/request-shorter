import React, { useEffect, useState } from "react";
import { useParams } from 'react-router-dom';
import { Card, CardContent, CardHeader, CircularProgress, Container } from '@material-ui/core';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import ChevronRightIcon from '@material-ui/icons/ChevronRight';
import { TreeView, TreeItem } from '@material-ui/lab';
import apiService from "../../service/ApiService";
import Header from "../../components/header";

function S() {

    const code = useParams().code
    const [clicksCount, setClicksCount] = useState(0)
    // const [osList, setOsList] = useState([])
    const [countryObj, setCountryObj] = useState({})
    const [cityObj, setCityObj] = useState({})
    const [countryList, setCountyList] = useState([])
    // const [loading, setLoading] = useState(true)

    useEffect(() => {
        if (countryObj && cityObj) {
            setCountyList(createCountryTree(countryObj, cityObj))
        }
    }, // eslint-disable-next-line
        [ countryObj, cityObj ])

    const createCityTree = (index, country, cityObj) => {
        const treeList = []
        if (cityObj.hasOwnProperty(country)) {
            for (const [key, value] of Object.entries(cityObj[country])) {
                index = index + 1
                treeList.push(<TreeItem key={index} nodeId={index.toString()} label={`${key}: ${value}`} />)
            }
        }
        return treeList
    }

    const createCountryTree = (countryObj, cityObj) => {
        const treeList = []
        let index = 0
        for (const [key, value] of Object.entries(countryObj)) {
            index = index + 1
            treeList.push(<TreeItem key={index} nodeId={index.toString()} label={`${key}: ${value}`}>
                {createCityTree(index, key, cityObj)}
            </TreeItem>)
        }
        return treeList
    }

    useEffect(() => {
        apiService.shortLinkStatClickCount(code).then((req) => {
            if (req.data) {
                setClicksCount(req.data)
            }
        })
        apiService.clickStatistic(code).then((req) => {
            if (req.data) {
                setCountryObj(req.data.country)
                setCityObj(req.data.city)
                // setOsList(req.data.os)
                // setLoading(false)
            }
        })
    }, [code])

    return (
        <React.Fragment>
            <Header />
            <Container maxWidth="sm">
                <div>
                    <span> Short link statistic for: </span>
                    <b>{code}</b>
                </div>
                <div className={`Stat-click`}>
                    <span>{`Total clicks: ${clicksCount}`}</span>
                </div>
                <Card>
                    <CardHeader title="Country and city"/>
                    <CardContent>
                        {countryList.length > 0?<TreeView
                            defaultCollapseIcon={<ExpandMoreIcon />}
                            defaultExpandIcon={<ChevronRightIcon />}
                            multiSelect
                        >
                            {countryList}
                        </TreeView>:<CircularProgress />}
                    </CardContent>
                </Card>
                {/*<div>*/}
                {/*    {loading && <span>Loading ...</span>}*/}
                {/*    {!loading && <StatItem title={"Country"} data={countryList} />}*/}
                {/*    {!loading && <StatItem title={"Operating system"} data={osList} />}*/}
                {/*</div>*/}
            </Container>
        </React.Fragment>
    )
}

export default S