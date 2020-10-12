const initialState = {
    data: null,
    imageErr: "",
    loading: false,
  };
  
  const image = (state = initialState, action) => {
    switch (action.type) {
      case "IMAGE_LOADING":
        return {
          ...state,
          loading: true,
        };
      case "IMAGE_GET_DATA":
        return { ...state, data: action.payload, imageErr: "", loading: false };
      case "IMAGE_FAILURE":
        return { ...state, imageErr: "NO DATA FOUND", loading: false };
      case "IMAGE_END_LOADING":
        return {
          ...state,
          loading: false,
        };
      case "CLEAR_IMAGE":
        return { ...state, data: null, imageErr: "", loading: false };
      default:
        return state;
    }
  };
  
  export default image;
  