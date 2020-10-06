import { apiCallProfile } from "../apiCall/index";

export const profileGetAll = () => async (dispatch) => {
    try {
      dispatch({ type: "PROFILE_LOADING" });
      const res = await apiCallProfile("/all", "get");
      dispatch({ type: "PROFILE_GET_DATA", payload: res.data });
      return res;
    } catch (err) {
      dispatch({ type: "PROFILE_END_LOADING" });
    }
  };
export const profileGetData = (id) => async (dispatch) => {
  try {
    dispatch({ type: "PROFILE_LOADING" });
    const res = await apiCallProfile("/"+id, "get");
    dispatch({ type: "PROFILE_END_LOADING" });
    return res;
  } catch (err) {
    dispatch({ type: "PROFILE_END_LOADING" });
  }
};
export const profileUpdateData = (profile) => async (dispatch) => {
  try {
    dispatch({ type: "PROFILE_LOADING" });
    const res = await apiCallProfile("/", "put",profile);
    dispatch({ type: "PROFILE_END_LOADING" });
    return res;
  } catch (err) {
    dispatch({ type: "PROFILE_END_LOADING" });
  }
};
export const profilePostData = (profile) => async (dispatch) => {
  try {
    dispatch({ type: "PROFILE_LOADING" });
    const res = await apiCallProfile("/", "post",profile);
    dispatch({ type: "PROFILE_END_LOADING" });

    return res;
  } catch (err) {
    dispatch({ type: "PROFILE_END_LOADING" });
  }
};
export const profileDeleteData = (id) => async (dispatch) => {
    try {
      dispatch({ type: "PROFILE_LOADING" });
      const res = await apiCallProfile("/"+id, "delete");
      dispatch({ type: "PROFILE_END_LOADING" });
  
      return res;
    } catch (err) {
      dispatch({ type: "PROFILE_END_LOADING" });
    }
  };
