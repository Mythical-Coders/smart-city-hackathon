import React, { useState, useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";
import { useHistory } from "react-router-dom";
import {
  impoundGetAll,
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
  const history = useHistory();
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
        lookup: { true: "نعم", false: "لا" },
        align: "right",
      },
      { title: "تاريخ الدفع", field: "paidDate", type: "date", align: "right" },

      {
        title: "دفع",
        field: "paid",
        lookup: { true: "نعم", false: "لا" },
        align: "right",
      },
      {
        title: "تاريخ الحجز",
        field: "impoundDate",
        type: "date",
        align: "right",
        editable: "never",
      },

      {
        title: "بطاقة الهوية الوطنية",
        field: "cin",
        align: "right",
        editable: "never",
        hidden: true,
      },

      {
        title: "الهاتف",
        field: "telephone",
        type: "numeric",
        align: "right",
        editable: "never",
      },

      {
        title: "رقم السياره",
        field: "matricule",
        align: "right",
        editable: "never",
      },

      {
        title: " معرّف السائق",
        field: "idDriver",
        align: "right",
        hidden: true,
      },

      { title: "المعرّف", field: "id", align: "right", hidden: true },
    ],
    data: [],
  });
  const getData = () => {
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
  };
  useEffect(() => {
    getData();
  }, []); // eslint-disable-line react-hooks/exhaustive-deps

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
        else {
          getData();

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
        }
      });
    }
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
          onRowClick={(e, rowData) =>
            history.push("/agent_dashboard/impound/" + rowData.id)
          }
          // editable={{
          //   onRowUpdate: (newData, oldData) =>
          //     new Promise((resolve) => {
          //       resolve();
          //       updateImpound(newData, oldData);
          //     }),
          // }}
        />
      </>
    );
}

ImpoundList.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(stylesContent)(ImpoundList);
