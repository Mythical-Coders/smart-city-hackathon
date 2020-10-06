import axios from "axios";

const composeToken = (token) =>
  token ? { Authorization: `Bearer ${token}` } : {};

const apiCallAuth = (url, method, body = {}, token = "") =>
  axios({
    method,
    url: `https://cheng-app-auth.herokuapp.com/api${url}`,
    data: body,
    headers: {
      ...composeToken(token),
    },
  });

const apiCallImpound = (url, method, body = {}, token = "") =>
  axios({
    method,
    url: `https://cheng-app-impound-data.herokuapp.com/api/impound${url}`,
    data: body,
    headers: {
      ...composeToken(token),
    },
  }); 
const apiCallPlace = (url, method, body = {}, token = "") =>
  axios({
    method,
    url: `https://cheng-app-place-data.herokuapp.com/api/place${url}`,
    data: body,
    headers: {
      ...composeToken(token),
    },
  });
const apiCallCitizen = (url, method, body = {}, token = "") =>
  axios({
    method,
    url: `https://cheng-app-citizen-data.herokuapp.com/api/citizen${url}`,
    data: body,
    headers: {
      ...composeToken(token),
    },
  });
  const apiCallProfile = (url, method, body = {}, token = "") =>
  axios({
    method,
    url: `http://localhost:8086/api/profile${url}`,
    data: body,
    headers: {
      ...composeToken(token),
    },
  });
export { apiCallAuth, apiCallImpound, apiCallPlace, apiCallCitizen, apiCallProfile };
