import { apiCallReportPlace } from "../apiCall/index";

export const reportPlaceGetAll = () => async (dispatch) => {
    try {
      dispatch({ type: "REPORT_PLACE_LOADING" });
      const res = await apiCallReportPlace("/all", "get");
      dispatch({ type: "REPORT_PLACE_GET_DATA", payload: res.data });
      return res;
    } catch (err) {
      dispatch({ type: "REPORT_PLACE_END_LOADING" });
    }
  };
export const reportPlaceGetData = (id) => async (dispatch) => {
  try {
    dispatch({ type: "REPORT_PLACE_LOADING" });
    const res = await apiCallReportPlace("/"+id, "get");
    dispatch({ type: "REPORT_PLACE_GET_DATA", payload: res.data });
    return res;
  } catch (err) {
    dispatch({ type: "REPORT_PLACE_END_LOADING" });
  }
};
export const reportPlaceUpdateData = (reportPlace) => async (dispatch) => {
  try {
    dispatch({ type: "REPORT_PLACE_LOADING" });
    const res = await apiCallReportPlace("/", "put",reportPlace);
    dispatch({ type: "REPORT_PLACE_GET_DATA", payload: res.data });
    return res;
  } catch (err) {
    dispatch({ type: "REPORT_PLACE_END_LOADING" });
  }
};
export const reportPlacePostData = (reportPlace) => async (dispatch) => {
  try {
    dispatch({ type: "REPORT_PLACE_LOADING" });
    const res = await apiCallReportPlace("/", "post",reportPlace);
    dispatch({ type: "REPORT_PLACE_GET_DATA", payload: res.data });

    return res;
  } catch (err) {
    dispatch({ type: "REPORT_PLACE_END_LOADING" });
  }
};
export const reportPlaceDeleteData = (id) => async (dispatch) => {
    try {
      dispatch({ type: "REPORT_PLACE_LOADING" });
      const res = await apiCallReportPlace("/"+id, "delete");
      dispatch({ type: "REPORT_PLACE_GET_DATA", payload: res.data });
  
      return res;
    } catch (err) {
      dispatch({ type: "REPORT_PLACE_END_LOADING" });
    }
  };
