import {CREATE_USER, LOGIN, LOGOUT} from "../actionTypes";

const initialState = {
  user: {logged: false}
};

const userReducer = (state = initialState, action) => {
  console.log(action);
  switch (action.type) {
    case CREATE_USER:
    case LOGIN: {
      console.log(action.type)
      return {
        ...state,
        ...action.user,
        logged: true,
      };
    }
    case LOGOUT: {
      console.log('LOGOUT')
      return {
        logged: false
      };
    }
    default: {
      return state;
    }
  }
};

export default userReducer;