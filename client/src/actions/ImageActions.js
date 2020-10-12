import { apiCallImage } from "../apiCall/index";

export const imageGetAll = () => async (dispatch) => {
    try {
      dispatch({ type: "IMAGE_LOADING" });
      const res = await apiCallImage("/", "get");
      dispatch({ type: "IMAGE_GET_DATA", payload: res.data });
      return res;
    } catch (err) {
      dispatch({ type: "IMAGE_END_LOADING" });
    }
  };
export const imageGetData = (id) => async (dispatch) => {
  try {
    dispatch({ type: "IMAGE_LOADING" });
    const res = await apiCallImage("/"+id, "get");
    dispatch({ type: "IMAGE_GET_DATA", payload: res.data });
    return res;
  } catch (err) {
    dispatch({ type: "IMAGE_END_LOADING" });
  }
};
export const imageDeleteData = (id) => async (dispatch) => {
    try {
      dispatch({ type: "IMAGE_LOADING" });
      const res = await apiCallImage("/"+id, "delete");
      dispatch({ type: "IMAGE_GET_DATA", payload: res.data });
  
      return res;
    } catch (err) {
      dispatch({ type: "IMAGE_END_LOADING" });
    }
  };
