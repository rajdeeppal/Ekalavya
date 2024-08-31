import axios from 'axios';

const BASE_URL = 'http://localhost:8080/admin'; // Update with your actual API URL

// Fetch all verticals
export const getVerticals = async () => {
    try {
        const response = await axios.get(`${BASE_URL}/verticals`);
        return response.data;
    } catch (error) {
        console.error("Error fetching verticals:", error);
        return [];
    }
};

// Fetch components based on selected vertical
export const getComponents = async (vertical) => {
    try {
        const response = await axios.get(`${BASE_URL}/components`, { params: { vertical } });
        return response.data;
    } catch (error) {
        console.error("Error fetching components:", error);
        return [];
    }
};

// Fetch activities based on selected component
export const getActivities = async (component) => {
    try {
        const response = await axios.get(`${BASE_URL}/activities`, { params: { component } });
        return response.data;
    } catch (error) {
        console.error("Error fetching activities:", error);
        return [];
    }
};

// Fetch tasks for editing
export const getTasks = async (activity) => {
    try {
        const response = await axios.get(`${BASE_URL}/tasks`, { params: { activity } });
        return response.data;
    } catch (error) {
        console.error("Error fetching tasks:", error);
        return [];
    }
};

// Fetch tasks by its ID for editing
export const getTaskById = async (taskId) => {
    try {
        const response = await axios.get(`${BASE_URL}/tasks/${taskId}`);
        return response.data;
    } catch (error) {
        console.error('Error fetching task by ID:', error);
        throw error;
    }
};

// Save the configuration
export const saveConfiguration = async (projectConfig) => {
    try {
        const response = await axios.post(`${BASE_URL}/configuration/save`, projectConfig);
        return response.data;
    } catch (error) {
        console.error("Error saving configuration:", error);
    }
};

// Update an existing task
export const updateTask = async (taskId, updatedTask) => {
    try {
        await axios.put(`${BASE_URL}/tasks/${taskId}`, updatedTask, {
            headers: {
                'Content-Type': 'application/json',
            },
        });
    } catch (error) {
        console.error("Error updating task:", error);
    }
};
