import { combineReducers } from "redux";
import auth from "./AuthReducer";
import impound from "./ImpoundReducer";
import place from "./PlaceReducer";
import citizen from "./CitizenReducer";
import profile from "./ProfileReducer";
import report from "./ReportReducer"
import reportPlace from "./ReportPlaceReducer"
import image from "./ImageReducer"
import uploadReducer from "./uploadReducer";
import notification from "./NotificationReducer"
const RootReducer = combineReducers({
  auth: auth,
  impound: impound,
  place: place,
  citizen: citizen,
  profile:profile,
  report:report,
  reportPlace:reportPlace,
  image:image,
  upload: uploadReducer,
  notification:notification
});

export default RootReducer;
