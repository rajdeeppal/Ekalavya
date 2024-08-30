import axios from "axios";

const BASE_URL = "http://localhost:8080/admin"; // Update this with your actual API URL

export const getVerticals = async () => {
  try {
    const response = await axios.get(`${BASE_URL}/verticals`);
    return response.data;
  } catch (error) {
    console.error("Error fetching data:", error.message);
  }
};

export const getComponents = async (vertical) => {
  try {
    const response = await axios.get(`${BASE_URL}/components`, {
      params: { vertical },
    });
    return response.data;
  } catch (error) {
    console.error("Error fetching data:", error.message);
  }
};

export const getActivities = async (component) => {
  try {
    const response = await axios.get(`${BASE_URL}/activities`, {
      params: { component },
    });
    return response.data;
  } catch (error) {
    console.error("Error fetching data:", error.message);
  }
};

export const saveConfiguration = async (projectConfig) => {
  try {
    const response = await axios.post(
      `${BASE_URL}/configuration/save`,
      projectConfig
    );
    return response.data;
  } catch (error) {
    console.error("Error fetching data:", error.message);
  }
};

export const getTasks = async (task) => {
  try {
    const response = await axios.get(`${BASE_URL}/tasks`, { params: { task } });
    return response.data;
  } catch (error) {
    console.error("Error fetching data:", error.message);
  }
};
