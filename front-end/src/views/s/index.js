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
    const [osList, setOsList] = useState([])
    const [osObj, setOsObj] = useState({})
    const [osLoad, setOsLoad] = useState(true)
    const [deviceList, setDeviceList] = useState([])
    const [deviceObj, setDeviceObj] = useState({})
    const [deviceLoad, setDeviceLoad] = useState(true)
    const [mobileList, setMobileList] = useState([])
    const [mobileObj, setMobileObj] = useState({})
    const [mobileLoad, setMobileLoad] = useState(true)
    const [browserList, setBrowserList] = useState([])
    const [browserObj, setBrowserObj] = useState({})
    const [browserLoad, setBrowserLoad] = useState(true)
    const [countryObj, setCountryObj] = useState({})
    const [cityObj, setCityObj] = useState({})
    const [countryList, setCountyList] = useState([])
    const [countryLoad, setCountyLoad] = useState(true)
    // const [loading, setLoading] = useState(true)

    useEffect(() => {
            const treeList = []
            let index = 0
            for (const [key, value] of Object.entries(osObj)) {
                index = index + 1
                treeList.push(<TreeItem key={index} nodeId={index.toString()} label={`${key}: ${value}`}/>)
            }
            setOsList(treeList)
            setOsLoad(false)
        }, // eslint-disable-next-line
        [ osObj ])

    useEffect(() => {
            const treeList = []
            let index = 0
            for (const [key, value] of Object.entries(mobileObj)) {
                index = index + 1
                treeList.push(<TreeItem key={index} nodeId={index.toString()} label={`${key}: ${value}`}/>)
            }
            setMobileList(treeList)
            setMobileLoad(false)
        }, // eslint-disable-next-line
        [ mobileObj ])

    useEffect(() => {
            const treeList = []
            let index = 0
            for (const [key, value] of Object.entries(deviceObj)) {
                index = index + 1
                treeList.push(<TreeItem key={index} nodeId={index.toString()} label={`${key}: ${value}`}/>)
            }
            setDeviceList(treeList)
            setDeviceLoad(false)
        }, // eslint-disable-next-line
        [ deviceObj ])

    useEffect(() => {
            const treeList = []
            let index = 0
            for (const [key, value] of Object.entries(browserObj)) {
                index = index + 1
                treeList.push(<TreeItem key={index} nodeId={index.toString()} label={`${key}: ${value}`}/>)
            }
            setBrowserList(treeList)
            setBrowserLoad(false)
        }, // eslint-disable-next-line
        [ browserObj ])

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
            setCountyList(createCountryTree(countryObj, cityObj))
            setCountyLoad(false)
        }, // eslint-disable-next-line
        [ countryObj, cityObj ])

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
                setOsObj(req.data.os)
                setMobileObj(req.data.mobile)
                setBrowserObj(req.data.browser)
                setDeviceObj(req.data.device)
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
                        {!countryLoad?<TreeView
                            defaultCollapseIcon={<ExpandMoreIcon />}
                            defaultExpandIcon={<ChevronRightIcon />}
                            multiSelect
                        >
                            {countryList}
                        </TreeView>:<CircularProgress />}
                    </CardContent>
                </Card>
                <Card>
                    <CardHeader title="Operating system"/>
                    <CardContent>
                        {!osLoad?<TreeView
                            defaultCollapseIcon={<ExpandMoreIcon />}
                            defaultExpandIcon={<ChevronRightIcon />}
                            multiSelect
                        >
                            {osList}
                        </TreeView>:<CircularProgress />}
                    </CardContent>
                </Card>
                <Card>
                    <CardHeader title="Device type"/>
                    <CardContent>
                        {!mobileLoad?<TreeView
                            defaultCollapseIcon={<ExpandMoreIcon />}
                            defaultExpandIcon={<ChevronRightIcon />}
                            multiSelect
                        >
                            {mobileList}
                        </TreeView>:<CircularProgress />}
                    </CardContent>
                </Card>
                <Card>
                    <CardHeader title="Device"/>
                    <CardContent>
                        {!deviceLoad?<TreeView
                            defaultCollapseIcon={<ExpandMoreIcon />}
                            defaultExpandIcon={<ChevronRightIcon />}
                            multiSelect
                        >
                            {deviceList}
                        </TreeView>:<CircularProgress />}
                    </CardContent>
                </Card>
                <Card>
                    <CardHeader title="Browser app"/>
                    <CardContent>
                        {!browserLoad?<TreeView
                            defaultCollapseIcon={<ExpandMoreIcon />}
                            defaultExpandIcon={<ChevronRightIcon />}
                            multiSelect
                        >
                            {browserList}
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