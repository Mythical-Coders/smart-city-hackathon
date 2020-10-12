const initialState = {
    data: null,
    reportPlaceErr: "",
    loading: false,
  };
  
  const reportPlace = (state = initialState, action) => {
    switch (action.type) {
      case "REPORT_PLACE_LOADING":
        return {
          ...state,
          loading: true,
        };
      case "REPORT_PLACE_GET_DATA":
        return { ...state, data: action.payload, reportPlaceErr: "", loading: false };
      case "REPORT_PLACE_FAILURE":
        return { ...state, reportPlaceErr: "NO DATA FOUND", loading: false };
      case "REPORT_PLACE_END_LOADING":
        return {
          ...state,
          loading: false,
        };
      case "CLEAR_REPORT_PLACE":
        return { ...state, data: null, reportPlaceErr: "", loading: false };
      default:
        return state;
    }
  };
  
  export default reportPlace;
  