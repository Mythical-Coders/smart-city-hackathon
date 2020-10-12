import { apiCallReport } from "../apiCall/index";

export const reportGetAll = () => async (dispatch) => {
    try {
      dispatch({ type: "REPORT_LOADING" });
      const res = await apiCallReport("/all", "get");
      dispatch({ type: "REPORT_GET_DATA", payload: res.data });
      return res;
    } catch (err) {
      dispatch({ type: "REPORT_END_LOADING" });
    }
  };
export const reportGetData = (id) => async (dispatch) => {
  try {
    dispatch({ type: "REPORT_LOADING" });
    const res = await apiCallReport("/"+id, "get");
    dispatch({ type: "REPORT_GET_DATA", payload: res.data });
    return res;
  } catch (err) {
    dispatch({ type: "REPORT_END_LOADING" });
  }
};
export const reportUpdateData = (report) => async (dispatch) => {
  try {
    dispatch({ type: "REPORT_LOADING" });
    const res = await apiCallReport("/", "put",report);
    dispatch({ type: "REPORT_GET_DATA", payload: res.data });
    return res;
  } catch (err) {
    dispatch({ type: "REPORT_END_LOADING" });
  }
};
export const reportPostData = (report) => async (dispatch) => {
  try {
    dispatch({ type: "REPORT_LOADING" });
    const res = await apiCallReport("/", "post",report);
    dispatch({ type: "REPORT_GET_DATA", payload: res.data });

    return res;
  } catch (err) {
    dispatch({ type: "REPORT_END_LOADING" });
  }
};
export const reportDeleteData = (id) => async (dispatch) => {
    try {
      dispatch({ type: "REPORT_LOADING" });
      const res = await apiCallReport("/"+id, "delete");
      dispatch({ type: "REPORT_GET_DATA", payload: res.data });
  
      return res;
    } catch (err) {
      dispatch({ type: "REPORT_END_LOADING" });
    }
  };
