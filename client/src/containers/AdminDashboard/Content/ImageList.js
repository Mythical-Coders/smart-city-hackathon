import React, { useState, useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";
import { useHistory } from "react-router-dom";
import { imageDeleteData, imageGetAll } from "../../../actions/ImageActions";
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
import ImageUploader from "../../imageUpload/ImageUploader";

function ImageList() {
  const history = useHistory();
  const dispatch = useDispatch();
  const imageData = useSelector((state) => state.image);
  const [alert, setAlert] = useState(null);
  const [alertDelete, setAlertDelete] = useState(null);
  const [state, setState] = useState({
    columns: [
      {
        title: "تاريخ الإنشاء",
        field: "createDate",
        align: "right",
        defaultSort: "desc",
      },
      {
        title: "العنوان",
        field: "title",
        align: "right",
      },
      { title: "المعرّف", field: "id", align: "right", editable: "never" },
    ],
    data: [],
  });
  const getData = () => {
    dispatch(imageGetAll()).then((res) => {
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

  const deleteImage = (oldData) => {
    dispatch(imageDeleteData(oldData.id)).then((res) => {
      setAlert(null);
      setAlertDelete(null);
      if (res) {
        getData();

        setAlertDelete(
          <SnackbarContent
            message={
              <span>
                <b> تنبيه النجاح: </b> تم حذف الصورة ...{" "}
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
  if (imageData.loading)
    return (
      <center>
        <CircularProgress />
      </center>
    );
  else
    return (
      <>
        {alert}
        {alertDelete}
        <NavPills
          alignCenter
          color="chengapp"
          tabs={[
            {
              tabButton: "قائمة الصور",
              tabIcon: Dashboard,
              tabContent: (
                <>
                  <MaterialTable
                    title="قائمة الصور"
                    columns={state.columns}
                    icons={tableIcons}
                    data={state.data}
                    localization={localization()}
                    onRowClick={(e, rowData) =>
                      history.push("/admin_dashboard/images/" + rowData.id)
                    }
                    editable={{
                      onRowDelete: (oldData) =>
                        new Promise((resolve) => {
                          resolve();
                          deleteImage(oldData);
                        }),
                    }}
                  />
                </>
              ),
            },
            {
              tabButton: "إضافة الصور",
              tabIcon: Schedule,
              tabContent: (
                <>
                  <ImageUploader />{" "}
                </>
              ),
            },
          ]}
        />
      </>
    );
}

ImageList.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(stylesContent)(ImageList);
