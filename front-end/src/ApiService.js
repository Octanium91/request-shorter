import axios from 'axios';

const apiService = {}

apiService.createShortLink = (type, link) => {
    return axios.put('/api/shortContent/create', { type, link })
}

export default apiService