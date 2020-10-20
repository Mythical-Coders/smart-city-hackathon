import React, { useEffect } from "react";
import { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
  imageGetData
} from "../../../actions/ImageActions";
import { CircularProgress } from "@material-ui/core";
import { Warning} from "@material-ui/icons";
import { makeStyles } from "@material-ui/core/styles";
import styles from "../../../assets/jss/material-kit-react/views/componentsSections/typographyStyle.js";
import SnackbarContent from "../../../components/Snackbar/SnackbarContent.js";
import GridContainer from "../../../components/Grid/GridContainer.js";
import GridItem from "../../../components/Grid/GridItem.js";
import { useHistory } from "react-router-dom";
import Button from "../../../components/CustomButtons/Button";

const useStyles = makeStyles(styles);
function ImageDetail(props) {
  const history = useHistory();
  const classes = useStyles();
  const dispatch = useDispatch();
  const image = useSelector((state) => state.image);
  const [alert, setAlert] = useState(null);
  const [data, setData] = useState(null);

  const getData = () => {
    dispatch(imageGetData(props.match.params.id)).then((res) => {
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

  if (image.loading) return <CircularProgress />;
  return (
    <>
      {alert}
      <Button
        className="button icon-left"
        color="chengapp"
        onClick={history.goBack}>
          رجوع 
      </Button>
      <GridContainer justify="center">
        <GridItem xs={12} sm={12} md={6}>
          {data ? (
            <div className={classes.title} style={{ textAlign: "center" }}>
              <img alt={data.title} height="30%" width="300px" src={`data:image/jpeg;base64,${data.image.data}`} />
              <h3>{data.title}</h3>
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

export default ImageDetail;
