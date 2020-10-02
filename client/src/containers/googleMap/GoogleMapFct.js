import React, { useState, useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";
import { makeStyles } from "@material-ui/core/styles";
import { Map, GoogleApiWrapper, Marker } from "google-maps-react";
import { placeGetAll, placePostData } from "../../actions/PlaceActions";
import Button from "../../components/CustomButtons/Button.js";
import { RegionDropdown } from "react-country-region-selector";
import Input from "@material-ui/core/Input";
import GridContainer from "../../components/Grid/GridContainer.js";
import GridItem from "../../components/Grid/GridItem.js";
import styles from "../../assets/jss/material-kit-react/views/componentsSections/javascriptStyles"
import IconButton from "@material-ui/core/IconButton";
import Dialog from "@material-ui/core/Dialog";
import DialogTitle from "@material-ui/core/DialogTitle";
import DialogContent from "@material-ui/core/DialogContent";
import DialogActions from "@material-ui/core/DialogActions";
import Slide from "@material-ui/core/Slide";

// @material-ui/icons
import LibraryBooks from "@material-ui/icons/LibraryBooks";
import Close from "@material-ui/icons/Close";

const useStyles = makeStyles(styles);
const mapStyles = {
  width: "100%",
  height: "100%",
};
const dataPlaces = [
  { latitude: 35.8245, longitude: 10.6346, message: "sousse" },
  { latitude: 35.7643, longitude: 10.8113, message: "monastir" },
];
const Transition = React.forwardRef(function Transition(props, ref) {
  return <Slide direction="down" ref={ref} {...props} />;
});
Transition.displayName = "Transition";

function GoogleMapFct(props) {
  const classes = useStyles();
  const dispatch = useDispatch();
  const [stores, setStores] = useState(dataPlaces);
  const [newMark, setNewMark] = useState();
  const [address, setaddress] = useState("");
  const [postCode, setpostCode] = useState("");
  const [ville, setville] = useState("");
  const [region, setregion] = useState("");
  const [classicModal, setClassicModal] = useState(false);
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
      default:
        break;
    }
  };
  const handleSave = async (e) => {
    e.preventDefault();
    setClassicModal(true)
    // let longitude = newMark.longitude;
    // let latitude = newMark.latitude;
    // dispatch(
    //   placePostData({
    //     ville,
    //     postCode,
    //     address,
    //     region,
    //     longitude,
    //     latitude,
    //   })
    // ).then((res) => {
    //   console.log(res)
    // });
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
          onClick={() => console.log(store.message)}
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
    if (newMark) setStores([...dataPlaces, newMark]);
  }, [newMark]);
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
            {displayMarkers()}
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
              <h4 className={classes.modalTitle}>Modal title</h4>
            </DialogTitle>
            <DialogContent
              id="classic-modal-slide-description"
              className={classes.modalBody}
            >
              <p>
                Far far away, behind the word mountains, far from the countries
                Vokalia and Consonantia, there live the blind texts. Separated
                they live in Bookmarksgrove right at the coast of the Semantics,
                a large language ocean. A small river named Duden flows by their
                place and supplies it with the necessary regelialia. It is a
                paradisematic country, in which roasted parts of sentences fly
                into your mouth. Even the all-powerful Pointing has no control
                about the blind texts it is an almost unorthographic life One
                day however a small line of blind text by the name of Lorem
                Ipsum decided to leave for the far World of Grammar.
              </p>
            </DialogContent>
            <DialogActions className={classes.modalFooter}>
              <Button color="transparent" simple>
                Nice Button
              </Button>
              <Button
                onClick={() => setClassicModal(false)}
                color="danger"
                simple
              >
                Close
              </Button>
            </DialogActions>
          </Dialog>
    </>
  );
}

export default GoogleApiWrapper({
  apiKey: "AIzaSyBinRQZdFGIbmN2MairR4qfXzrRDS_pFiQ",
})(GoogleMapFct);
