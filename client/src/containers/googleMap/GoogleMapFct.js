import React, { useState, useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";
import { makeStyles } from "@material-ui/core/styles";
import { Map, GoogleApiWrapper, Marker } from "google-maps-react";
import { placePostData } from "../../actions/PlaceActions";
import Button from "../../components/CustomButtons/Button.js";
import { RegionDropdown } from "react-country-region-selector";
import Input from "@material-ui/core/Input";
import GridContainer from "../../components/Grid/GridContainer.js";
import GridItem from "../../components/Grid/GridItem.js";
import styles from "../../assets/jss/material-kit-react/views/componentsSections/javascriptStyles";
import IconButton from "@material-ui/core/IconButton";
import Dialog from "@material-ui/core/Dialog";
import DialogTitle from "@material-ui/core/DialogTitle";
import DialogContent from "@material-ui/core/DialogContent";
import DialogActions from "@material-ui/core/DialogActions";
import Slide from "@material-ui/core/Slide";

// @material-ui/icons
import Close from "@material-ui/icons/Close";

const useStyles = makeStyles(styles);
const mapStyles = {
  width: "100%",
  height: "100%",
};
const Transition = React.forwardRef(function Transition(props, ref) {
  return <Slide direction="down" ref={ref} {...props} />;
});
Transition.displayName = "Transition";

function GoogleMapFct(props) {
  const placeData = useSelector((state) => state.place);
  const classes = useStyles();
  const dispatch = useDispatch();
  const [stores, setStores] = useState(null);
  const [newMark, setNewMark] = useState(null);
  const [address, setaddress] = useState("");
  const [postCode, setpostCode] = useState("");
  const [ville, setville] = useState("");
  const [region, setregion] = useState("");
  const [classicModal, setClassicModal] = useState(false);
  const [object, setObject] = useState("");
  const [body, setBody] = useState("");
  const handleChange = (e, name) => {
    const user = {};
    if (name === "region") user[name] = e;
    else user[name] = e.target.value;
    // validations
    switch (name) {
      case "address":
        setaddress(user.address);
        break;
      case "postCode":
        setpostCode(user.postCode);
        break;
      case "ville":
        setville(user.ville);
        break;
      case "region":
        setregion(user.region);
        break;
      default:
        break;
    }
  };
  const handleSave = async (e) => {
    e.preventDefault();
    if (!newMark || !ville || !postCode || !address || !region) {
      setObject("طلب سيئ");
      setBody("يجب إدخال كل البيانات و تحديد مكان  الحاجز في الخريطة ");
      setClassicModal(true);
    } else {
      let longitude = newMark.longitude;
      let latitude = newMark.latitude;
      dispatch(
        placePostData({
          ville,
          postCode,
          address,
          region,
          longitude,
          latitude,
        })
      ).then((res) => {
        if (res) {
          setObject("تنبيه النجاح");
          setBody("تمت إضافة المكان ");
          setClassicModal(true);
        } else {
          setObject("تنبيه تحذير");
          setBody("Server Error");
          setClassicModal(true);
        }
      });
    }
  };
  const displayMarkers = () => {
    return stores.map((store, index) => {
      return (
        <Marker
          key={index}
          id={index}
          position={{
            lat: store.latitude,
            lng: store.longitude,
          }}
          onClick={() => {
            setObject("تفاصيل مكان الحجز");
            setBody(store.address+" : الرمز البريدي\n"+
            store.postCode+" : العنوان\n"+
            store.ville+" : المدينة\n"+
            store.region+" : منطقة\n");
            setClassicModal(true);
          }}
        />
      );
    });
  };
  const addNewMark = (e, map, coord) => {
    setNewMark({
      latitude: coord.latLng.lat(),
      longitude: coord.latLng.lng(),
      message: "nouvelle",
    });
  };

  useEffect(() => {
    if (newMark) setStores([...placeData.data, newMark]);
    else setStores(placeData.data);
  }, [newMark,placeData.data]);
  return (
    <>
      <GridContainer>
        <GridItem xs={12} sm={12} md={5} style={{ padding: "20px" }}>
          <Map
            google={props.google}
            zoom={8}
            style={mapStyles}
            initialCenter={{ lat: 33.8869, lng: 9.5375 }}
            onClick={(e, map, coord) => addNewMark(e, map, coord)}
          >
            {stores ? displayMarkers() : ""}
          </Map>
        </GridItem>
        <GridItem xs={12} sm={12} md={6}>
          <form
            onSubmit={handleSave}
            style={{ padding: "20px", textAlign: "right" }}
          >
            <h1>إضافة مكان الحجز</h1>

            <h3>العنوان</h3>
            <Input
              id="address"
              name="address"
              inputProps={{
                placeholder: "العنوان",
              }}
              value={address}
              fullWidth
              onChange={(e) => handleChange(e, "address")}
            />

            <h3>الرمز البريدي</h3>
            <Input
              id="postCode"
              name="postCode"
              type="number"
              inputProps={{
                placeholder: "الرمز البريدي",
              }}
              value={postCode}
              fullWidth
              onChange={(e) => handleChange(e, "postCode")}
            />

            <h3>المدينة</h3>
            <Input
              id="ville"
              name="ville"
              inputProps={{
                placeholder: "المدينة",
              }}
              value={ville}
              fullWidth
              onChange={(e) => handleChange(e, "ville")}
            />
            <h3>المنطقة</h3>
            <RegionDropdown
              defaultOptionLabel="اختر المنطقة"
              id="region"
              name="region"
              country="Tunisia"
              value={region}
              onChange={(e) => handleChange(e, "region")}
            />
            <br />
            <br />
            <Button color="chengapp" type="submit" round>
              حفظ
            </Button>
            <Button round>إلغاء</Button>
            <br />
            <br />
          </form>
        </GridItem>
      </GridContainer>
      <Dialog
        classes={{
          root: classes.center,
          paper: classes.modal,
        }}
        open={classicModal}
        TransitionComponent={Transition}
        keepMounted
        onClose={() => setClassicModal(false)}
        aria-labelledby="classic-modal-slide-title"
        aria-describedby="classic-modal-slide-description"
      >
        <DialogTitle
          id="classic-modal-slide-title"
          disableTypography
          className={classes.modalHeader}
        >
          <IconButton
            className={classes.modalCloseButton}
            key="close"
            aria-label="Close"
            color="inherit"
            onClick={() => setClassicModal(false)}
          >
            <Close className={classes.modalClose} />
          </IconButton>
          <h4 className={classes.modalTitle}>{object}</h4>
        </DialogTitle>
        <DialogContent
          id="classic-modal-slide-description"
          className={classes.modalBody}
        >
          {body}
        </DialogContent>
        <DialogActions className={classes.modalFooter}>
          <Button onClick={() => setClassicModal(false)} color="danger" simple>
            أغلق
          </Button>
        </DialogActions>
      </Dialog>
    </>
  );
}

export default GoogleApiWrapper({
  apiKey: "AIzaSyBinRQZdFGIbmN2MairR4qfXzrRDS_pFiQ",
})(GoogleMapFct);
