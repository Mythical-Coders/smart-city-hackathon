import React, { useState, useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";
import {
  profileDeleteData,
  profileGetAll,
  profilePostData,
  profileUpdateData,
} from "../../../actions/ProfileActions";
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

function ProfileList() {
  const dispatch = useDispatch();
  const profileData = useSelector((state) => state.profile);
  const [alert, setAlert] = useState(null);
  const [alertAdd, setAlertAdd] = useState(null);
  const [alertUpdate, setAlertUpdate] = useState(null);
  const [alertDelete, setAlertDelete] = useState(null);
  const [state, setState] = useState({
    columns: [
      { title: "المنطقة", field: "region", align: "right" },
      {
        title: "بطاقة الهوية الوطنية",
        field: "cin",
        align: "right",
      },
      { title: "الهاتف", field: "telephone", type: "numeric", align: "right" },
      { title: "اللقب", field: "lastName", align: "right" },
      { title: "الإسم", field: "firstName", align: "right" },
      { title: "معرف المستخدم", field: "userID", align: "right" },

      { title: "المعرّف", field: "id", align: "right" ,editable:"never"},
    ],
    data: [],
  });
  const getData = () => {
    dispatch(profileGetAll()).then((res) => {
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
  const addProfile = (newData) => {
    setAlert(null);
    setAlertAdd(null);
    setAlertUpdate(null);
    setAlertDelete(null);
    if (
      !newData.telephone ||
      !newData.cin ||
      !newData.userID ||
      !newData.firstName ||
      !newData.lastName ||
      !newData.region
    ) {
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
      let telephone = newData.telephone;
      let cin = newData.cin;
      let userID = newData.userID;
      let firstName = newData.firstName;
      let lastName = newData.lastName;
      let region = newData.region;

      dispatch(
        profilePostData({
          telephone,
          cin,
          userID,
          firstName,
          lastName,
          region,
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
  const updateProfile = (newData, oldData) => {
    setAlert(null);
    setAlertAdd(null);
    setAlertUpdate(null);
    setAlertDelete(null);

    if (!newData.telephone ||
      !newData.cin ||
      !newData.userID ||
      !newData.firstName ||
      !newData.lastName ||
      !newData.region) {
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
      let telephone = newData.telephone;
      let cin = newData.cin;
      let userID = newData.userID;
      let firstName = newData.firstName;
      let lastName = newData.lastName;
      let region = newData.region;
      dispatch(
        profileUpdateData({
          id,
          telephone,
          cin,
          userID,
          firstName,
          lastName,
          region,
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
  const deleteProfile = (oldData) => {
    dispatch(profileDeleteData(oldData.id)).then((res) => {
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
                <b> تنبيه النجاح: </b> حذف الحجز ...{" "}
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
  if (profileData.loading) return <center><CircularProgress /></center>;
  else
    return (
      <>
        {alert}
        {alertAdd}
        {alertUpdate}
        {alertDelete}
        <MaterialTable
          title="ملفات تعريف المستخدمين"
          columns={state.columns}
          localization={localization()}
          icons={tableIcons}
          data={state.data}
          editable={{
            onRowAdd: (newData) =>
              new Promise((resolve) => {
                resolve();
                addProfile(newData);
              }),
            onRowUpdate: (newData, oldData) =>
              new Promise((resolve) => {
                resolve();
                updateProfile(newData, oldData);
              }),
            onRowDelete: (oldData) =>
              new Promise((resolve) => {
                resolve();
                deleteProfile(oldData);
              }),
          }}
        />
      </>
    );
}

ProfileList.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(stylesContent)(ProfileList);
