import { apiCallNotification } from "../apiCall/index";

export const notificationGetAll = () => async (dispatch) => {
  try {
    dispatch({ type: "NOTIFICATION_LOADING" });
    const res = await apiCallNotification("/all", "get");
    dispatch({ type: "NOTIFICATION_GET_DATA", payload: res.data });
    return res;
  } catch (err) {
    dispatch({ type: "NOTIFICATION_END_LOADING" });
  }
};
export const notificationGetDataReceiver = (idReceiver) => async (dispatch) => {
  try {
    const res = await apiCallNotification("/receiver/" + idReceiver, "get");
    dispatch({ type: "NOTIFICATION_GET_DATA", payload: res.data });
    return res;
  } catch (err) {
    return err;
  }
};
export const notificationUpdateData = (notification) => async (dispatch) => {
  try {
    dispatch({ type: "NOTIFICATION_LOADING" });
    const res = await apiCallNotification("/", "put", notification);
    dispatch({ type: "NOTIFICATION_GET_DATA", payload: res.data });
    return res;
  } catch (err) {
    dispatch({ type: "NOTIFICATION_END_LOADING" });
  }
};
export const notificationPostData = (notification) => async (dispatch) => {
  try {
    dispatch({ type: "NOTIFICATION_LOADING" });
    const res = await apiCallNotification("/", "post", notification);
    dispatch({ type: "NOTIFICATION_GET_DATA", payload: res.data });

    return res;
  } catch (err) {
    dispatch({ type: "NOTIFICATION_END_LOADING" });
  }
};
export const notificationDeleteData = (id) => async (dispatch) => {
  try {
    dispatch({ type: "NOTIFICATION_LOADING" });
    const res = await apiCallNotification("/" + id, "delete");
    dispatch({ type: "NOTIFICATION_GET_DATA", payload: res.data });

    return res;
  } catch (err) {
    dispatch({ type: "NOTIFICATION_END_LOADING" });
  }
};
