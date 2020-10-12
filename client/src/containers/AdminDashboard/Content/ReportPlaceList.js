import React, { useState, useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";
import {
  reportPlaceDeleteData,
  reportPlaceGetAll,
  reportPlacePostData,
  reportPlaceUpdateData,
} from "../../../actions/ReportPlaceActions";
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
import localization from "../../tableFeatures/localization";

function ReportPlaceList() {
  const dispatch = useDispatch();
  const reportPlaceData = useSelector((state) => state.reportPlace);
  const [alert, setAlert] = useState(null);
  const [alertAdd, setAlertAdd] = useState(null);
  const [alertUpdate, setAlertUpdate] = useState(null);
  const [alertDelete, setAlertDelete] = useState(null);
  const [state, setState] = useState({
    columns: [
      { title: "خط العرض", field: "latitude", align: "right" , type:"numeric"},
      { title: "خط الطول", field: "longitude", align: "right" ,type:"numeric" },
      { title: "العنوان", field: "adress", align: "right" },

      {
        title: "نوع",
        field: "type",
        align: "right",
      },
      { title: "المعرّف", field: "id", align: "right"  ,editable:"never"},
    ],
    data: [],
  });
  const getData = () => {
    dispatch(reportPlaceGetAll()).then((res) => {
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
  const addReportPlace = (newData) => {
    setAlert(null);
    setAlertAdd(null);
    setAlertUpdate(null);
    setAlertDelete(null);
    if (!newData.adress ||!newData.type || !newData.longitude || !newData.latitude) {
      setAlertAdd(
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
      let adress = newData.adress;
      let type = newData.type;
      let longitude = newData.longitude;
      let latitude = newData.latitude;

      dispatch(
        reportPlacePostData({
          adress,
          type,
          longitude,latitude
        })
      ).then((res) => {
        if (!res)
          setAlertAdd(
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
          setAlertAdd(
            <SnackbarContent
              message={
                <span>
                  <b> تنبيه النجاح: </b> تمت إضافة مكان التقرير ...{" "}
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
  const updateReportPlace = (newData, oldData) => {
    setAlert(null);
    setAlertAdd(null);
    setAlertUpdate(null);
    setAlertDelete(null);

    if (!newData.adress ||!newData.type || !newData.longitude || !newData.latitude) {
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
      let adress = newData.adress;
      let type = newData.type;
      let longitude = newData.longitude;
      let latitude = newData.latitude;

      dispatch(
        reportPlaceUpdateData({
          id,
          adress,
          type,
          longitude,latitude
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
                  <b> تنبيه النجاح: </b> تم تحديث مكان التقرير ...{" "}
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
  const deleteReportPlace = (oldData) => {
    dispatch(reportPlaceDeleteData(oldData.id)).then((res) => {
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
                <b> تنبيه النجاح: </b> تم حذف مكان التقرير ...{" "}
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
  if (reportPlaceData.loading) return <CircularProgress />;
  else
    return (
      <>
        {alert}
        {alertAdd}
        {alertUpdate}
        {alertDelete}
        <MaterialTable
          title="قائمة أماكن التقرير"
          columns={state.columns}
          icons={tableIcons}
          data={state.data}
          localization={localization()}
          editable={{
            onRowAdd: (newData) =>
              new Promise((resolve) => {
                resolve();
                addReportPlace(newData);
              }),
            onRowUpdate: (newData, oldData) =>
              new Promise((resolve) => {
                resolve();
                updateReportPlace(newData, oldData);
              }),
            onRowDelete: (oldData) =>
              new Promise((resolve) => {
                resolve();
                deleteReportPlace(oldData);
              }),
          }}
        />
      </>
    );
}

ReportPlaceList.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(stylesContent)(ReportPlaceList);
