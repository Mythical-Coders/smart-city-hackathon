const initialState = {
    data: null,
    citizenErr: "",
    loading: false,
  };
  
  const citizen = (state = initialState, action) => {
    switch (action.type) {
      case "CITIZEN_LOADING":
        return {
          ...state,
          loading: true,
        };
      case "CITIZEN_GET_DATA":
        return { ...state, data: action.payload, citizenErr: "", loading: false };
      case "CITIZEN_FAILURE":
        return { ...state, citizenErr: "NO DATA FOUND", loading: false };
      case "CITIZEN_END_LOADING":
        return {
          ...state,
          loading: false,
        };
      case "CLEAR_CITIZEN":
        return { ...state, data: null, citizenErr: "", loading: false };
      default:
        return state;
    }
  };
  
  export default citizen;
  