export function authHeader() {
    // return authorization header with basic auth credentials
    let user = JSON.parse(localStorage.getItem('user'));
    console.log(localStorage.getItem('user'));
    if (user && user.authdata) {
        return ('Basic ' + user.authdata);
    } else {
        return {};
    }
}