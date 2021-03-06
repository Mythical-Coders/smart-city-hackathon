import React, { useState, useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";
import {
  citizenDeleteData,
  citizenGetAll,
  citizenPostData,
  citizenUpdateData,
} from "../../../actions/CitizenActions";
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

function CitizenList() {
  const dispatch = useDispatch();
  const citizenData = useSelector((state) => state.citizen);
  const [alert, setAlert] = useState(null);
  const [alertAdd, setAlertAdd] = useState(null);
  const [alertUpdate, setAlertUpdate] = useState(null);
  const [alertDelete, setAlertDelete] = useState(null);
  const [state, setState] = useState({
    columns: [
      { title: "الهاتف", field: "telephone", align: "right" },
      { title: "رقم السياره", field: "matricule", align: "right" },
      {
        title: "بطاقة الهوية الوطنية",
        field: "cin",
        align: "right",
      },
      { title: "المعرّف", field: "id", align: "right"  ,editable:"never"},
    ],
    data: [],
  });
  const getData = () => {
    dispatch(citizenGetAll()).then((res) => {
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
  const addCitizen = (newData) => {
    setAlert(null);
    setAlertAdd(null);
    setAlertUpdate(null);
    setAlertDelete(null);
    if (!newData.matricule || !newData.telephone || !newData.cin) {
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
      let matricule = newData.matricule;
      let telephone = newData.telephone;
      let cin = newData.cin;

      dispatch(
        citizenPostData({
          matricule,
          telephone,
          cin
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
                  <b> تنبيه النجاح: </b> تمت إضافة مواطن ...{" "}
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
  const updateCitizen = (newData, oldData) => {
    setAlert(null);
    setAlertAdd(null);
    setAlertUpdate(null);
    setAlertDelete(null);

    if (!newData.matricule || !newData.telephone|| !newData.cin) {
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
      let matricule = newData.matricule;
      let telephone = newData.telephone;
      let cin = newData.cin;

      dispatch(
        citizenUpdateData({
          id,
          matricule,
          telephone,
          cin
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
                  <b> تنبيه النجاح: </b> تم تحديث المواطن ...{" "}
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
  const deleteCitizen = (oldData) => {
    dispatch(citizenDeleteData(oldData.id)).then((res) => {
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
                <b> تنبيه النجاح: </b> تم حذف مواطن ...{" "}
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
  if (citizenData.loading) return <center><CircularProgress /></center>;
  else
    return (
      <>
        {alert}
        {alertAdd}
        {alertUpdate}
        {alertDelete}
        <MaterialTable
          title="قائمة المواطنين"
          columns={state.columns}
          icons={tableIcons}
          data={state.data}
          localization={localization()}
          editable={{
            onRowAdd: (newData) =>
              new Promise((resolve) => {
                resolve();
                addCitizen(newData);
              }),
            onRowUpdate: (newData, oldData) =>
              new Promise((resolve) => {
                resolve();
                updateCitizen(newData, oldData);
              }),
            onRowDelete: (oldData) =>
              new Promise((resolve) => {
                resolve();
                deleteCitizen(oldData);
              }),
          }}
        />
      </>
    );
}

CitizenList.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(stylesContent)(CitizenList);
