const initialState = {
  image: null,
};

export default function (state = initialState, action) {
  switch (action.type) {
    case "UPLOAD_IMAGE":
      return {
        ...state,
        image: action.payload,
      };
    case "CLEAR_UPLOAD_IMAGE":
      return { ...state, image: null };
    default:
      return state;
  }
}
