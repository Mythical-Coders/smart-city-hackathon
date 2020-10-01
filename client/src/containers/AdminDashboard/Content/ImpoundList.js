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

function ImpoundList() {
  const dispatch = useDispatch();
  const impoundData = useSelector((state) => state.impound);
  const [alert, setAlert] = useState(null);
  const [alertAdd, setAlertAdd] = useState(null);
  const [alertUpdate, setAlertUpdate] = useState(null);
  const [alertDelete, setAlertDelete] = useState(null);
  const [state, setState] = useState({
    columns: [
      { title: "Id", field: "id" },
      { title: "idDriver", field: "idDriver" },
      { title: "matricule", field: "matricule" },
      { title: "telephone", field: "telephone", type: "numeric" },
      { title: "cin", field: "cin", type: "numeric" },
      { title: "impoundDate", field: "impoundDate", type: "date" },
      {
        title: "paid",
        field: "paid",
        lookup: { true: "Yes", false: "No" },
      },
      { title: "paidDate", field: "paidDate", type: "date" },

      {
        title: "released",
        field: "released",
        lookup: { true: "Yes", false: "No" },
      },
      { title: "releaseDate", field: "releaseDate", type: "date" },
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
      !newData.impoundDate ||
      !newData.paid ||
      !newData.paidDate ||
      !newData.released ||
      !newData.releaseDate
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
                  <b>SUCCESS ALERT:</b> Impound Added...
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
      !newData.impoundDate ||
      !newData.paid ||
      !newData.paidDate ||
      !newData.released ||
      !newData.releaseDate
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
                  <b>SUCCESS ALERT:</b> Impound Updated...
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
                <b>SUCCESS ALERT:</b> Impound Deleted...
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
  if (impoundData.loading) return <CircularProgress />;
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
