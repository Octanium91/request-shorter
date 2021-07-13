import React, {useEffect, useState, useRef} from "react";
import {useParams} from 'react-router-dom';
import {
    Button,
    Card,
    CardContent,
    CardHeader,
    CircularProgress,
    Container,
    Tooltip,
    Typography
} from '@material-ui/core';
import { Alert, AlertTitle } from '@material-ui/lab';
import {findFlagUrlByIso2Code} from "country-flags-svg";
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import ChevronRightIcon from '@material-ui/icons/ChevronRight';
import {TreeView, TreeItem} from '@material-ui/lab';
import apiService from "../../service/ApiService";
import Header from "../../components/header";
import Footer from "../../components/footer";

function S(props) {

    const icons = {
        unknown: { name: "Unknown", icon: "unknown.svg" },
        unix: { name: "Unix", icon: "linux.svg" },
        pc: { name: "PC", icon: "pc.svg" },
        mac: { name: "Mac", icon: "pc.svg" },
        mobile: { name: "Mobile", icon: "mobile.svg" },
        iPhone: { name: "iPhone", icon: "iphone.svg" },
        iPad: { name: "iPad", icon: "ipad.svg" },
        android: { name: "Android", icon: "android.svg" },
        ios: { name: "IOS", icon: "ios.svg" },
        telegram: { name: "Telegram", icon: "telegram.svg" },
        chrome: { name: "Google Chrome", icon: "chrome.svg" },
        firefox: { name: "Firefox", icon: "firefox.svg" },
        ie: { name: "IE", icon: "ie.svg" },
        opera: { name: "Opera", icon: "opera.svg" },
        windows: { name: "Windows", icon: "windows.svg" }
    }

    const code = useParams().code
    const refIntervalId = useRef(0);
    const [serviceUpdating, setServiceUpdating] = useState(false)
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

    const setLabel = (label, value) => {
        if (icons.hasOwnProperty(label)) {
            return <>{<img height={20} src={`/assets/img/s/icons/${icons[label].icon}`}
                           alt={`${icons[label].name} - ${value}`}/>}<span>{` ${icons[label].name}: ${value}`}</span></>
        } else {
            return `${label}: ${value}`
        }
    }

    useEffect(() => {
            const treeList = []
            let index = 0
            for (const [key, value] of Object.entries(osObj)) {
                index = index + 1
                treeList.push(<TreeItem key={index} nodeId={index.toString()} label={setLabel(key, value)}/>)
            }
            setOsList(treeList)
            setOsLoad(false)
        }, // eslint-disable-next-line
        [osObj])

    useEffect(() => {
            const treeList = []
            let index = 0
            for (const [key, value] of Object.entries(mobileObj)) {
                index = index + 1
                treeList.push(<TreeItem key={index} nodeId={index.toString()} label={setLabel(key, value)}/>)
            }
            setMobileList(treeList)
            setMobileLoad(false)
        }, // eslint-disable-next-line
        [mobileObj])

    useEffect(() => {
            const treeList = []
            let index = 0
            for (const [key, value] of Object.entries(deviceObj)) {
                index = index + 1
                treeList.push(<TreeItem key={index} nodeId={index.toString()} label={setLabel(key, value)}/>)
            }
            setDeviceList(treeList)
            setDeviceLoad(false)
        }, // eslint-disable-next-line
        [deviceObj])

    useEffect(() => {
            const treeList = []
            let index = 0
            for (const [key, value] of Object.entries(browserObj)) {
                index = index + 1
                treeList.push(<TreeItem key={index} nodeId={index.toString()} label={setLabel(key, value)}/>)
            }
            setBrowserList(treeList)
            setBrowserLoad(false)
        }, // eslint-disable-next-line
        [browserObj])

    const createCityTree = (index, country, cityObj) => {
        const treeList = []
        if (cityObj.hasOwnProperty(country)) {
            for (const [key, value] of Object.entries(cityObj[country])) {
                index = index + 1
                treeList.push(<TreeItem key={index} nodeId={index.toString()} label={(icons.hasOwnProperty(key))?`${icons.hasOwnProperty(key).name}: ${value}`:`${key}: ${value}`}/>)
            }
        }
        return treeList
    }

    const createCountryTree = (countryObj, cityObj) => {
        const treeList = []
        let index = 0
        for (const [key, value] of Object.entries(countryObj)) {
            const flagUrl = findFlagUrlByIso2Code(key)
            index = index + 1
            treeList.push(<TreeItem key={index} nodeId={index.toString()} label={<>{flagUrl &&
            <img height={16} src={flagUrl} alt={`${key}`}/>}<span>{(icons.hasOwnProperty(key))?` ${icons.hasOwnProperty(key).name}: ${value}`:` ${key}: ${value}`}</span></>}>
                {createCityTree(index, key, cityObj)}
            </TreeItem>)
        }
        return treeList
    }

    useEffect(() => {
            setCountyList(createCountryTree(countryObj, cityObj))
            setCountyLoad(false)
        }, // eslint-disable-next-line
        [countryObj, cityObj])

    const getStatistic = () => {
        apiService.shortLinkStatClickCount(code).then((req) => {
            if (req.data) {
                setServiceUpdating(false)
                setClicksCount(req.data)
            } else {
                setServiceUpdating(true)
            }
        }).catch((error) => {
            if (error.response.status === 404) {
                props.history.push(`/404/${code}`)
            } else {
                setServiceUpdating(true)
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
        }).catch(() => {
        })
    }

    useEffect(() => {
            getStatistic()
            clearInterval(refIntervalId.current)
            refIntervalId.current = setInterval(getStatistic, 3000);
            return () => {
                clearInterval(refIntervalId.current);
            };
        }, // eslint-disable-next-line
        [code])

    return (
        <React.Fragment>
            <Header/>
            <Container maxWidth="sm">
                {serviceUpdating&&<Alert key={`service_update_alert`} severity="warning">
                    <AlertTitle>Service is updating</AlertTitle>
                    Please wait a bit ...
                </Alert>}
                <div>
                    <Typography variant="h4" component="h3">Statistic for: <b>{code}</b></Typography>
                </div>
                <div className={`Stat-click`}>
                    <Typography variant="h4" component="h3">Total clicks: <b>{clicksCount}</b></Typography>
                </div>
                <Card style={{margin: "4px"}}>
                    <CardHeader title="Country and city"/>
                    <CardContent>
                        {!countryLoad ? <TreeView
                            defaultCollapseIcon={<ExpandMoreIcon/>}
                            defaultExpandIcon={<ChevronRightIcon/>}
                            multiSelect
                        >
                            {countryList}
                        </TreeView> : <CircularProgress/>}
                    </CardContent>
                </Card>
                <Card style={{margin: "4px"}}>
                    <CardHeader title="Operating system"/>
                    <CardContent>
                        {!osLoad ? <TreeView
                            defaultCollapseIcon={<ExpandMoreIcon/>}
                            defaultExpandIcon={<ChevronRightIcon/>}
                            multiSelect
                        >
                            {osList}
                        </TreeView> : <CircularProgress/>}
                    </CardContent>
                </Card>
                <Card style={{margin: "4px"}}>
                    <CardHeader title="Device type"/>
                    <CardContent>
                        {!mobileLoad ? <TreeView
                            defaultCollapseIcon={<ExpandMoreIcon/>}
                            defaultExpandIcon={<ChevronRightIcon/>}
                            multiSelect
                        >
                            {mobileList}
                        </TreeView> : <CircularProgress/>}
                    </CardContent>
                </Card>
                <Card style={{margin: "4px"}}>
                    <CardHeader title="Device"/>
                    <CardContent>
                        {!deviceLoad ? <TreeView
                            defaultCollapseIcon={<ExpandMoreIcon/>}
                            defaultExpandIcon={<ChevronRightIcon/>}
                            multiSelect
                        >
                            {deviceList}
                        </TreeView> : <CircularProgress/>}
                    </CardContent>
                </Card>
                <Card style={{margin: "4px"}}>
                    <CardHeader title="Browser app"/>
                    <CardContent>
                        {!browserLoad ? <TreeView
                            defaultCollapseIcon={<ExpandMoreIcon/>}
                            defaultExpandIcon={<ChevronRightIcon/>}
                            multiSelect
                        >
                            {browserList}
                        </TreeView> : <CircularProgress/>}
                    </CardContent>
                </Card>
                <div style={{marginTop: "20px"}} className={"d-flex justify-content-center"}>
                    <Tooltip title="Go to home page" aria-label="Go to home page">
                        <Button variant="contained" color="primary" onClick={() => {
                            props.history.push("/")
                        }}>
                            Go on home page
                        </Button>
                    </Tooltip>
                </div>
                <Footer/>
            </Container>
        </React.Fragment>
    )
}

export default S