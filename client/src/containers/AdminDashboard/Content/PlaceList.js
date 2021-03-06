import React, { useState, useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";
import {
  placeDeleteData,
  placeGetAll,
  placeUpdateData,
} from "../../../actions/PlaceActions";
import MaterialTable from "material-table";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/core/styles";
import { stylesContent } from "../styles/Styles";
import { tableIcons } from "../../tableFeatures/tableIcons";
import Dashboard from "@material-ui/icons/Dashboard";
import Schedule from "@material-ui/icons/Schedule";
import Warning from "@material-ui/icons/Warning";
import Check from "@material-ui/icons/Check";
import CircularProgress from "@material-ui/core/CircularProgress";
import SnackbarContent from "../../../components/Snackbar/SnackbarContent.js";
import localization from "../../tableFeatures/localization";
import NavPills from "../../../components/NavPills/NavPills.js";
import GoogleMapFct from "../../googleMap/GoogleMapFct";

function PlaceList() {
  const dispatch = useDispatch();
  const placeData = useSelector((state) => state.place);
  const [alert, setAlert] = useState(null);
  const [alertAdd, setAlertAdd] = useState(null);
  const [alertUpdate, setAlertUpdate] = useState(null);
  const [alertDelete, setAlertDelete] = useState(null);
  const [state, setState] = useState({
    columns: [
      { title: "خط العرض", field: "latitude", type: "numeric", align: "right" },
      {
        title: "خط الطول",
        field: "longitude",
        type: "numeric",
        align: "right",
      },

      { title: "منطقة", field: "region", align: "right" },

      { title: "عنوان", field: "address", align: "right" },

      {
        title: "الرمز البريدي",
        field: "postCode",
        type: "numeric",
        align: "right",
      },

      { title: "المدينة", field: "ville", align: "right" },

      { title: "المعرّف", field: "id", align: "right"  ,editable:"never"},
    ],
    data: [],
  });
  const getData = () => {
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
                <b> تنبيه تحذير: </b> تعذر الوصول إلى البيانات ...
              </span>
            }
            close
            color="danger"
            icon={Warning}
          />
        );
    });
  };
  useEffect(() => {
    getData();
  }, []); // eslint-disable-line react-hooks/exhaustive-deps
  const updatePlace = (newData, oldData) => {
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
      setAlertUpdate(
        <SnackbarContent
          message={
            <span>
              <b> تنبيه تحذير: </b> خانات فارغ ...
            </span>
          }
          close
          color="danger"
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
          latitude,
        })
      ).then((res) => {
        if (!res)
          setAlertUpdate(
            <SnackbarContent
              message={
                <span>
                  <b> تنبيه تحذير: </b> طلب سيئ ...
                </span>
              }
              close
              color="danger"
              icon={Warning}
            />
          );
        else {
          getData();
          setAlertUpdate(
            <SnackbarContent
              message={
                <span>
                  <b> تنبيه النجاح: </b> تم تحديث المكان ...{" "}
                </span>
              }
              close
              color="success"
              icon={Check}
            />
          );
        }
      });
    }
  };
  const deletePlace = (oldData) => {
    dispatch(placeDeleteData(oldData.id)).then((res) => {
      setAlert(null);
      setAlertAdd(null);
      setAlertUpdate(null);
      setAlertDelete(null);
      if (res) {
        getData();
        setAlertDelete(
          <SnackbarContent
            message={
              <span>
                <b> تنبيه النجاح: </b> المكان محذوف ...{" "}
              </span>
            }
            close
            color="success"
            icon={Check}
          />
        );
      } else
        setAlertDelete(
          <SnackbarContent
            message={
              <span>
                <b> تنبيه تحذير: </b> تعذر الوصول إلى البيانات ...
              </span>
            }
            close
            color="danger"
            icon={Warning}
          />
        );
    });
  };
  if (placeData.loading) return <center><CircularProgress /></center>;
  else
    return (
      <>
        {alert}
        {alertAdd}
        {alertUpdate}
        {alertDelete}
        <NavPills
          color="chengapp"
          alignCenter
          tabs={[
            {
              tabButton: "قائمة أماكن الحجز",
              tabIcon: Dashboard,
              tabContent: (
                <>
                  <MaterialTable
                    title="قائمة أماكن الحجز"
                    columns={state.columns}
                    localization={localization()}
                    icons={tableIcons}
                    data={state.data}
                    editable={{
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
              ),
            },
            {
              tabButton: "إضافة مكان الحجز",
              tabIcon: Schedule,
              tabContent: (
                <>
                  <GoogleMapFct />
                </>
              ),
            },
          ]}
        />
      </>
    );
}

PlaceList.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(stylesContent)(PlaceList);
