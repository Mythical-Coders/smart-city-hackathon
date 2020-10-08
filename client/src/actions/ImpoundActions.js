import { apiCallImpound } from "../apiCall/index";

export const impoundGetAll = () => async (dispatch) => {
    try {
      dispatch({ type: "IMPOUND_LOADING" });
      const res = await apiCallImpound("/all", "get");
      dispatch({ type: "IMPOUND_GET_DATA", payload: res.data });
      return res;
    } catch (err) {
      dispatch({ type: "IMPOUND_END_LOADING" });
    }
  };
export const impoundGetData = (id) => async (dispatch) => {
  try {
    dispatch({ type: "IMPOUND_LOADING" });
    const res = await apiCallImpound("/"+id, "get");
    dispatch({ type: "IMPOUND_GET_DATA", payload: res.data });
    return res;
  } catch (err) {
    dispatch({ type: "IMPOUND_END_LOADING" });
  }
};
export const impoundUpdateData = (impound) => async (dispatch) => {
  try {
    dispatch({ type: "IMPOUND_LOADING" });
    const res = await apiCallImpound("/", "put",impound);
    dispatch({ type: "IMPOUND_GET_DATA", payload: res.data });
    return res;
  } catch (err) {
    dispatch({ type: "IMPOUND_END_LOADING" });
  }
};
export const impoundPostData = (impound) => async (dispatch) => {
  try {
    dispatch({ type: "IMPOUND_LOADING" });
    const res = await apiCallImpound("/", "post",impound);
    dispatch({ type: "IMPOUND_GET_DATA", payload: res.data });

    return res;
  } catch (err) {
    dispatch({ type: "IMPOUND_END_LOADING" });
  }
};
export const impoundDeleteData = (id) => async (dispatch) => {
    try {
      dispatch({ type: "IMPOUND_LOADING" });
      const res = await apiCallImpound("/"+id, "delete");
      dispatch({ type: "IMPOUND_GET_DATA", payload: res.data });
  
      return res;
    } catch (err) {
      dispatch({ type: "IMPOUND_END_LOADING" });
    }
  };
  export const impoundPaid = (impound) => async (dispatch) => {
    try {
      dispatch({ type: "IMPOUND_LOADING" });
      const res = await apiCallImpound("/paid", "put",impound);
      dispatch({ type: "IMPOUND_END_LOADING" });
  
      return res;
    } catch (err) {
      dispatch({ type: "IMPOUND_END_LOADING" });
    }
  };
  export const impoundRelease = (impound) => async (dispatch) => {
    try {
      dispatch({ type: "IMPOUND_LOADING" });
      const res = await apiCallImpound("/release", "put",impound);
      dispatch({ type: "IMPOUND_END_LOADING" });
  
      return res;
    } catch (err) {
      dispatch({ type: "IMPOUND_END_LOADING" });
    }
  };
