import { combineReducers } from "redux";
import auth from "./AuthReducer";
import impound from "./ImpoundReducer";
import place from "./PlaceReducer";
import citizen from "./CitizenReducer";
import profile from "./ProfileReducer";

const RootReducer = combineReducers({
  auth: auth,
  impound: impound,
  place: place,
  citizen: citizen,
  profile:profile
});

export default RootReducer;
