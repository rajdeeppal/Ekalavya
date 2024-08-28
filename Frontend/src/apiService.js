import axios from 'axios';

const BASE_URL = 'http://localhost:8080/admin'; // Update this with your actual API URL

export const getVerticals = async () => {
    const response = await axios.get(`${BASE_URL}/verticals`);
    return response.data;
};

export const getComponents = async (vertical) => {
    const response = await axios.get(`${BASE_URL}/components`, { params: { vertical } });
    return response.data;
};

export const getActivities = async (component) => {
    const response = await axios.get(`${BASE_URL}/activities`, { params: { component } });
    return response.data;
};

export const saveConfiguration = async (projectConfig) => {
    const response = await axios.post(`${BASE_URL}/configuration/save`, projectConfig);
    return response.data;
};
