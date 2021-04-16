import {
    LOGOUT
} from "../actionTypes";

const initialState = {};

const BACKEND_URL = "http://localhost:8080";
//const BACKEND_URL = "https://exception-doggo-backend-dev.herokuapp.com";

const logoutReducer = (state = initialState, action) => {
    console.log(action);
    switch (action.type) {
        case LOGOUT: {
            fetch(`${BACKEND_URL}/logout`).then((response) => {
                if (response.status === 200) {
                    console.log('logout')
                }
            })
            localStorage.removeItem('user')
            console.log('LOGOUT')
            return {
                user: undefined
            };
        }
        default: {
            return state;
        }
    }
};

export default logoutReducer;