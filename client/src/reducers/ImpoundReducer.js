const initialState = {
    data: null,
    impoundErr: "",
    loading: false,
  };
  
  const impound = (state = initialState, action) => {
    switch (action.type) {
      case "IMPOUND_LOADING":
        return {
          ...state,
          loading: true,
        };
      case "IMPOUND_GET_DATA":
        return { ...state, data: action.payload, impoundErr: "", loading: false };
      case "IMPOUND_FAILURE":
        return { ...state, impoundErr: "NO DATA FOUND", loading: false };
      case "IMPOUND_END_LOADING":
        return {
          ...state,
          loading: false,
        };
      case "CLEAR_IMPOUND":
        return { ...state, data: null, impoundErr: "", loading: false };
      default:
        return state;
    }
  };
  
  export default impound;
  