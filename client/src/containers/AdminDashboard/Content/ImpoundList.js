import React, { useState, useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";
import {
  impoundDeleteData,
  impoundGetAll,
  impoundPostData,
  impoundUpdateData,
} from "../../../actions/ImpoundActions";
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

function ImpoundList() {
  const dispatch = useDispatch();
  const impoundData = useSelector((state) => state.impound);
  const [alert, setAlert] = useState(null);
  const [alertAdd, setAlertAdd] = useState(null);
  const [alertUpdate, setAlertUpdate] = useState(null);
  const [alertDelete, setAlertDelete] = useState(null);
  const [state, setState] = useState({
    columns: [
      {
        title: "تاريخ الافراج ",
        field: "releaseDate",
        type: "date",
        align: "right",
      },

      {
        title: "اطلق سراح",
        field: "released",
        lookup: { true: "Yes", false: "No" },
        align: "right",
      },
      { title: "تاريخ الدفع", field: "paidDate", type: "date", align: "right" },

      {
        title: "دفع",
        field: "paid",
        lookup: { true: "Yes", false: "No" },
        align: "right",
      },
      {
        title: "تاريخ الحجز",
        field: "impoundDate",
        type: "date",
        align: "right",
      },

      {
        title: "بطاقة الهوية الوطنية",
        field: "cin",
        type: "numeric",
        align: "right",
      },

      { title: "الهاتف", field: "telephone", type: "numeric", align: "right" },

      { title: "رقم السياره", field: "matricule", align: "right" },

      { title: " معرّف السائق", field: "idDriver", align: "right" },

      { title: "المعرّف", field: "id", align: "right" },
    ],
    data: [],
  });
  useEffect(() => {
    dispatch(impoundGetAll()).then((res) => {
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
            color="warning"
            icon={Warning}
          />
        );
    });
  }, [dispatch, state.columns]);
  const addImpound = (newData) => {
    setAlert(null);
    setAlertAdd(null);
    setAlertUpdate(null);
    setAlertDelete(null);
    if (
      !newData.idDriver ||
      !newData.matricule ||
      !newData.telephone ||
      !newData.cin ||
      !newData.impoundDate
    ) {
      setAlertAdd(
        <SnackbarContent
          message={
            <span>
              <b> تنبيه تحذير: </b> خانات فارغ ...
            </span>
          }
          close
          color="warning"
          icon={Warning}
        />
      );
    } else {
      let idDriver = newData.idDriver;
      let matricule = newData.matricule;
      let telephone = newData.telephone;
      let cin = newData.cin;
      let impoundDate = newData.impoundDate;
      let paid = newData.paid;
      let paidDate = newData.paidDate;
      let released = newData.released;
      let releaseDate = newData.releaseDate;

      dispatch(
        impoundPostData({
          idDriver,
          matricule,
          telephone,
          cin,
          impoundDate,
          paid,
          paidDate,
          released,
          releaseDate,
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
              color="warning"
              icon={Warning}
            />
          );
        else {
          setState({
            columns: state.columns,
            data: [...state.data, res.data],
          });
          setAlertAdd(
            <SnackbarContent
              message={
                <span>
                  <b> تنبيه النجاح: </b> تمت إضافة الحجز ...{" "}
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
  const updateImpound = (newData, oldData) => {
    setAlert(null);
    setAlertAdd(null);
    setAlertUpdate(null);
    setAlertDelete(null);
    console.log(newData, oldData);
    if (
      !newData.idDriver ||
      !newData.matricule ||
      !newData.telephone ||
      !newData.cin ||
      !newData.impoundDate
    ) {
      setAlertUpdate(
        <SnackbarContent
          message={
            <span>
              <b> تنبيه تحذير: </b> خانات فارغ ...
            </span>
          }
          close
          color="warning"
          icon={Warning}
        />
      );
    } else {
      let id = oldData.id;
      let idDriver = newData.idDriver;
      let matricule = newData.matricule;
      let telephone = newData.telephone;
      let cin = newData.cin;
      let impoundDate = newData.impoundDate;
      let paid = newData.paid;
      let paidDate = newData.paidDate;
      let released = newData.released;
      let releaseDate = newData.releaseDate;
      dispatch(
        impoundUpdateData({
          id,
          idDriver,
          matricule,
          telephone,
          cin,
          impoundDate,
          paid,
          paidDate,
          released,
          releaseDate,
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
              color="warning"
              icon={Warning}
            />
          );
        else
          setAlertUpdate(
            <SnackbarContent
              message={
                <span>
                  <b> تنبيه النجاح: </b> تم تحديث الحجز ...{" "}
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
  const deleteImpound = (oldData) => {
    dispatch(impoundDeleteData(oldData.id)).then((res) => {
      setAlert(null);
      setAlertAdd(null);
      setAlertUpdate(null);
      setAlertDelete(null);
      if (res)
        setAlertDelete(
          <SnackbarContent
            message={
              <span>
                <b> تنبيه النجاح: </b> حذف الحجز ...{" "}
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
                <b> تنبيه تحذير: </b> تعذر الوصول إلى البيانات ...
              </span>
            }
            close
            color="warning"
            icon={Warning}
          />
        );
    });
  };
  if (impoundData.loading) return <CircularProgress />;
  else
    return (
      <>
        {alert}
        {alertAdd}
        {alertUpdate}
        {alertDelete}
        <MaterialTable
          title="قائمة الحجز"
          columns={state.columns}
          localization={localization()}
          icons={tableIcons}
          data={state.data}
          editable={{
            onRowAdd: (newData) =>
              new Promise((resolve) => {
                resolve();
                addImpound(newData);
              }),
            onRowUpdate: (newData, oldData) =>
              new Promise((resolve) => {
                resolve();
                updateImpound(newData, oldData);
              }),
            onRowDelete: (oldData) =>
              new Promise((resolve) => {
                resolve();
                deleteImpound(oldData);
              }),
          }}
        />
      </>
    );
}

ImpoundList.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(stylesContent)(ImpoundList);
