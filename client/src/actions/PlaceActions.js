import { apiCallPlace } from "../apiCall/index";

export const placeGetAll = () => async (dispatch) => {
    try {
      dispatch({ type: "PLACE_LOADING" });
      const res = await apiCallPlace("/all", "get");
      dispatch({ type: "PLACE_GET_DATA", payload: res.data });
      return res;
    } catch (err) {
      dispatch({ type: "PLACE_END_LOADING" });
    }
  };
export const placeGetData = (id) => async (dispatch) => {
  try {
    dispatch({ type: "PLACE_LOADING" });
    const res = await apiCallPlace("/"+id, "get");
    dispatch({ type: "PLACE_END_LOADING" });
    return res;
  } catch (err) {
    dispatch({ type: "PLACE_END_LOADING" });
  }
};
export const placeUpdateData = (place) => async (dispatch) => {
  try {
    dispatch({ type: "PLACE_LOADING" });
    const res = await apiCallPlace("/", "put",place);
    dispatch({ type: "PLACE_END_LOADING" });
    return res;
  } catch (err) {
    dispatch({ type: "PLACE_END_LOADING" });
  }
};
export const placePostData = (place) => async (dispatch) => {
  try {
    dispatch({ type: "PLACE_LOADING" });
    const res = await apiCallPlace("/", "post",place);
    dispatch({ type: "PLACE_END_LOADING" });

    return res;
  } catch (err) {
    dispatch({ type: "PLACE_END_LOADING" });
  }
};
export const placeDeleteData = (id) => async (dispatch) => {
    try {
      dispatch({ type: "PLACE_LOADING" });
      const res = await apiCallPlace("/"+id, "delete");
      dispatch({ type: "PLACE_END_LOADING" });
  
      return res;
    } catch (err) {
      dispatch({ type: "PLACE_END_LOADING" });
    }
  };
