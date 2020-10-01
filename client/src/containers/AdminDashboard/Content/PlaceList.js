import React, { useState, useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";
import {
  placeDeleteData,
  placeGetAll,
  placePostData,
  placeUpdateData,
} from "../../../actions/PlaceActions";
import MaterialTable from "material-table";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/core/styles";
import { stylesContent } from "../styles/Styles";
import { tableIcons } from "../../tableFeatures/tableIcons";
// import Dashboard from "@material-ui/icons/Dashboard";
// import Schedule from "@material-ui/icons/Schedule";
import Warning from "@material-ui/icons/Warning";
import Check from "@material-ui/icons/Check";
import CircularProgress from "@material-ui/core/CircularProgress";
import SnackbarContent from "../../../components/Snackbar/SnackbarContent.js";

function PlaceList() {
  const dispatch = useDispatch();
  const placeData = useSelector((state) => state.place);
  const [alert, setAlert] = useState(null);
  const [alertAdd, setAlertAdd] = useState(null);
  const [alertUpdate, setAlertUpdate] = useState(null);
  const [alertDelete, setAlertDelete] = useState(null);
  const [state, setState] = useState({
    columns: [
      { title: "Id", field: "id" },
      { title: "ville", field: "ville" },
      { title: "postCode", field: "postCode", type: "numeric" },
      { title: "address", field: "address" },
      { title: "region", field: "region" },
      { title: "longitude", field: "longitude", type: "numeric" },
      { title: "latitude", field: "latitude", type: "numeric" },
    ],
    data: [],
  });
  useEffect(() => {
    dispatch(placeGetAll()).then((res) => {
      if (res) {
        setState({
          columns: state.columns,
          data: res.data,
        });
        setAlert(null);
      } else
        setAlert(
          <SnackbarContent
            message={
              <span>
                <b>WARNING ALERT:</b> Could not reach data... Refresh Page...
              </span>
            }
            close
            color="warning"
            icon={Warning}
          />
        );
    });
  }, [dispatch, state.columns]);
  const addPlace = (newData) => {
    setAlert(null);
    setAlertAdd(null);
    setAlertUpdate(null);
    setAlertDelete(null);
    if (
      !newData.ville ||
      !newData.postCode ||
      !newData.address ||
      !newData.region ||
      !newData.longitude ||
      !newData.latitude 
    ) {
      setAlertAdd(
        <SnackbarContent
          message={
            <span>
              <b>WARNING ALERT:</b> Empty field(s)...
            </span>
          }
          close
          color="warning"
          icon={Warning}
        />
      );
    } else {
      let ville = newData.ville;
      let postCode = newData.postCode;
      let address = newData.address;
      let region = newData.region;
      let longitude = newData.longitude;
      let latitude = newData.latitude;
      dispatch(
        placePostData({
          ville,
          postCode,
          address,
          region,
          longitude,
          latitude
        })
      ).then((res) => {
        if (!res)
          setAlertAdd(
            <SnackbarContent
              message={
                <span>
                  <b>WARNING ALERT:</b> Bad Request...
                </span>
              }
              close
              color="warning"
              icon={Warning}
            />
          );
        else
          setAlertAdd(
            <SnackbarContent
              message={
                <span>
                  <b>SUCCESS ALERT:</b> Place Added...
                </span>
              }
              close
              color="success"
              icon={Check}
            />
          );
      });
    }
  };
  const updatePlace = (newData, oldData) => {
    setAlert(null);
    setAlertAdd(null);
    setAlertUpdate(null);
    setAlertDelete(null);
    console.log(newData, oldData);
    if (
      !newData.ville ||
      !newData.postCode ||
      !newData.address ||
      !newData.region ||
      !newData.longitude ||
      !newData.latitude 
    ) {
      setAlertUpdate(
        <SnackbarContent
          message={
            <span>
              <b>WARNING ALERT:</b> Empty field(s)...
            </span>
          }
          close
          color="warning"
          icon={Warning}
        />
      );
    } else {
      let id = oldData.id;
      let ville = newData.ville;
      let postCode = newData.postCode;
      let address = newData.address;
      let region = newData.region;
      let longitude = newData.longitude;
      let latitude = newData.latitude;
      dispatch(
        placeUpdateData({
          id,
          ville,
          postCode,
          address,
          region,
          longitude,
          latitude
        })
      ).then((res) => {
        if (!res)
          setAlertUpdate(
            <SnackbarContent
              message={
                <span>
                  <b>WARNING ALERT:</b> Bad Request...
                </span>
              }
              close
              color="warning"
              icon={Warning}
            />
          );
        else
          setAlertUpdate(
            <SnackbarContent
              message={
                <span>
                  <b>SUCCESS ALERT:</b> Place Updated...
                </span>
              }
              close
              color="success"
              icon={Check}
            />
          );
      });
    }
  };
  const deletePlace = (oldData) => {
    dispatch(placeDeleteData(oldData.id)).then((res) => {
      setAlert(null);
      setAlertAdd(null);
      setAlertUpdate(null);
      setAlertDelete(null);
      if (res)
        setAlertDelete(
          <SnackbarContent
            message={
              <span>
                <b>SUCCESS ALERT:</b> Place Deleted...
              </span>
            }
            close
            color="success"
            icon={Check}
          />
        );
      else
        setAlertDelete(
          <SnackbarContent
            message={
              <span>
                <b>WARNING ALERT:</b> Server ERROR...
              </span>
            }
            close
            color="warning"
            icon={Warning}
          />
        );
    });
  };
  if (placeData.loading) return <CircularProgress />;
  else
    return (
      <>
        {alert}
        {alertAdd}
        {alertUpdate}
        {alertDelete}
        <MaterialTable
          title="Editable Example"
          columns={state.columns}
          icons={tableIcons}
          data={state.data}
          editable={{
            onRowAdd: (newData) =>
              new Promise((resolve) => {
                resolve();
                addPlace(newData);
              }),
            onRowUpdate: (newData, oldData) =>
              new Promise((resolve) => {
                resolve();
                updatePlace(newData, oldData);
              }),
            onRowDelete: (oldData) =>
              new Promise((resolve) => {
                resolve();
                deletePlace(oldData);
              }),
          }}
        />
      </>
    );
} 

PlaceList.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(stylesContent)(PlaceList);
