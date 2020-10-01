import { apiCallCitizen } from "../apiCall/index";

export const citizenGetAll = () => async (dispatch) => {
    try {
      dispatch({ type: "CITIZEN_LOADING" });
      const res = await apiCallCitizen("/all", "get");
      dispatch({ type: "CITIZEN_GET_DATA", payload: res.data });
      return res;
    } catch (err) {
      dispatch({ type: "CITIZEN_END_LOADING" });
    }
  };
export const citizenGetData = (id) => async (dispatch) => {
  try {
    dispatch({ type: "CITIZEN_LOADING" });
    const res = await apiCallCitizen("/"+id, "get");
    dispatch({ type: "CITIZEN_GET_DATA", payload: res.data });
    return res;
  } catch (err) {
    dispatch({ type: "CITIZEN_END_LOADING" });
  }
};
export const citizenUpdateData = (citizen) => async (dispatch) => {
  try {
    dispatch({ type: "CITIZEN_LOADING" });
    const res = await apiCallCitizen("/", "put",citizen);
    dispatch({ type: "CITIZEN_GET_DATA", payload: res.data });
    return res;
  } catch (err) {
    dispatch({ type: "CITIZEN_END_LOADING" });
  }
};
export const citizenPostData = (citizen) => async (dispatch) => {
  try {
    dispatch({ type: "CITIZEN_LOADING" });
    const res = await apiCallCitizen("/", "post",citizen);
    dispatch({ type: "CITIZEN_GET_DATA", payload: res.data });

    return res;
  } catch (err) {
    dispatch({ type: "CITIZEN_END_LOADING" });
  }
};
export const citizenDeleteData = (id) => async (dispatch) => {
    try {
      dispatch({ type: "CITIZEN_LOADING" });
      const res = await apiCallCitizen("/"+id, "delete");
      dispatch({ type: "CITIZEN_GET_DATA", payload: res.data });
  
      return res;
    } catch (err) {
      dispatch({ type: "CITIZEN_END_LOADING" });
    }
  };
