import React, { useState, useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";
import { getAllUsers, signUp, deleteUser } from "../../../actions/AuthActions";
import MaterialTable from "material-table";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/core/styles";
import { stylesContent } from "../styles/Styles";
import { tableIcons } from "../../tableFeatures/tableIcons";
import Warning from "@material-ui/icons/Warning";
import Check from "@material-ui/icons/Check";
import CircularProgress from "@material-ui/core/CircularProgress";
import SnackbarContent from "../../../components/Snackbar/SnackbarContent.js";
import localization from "../../tableFeatures/localization";

function UserList() {
  const dispatch = useDispatch();
  const authDetail = useSelector((state) => state.auth);
  const [alert, setAlert] = useState(null);
  const [alertAdd, setalertAdd] = useState(null);
  const [alertDelete, setalertDelete] = useState(null);

  const [state, setState] = useState({
    columns: [
      {
        title: "وظيفة",
        field: "roles[0].name",
        lookup: {
          ROLE_ADMIN: "Admin",
          ROLE_RECEPTIVE: "Receptive",
          ROLE_DRIVER: "Driver",
        },
        align: "right",
      },
      { title: "كلمه السر", field: "password", align: "right" },

      { title: "البريد الإلكتروني", field: "email", align: "right" },
      { title: "اسم المستخدم", field: "username", align: "right" },
      { title: "المعرّف", field: "id", align: "right" },
    ],
    data: [],
  });
  useEffect(() => {
    dispatch(getAllUsers()).then((res) => {
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
  const addUser = (newData) => {
    setAlert(null);
    setalertDelete(null);
    if (
      !newData.username ||
      !newData.email ||
      !newData.password ||
      !newData.roles
    ) {
      setalertAdd(
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
      let username = newData.username;
      let email = newData.email;
      let password = newData.password;
      let roles;
      switch (newData.roles[0].name) {
        case "ROLE_ADMIN":
          roles = ["admin"];
          break;
        case "ROLE_RECEPTIVE":
          roles = ["receptive"];
          break;
        default:
          roles = ["driver"];
          break;
      }
      dispatch(signUp({ username, email, password, roles }))
        .then((res) => {
          if (res.type === "SIGNUP_USER_FAILURE")
            setalertAdd(
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
            setalertAdd(
              <SnackbarContent
                message={
                  <span>
                    <b> تنبيه النجاح: </b> تمت إضافة المستخدم ...
                  </span>
                }
                close
                color="success"
                icon={Check}
              />
            );
          }
        })
        .catch((err) => {
          setalertAdd(
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
    }
  };
  const deleteOneUser = (oldData) => {
    dispatch(deleteUser(oldData.id)).then((res) => {
      setAlert(null);
      setalertAdd(null);
      if (res)
        setalertDelete(
          <SnackbarContent
            message={
              <span>
                <b> تنبيه النجاح: </b> تم حذف المستخدم ...
              </span>
            }
            close
            color="success"
            icon={Check}
          />
        );
      else
        setalertDelete(
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
  if (authDetail.loading) return <CircularProgress />;
  else
    return (
      <>
        {alert}
        {alertAdd}
        {alertDelete}
        <MaterialTable
          title="قائمة المستخدمين"
          columns={state.columns}
          data={state.data}
          icons={tableIcons}
          localization={localization()}
          editable={{
            onRowAdd: (newData) =>
              new Promise((resolve) => {
                resolve();
                addUser(newData);
              }),
            onRowDelete: (oldData) =>
              new Promise((resolve) => {
                resolve();
                deleteOneUser(oldData);
              }),
          }}
        />
      </>
    );
}

UserList.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(stylesContent)(UserList);