import React, { useState, useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";
import { useHistory } from "react-router-dom";
import {
  impoundGetAll,
} from "../../../actions/ImpoundActions";
import MaterialTable from "material-table";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/core/styles";
import { stylesContent } from "../styles/Styles";
import { tableIcons } from "../../tableFeatures/tableIcons";
// import Dashboard from "@material-ui/icons/Dashboard";
// import Schedule from "@material-ui/icons/Schedule";
import Warning from "@material-ui/icons/Warning";
import CircularProgress from "@material-ui/core/CircularProgress";
import SnackbarContent from "../../../components/Snackbar/SnackbarContent.js";
import localization from "../../tableFeatures/localization";

function ImpoundList() {
  const history = useHistory();
  const dispatch = useDispatch();
  const impoundData = useSelector((state) => state.impound);
  const [alert, setAlert] = useState(null);
  const [state, setState] = useState({
    columns: [
      {
        title: "تاريخ إخراج السيارة ",
        field: "releaseDate",
        type: "date",
        align: "right",
      },

      {
        title: "إخراج السيارة",
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
        defaultSort: "desc"
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
        title: " معرّف مكان الحجز",
        field: "idPlace",
        align: "right",
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
            color="danger"
            icon={Warning}
          />
        );
    });
  };
  useEffect(() => {
    getData();
  }, []); // eslint-disable-line react-hooks/exhaustive-deps

  if (impoundData.loading) return <CircularProgress />;
  else
    return (
      <>
        {alert}
        <MaterialTable
          title="قائمة الحجز"
          columns={state.columns}
          localization={localization()}
          icons={tableIcons}
          data={state.data}
          onRowClick={(e, rowData) =>
            history.push("/agent_dashboard/impound/" + rowData.id)
          }
        />
      </>
    );
}

ImpoundList.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(stylesContent)(ImpoundList);
