const initialState = {
    data: null,
    placeErr: "",
    loading: false,
  };
  
  const place = (state = initialState, action) => {
    switch (action.type) {
      case "PLACE_LOADING":
        return {
          ...state,
          loading: true,
        };
      case "PLACE_GET_DATA":
        return { ...state, data: action.payload, placeErr: "", loading: false };
      case "PLACE_FAILURE":
        return { ...state, placeErr: "NO DATA FOUND", loading: false };
      case "PLACE_END_LOADING":
        return {
          ...state,
          loading: false,
        };
      case "CLEAR_PLACE":
        return { ...state, data: null, placeErr: "", loading: false };
      default:
        return state;
    }
  };
  
  export default place;
  