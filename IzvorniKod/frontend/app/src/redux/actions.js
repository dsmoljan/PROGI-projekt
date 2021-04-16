import { CREATE_USER, LOGIN, LOGOUT } from './actionTypes'

const createUser = (user) => {
    return {
        type: CREATE_USER,
        user
    }
}

const loginUser = (user) => {
    return {
        type: LOGIN,
        user,
    }
}

const logoutUser = () => {
    return {
        type: LOGOUT
    }
}

export {createUser, loginUser, logoutUser}