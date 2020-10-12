const initialState = {
    data: null,
    reportErr: "",
    loading: false,
  };
  
  const report = (state = initialState, action) => {
    switch (action.type) {
      case "REPORT_LOADING":
        return {
          ...state,
          loading: true,
        };
      case "REPORT_GET_DATA":
        return { ...state, data: action.payload, reportErr: "", loading: false };
      case "REPORT_FAILURE":
        return { ...state, reportErr: "NO DATA FOUND", loading: false };
      case "REPORT_END_LOADING":
        return {
          ...state,
          loading: false,
        };
      case "CLEAR_REPORT":
        return { ...state, data: null, reportErr: "", loading: false };
      default:
        return state;
    }
  };
  
  export default report;
  