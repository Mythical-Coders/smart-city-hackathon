import React, { useEffect } from "react";
import { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
  impoundGetData,
  impoundPaid,
  impoundRelease,
} from "../../../actions/ImpoundActions";
import { CircularProgress } from "@material-ui/core";
import { Check, Warning, Schedule, Dashboard } from "@material-ui/icons";
import NavPills from "../../../components/NavPills/NavPills.js";
import { makeStyles } from "@material-ui/core/styles";
import styles from "../../../assets/jss/material-kit-react/views/componentsSections/typographyStyle.js";
import SnackbarContent from "../../../components/Snackbar/SnackbarContent.js";
import DateTimePicker from "react-datetime-picker";
import Button from "../../../components/CustomButtons/Button.js";
import GridContainer from "../../../components/Grid/GridContainer.js";
import GridItem from "../../../components/Grid/GridItem.js";

const useStyles = makeStyles(styles);
function EditImpound(props) {
  const classes = useStyles();
  const dispatch = useDispatch();
  const impoundData = useSelector((state) => state.impound);
  const [alert, setAlert] = useState(null);
  const [paid, setPaid] = useState(new Date());
  const [release, setRelease] = useState(new Date());
  const [data, setData] = useState(null);
  const [alertUpdate, setAlertUpdate] = useState(null);
  const handleChange = (e, name) => {
    const date = {};
    date[name] = e;
    switch (name) {
      case "paid":
        setPaid(new Date(date.paid));
        break;
      case "release":
        setRelease(new Date(date.release));
        break;
      default:
        break;
    }
  };
  const getData = () => {
    dispatch(impoundGetData(props.match.params.id)).then((res) => {
      if (res) {
        if (res.data) {
          setData(res.data);
          setAlert(null);
        }
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

  const handleRelease = async (e) => {
    e.preventDefault();
    setAlert(null);
    setAlertUpdate(null);
    if (!release) {
      setAlertUpdate(
        <SnackbarContent
          message={
            <span>
              <b> تنبيه تحذير: </b> عليك أن تضيف الوقت ...
            </span>
          }
          close
          color="danger"
          icon={Warning}
        />
      );
    } else {
      dispatch(
        impoundRelease({
          id: props.match.params.id,
          released: true,
          releaseDate: release,
        })
      ).then((res) => {
        if (!res)
          setAlertUpdate(
            <SnackbarContent
              message={
                <span>
                  <b> Server Error</b> 
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
  const handlePaid = async (e) => {
    e.preventDefault();
    setAlert(null);
    setAlertUpdate(null);
    if (!paid) {
      setAlertUpdate(
        <SnackbarContent
          message={
            <span>
              <b> تنبيه تحذير: </b> عليك أن تضيف الوقت ...
            </span>
          }
          close
          color="warning"
          icon={Warning}
        />
      );
    } else {
      dispatch(
        impoundPaid({
          id: props.match.params.id,
          paid: true,
          paidDate: paid,
        })
      ).then((res) => {
        if (!res)
          setAlertUpdate(
            <SnackbarContent
              message={
                <span>
                  <b> Server Error</b> 
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
  return (
    <>
      {alert}
      {alertUpdate}
      <GridContainer justify="center">
        <GridItem xs={12} sm={12} md={6}>
          <NavPills
            active={1}
            color="chengapp"
            tabs={[
              {
                tabButton: "إخراج السيارة ",
                tabIcon: Dashboard,
                tabContent: (
                  <>
                    <div
                      className={classes.title}
                      style={{ textAlign: "center", height: "350px" }}
                    >
                      <form onSubmit={handleRelease}>
                        <DateTimePicker
                          onChange={(e) => handleChange(e, "release")}
                          value={release}
                        />
                        <Button color="primary" type="submit" round>
                          حفظ
                        </Button>
                      </form>
                    </div>
                  </>
                ),
              },
              {
                tabButton: "دفع الخطية المالية ",

                tabIcon: Schedule,
                tabContent: (
                  <>
                    <div
                      className={classes.title}
                      style={{ textAlign: "center", height: "350px" }}
                    >
                      <form onSubmit={handlePaid}>
                        <DateTimePicker
                          onChange={(e) => handleChange(e, "paid")}
                          value={paid}
                        />
                        <Button color="primary" type="submit" round>
                          حفظ
                        </Button>
                      </form>
                    </div>
                  </>
                ),
              },
            ]}
          />
        </GridItem>
        <GridItem xs={12} sm={12} md={6}>
          {data ? (
            <div className={classes.title} style={{ textAlign: "center" }}>
              <h2>: رقم السيارة</h2>
              <h3>{data.matricule}</h3>
              <h2>: رقم الهاتف</h2>
              <h3>{data.telephone}</h3>
              <h2>: بطاقة الهوية الوطنية</h2>
              <h3>{data.cin}</h3>
              <h2>: تاريخ الحجز</h2>
              <h3>{new Date(data.impoundDate).toUTCString()}</h3>
              <h2>: دفع الخطية المالية</h2>
              <h3>{data.paid ? "نعم" : "لا"}</h3>
              <h2>: تاريخ الدفع</h2>
              <h3>{data.paidDate?new Date(data.paidDate).toUTCString():""}</h3>
              <h2>: إخراج السيارة</h2>
              <h3>{data.released ? "نعم" : "لا"}</h3>
              <h2>: تاريخ إخراج السيارة</h2>
              <h3>{data.releaseDate?new Date(data.releaseDate).toUTCString():""}</h3>
              <br /> <br />
            </div>
          ) : (
            ""
          )}
        </GridItem>
      </GridContainer>
    </>
  );
}

export default EditImpound;
