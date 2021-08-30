import axios from 'axios';

const apiService = {}

apiService.createShortLink = (type, link) => {
    return axios.put('/api/shortContent/create', { type, link })
}

apiService.shortLinkStatClickCount = (code) => {
    return axios.get('/api/shortContent/statistic/count', { params: { code } })
}

apiService.clickStatistic = (code) => {
    return axios.get(`/api/click/statistic`, { params: { code } })
}

apiService.serviceStatistic = () => {
    return axios.get(`/api/service/statistic`)
}

export default apiService