import React, { useContext } from "react";
import "./../static/login.css";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { withRouter } from "react-router-dom";
import { connect } from "react-redux";
import { loginUser } from "../redux/actions";
import { CHECKBOX_STATUS_INDETERMINATE } from "react-bootstrap-table-next";
import { authHeader } from "./../helpers/authHeader";

//const BACKEND_URL = "http://localhost:8080";
const BACKEND_URL = "https://exception-doggo-backend-dev.herokuapp.com";

class Login extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      username: "",
      password: "",
    };

    this.onChange = this.onChange.bind(this);
  }

  onChange = (event) => {
    const { name, value } = event.target;
    this.setState({ [name]: value });
  };

  onSubmit = (e) => {
    e.preventDefault();

    const fromData = this.state;

    const requestBody = {
      username: fromData.username,
      password: fromData.password,
    };

    const options = {
      method: "POST",
      headers: {
        "Content-Type": "application/json; charset=UTF-8",
      },
      body: JSON.stringify(requestBody),
    };
    const errorToastConfiguration = {
      position: "top-right",
      autoClose: 5000,
      hideProgressBar: true,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
      progress: undefined,
    };
    if (fromData.username === "admin") {
      fetch(`${BACKEND_URL}/login/admin`, options).then((response) => {
        if (response.status === 200) {
          response.json().then((json) => {
            this.props.dispatch({
              ...json,
              // logged:true,
              userType: "admin",
            });
            localStorage.setItem(
              "user",
              JSON.stringify({
                userId: json,
                ...{
                  authdata: window.btoa(
                    `${fromData.username}:${fromData.password}`
                  ),
                },
              })
            );
            this.props.history.push("/home");
          });
        } else {
          toast.error("❌ Korisnik ne postoji!", errorToastConfiguration);
        }
      });
    }
    if (document.getElementById("setac").selected) {
      fetch(`${BACKEND_URL}/login/walker`, options).then((response) => {
        if (response.status === 400) {
          toast.error("❌ Korisnik ne postoji!", errorToastConfiguration);
        } else {
          response.json().then((json) => {
            console.log(json);
            this.props.dispatch({
              id: json,
              // logged:true,
              userType: "korisnik",
            });
            console.log(
              JSON.stringify({
                userId: json,
                ...{
                  authdata: window.btoa(
                    `${fromData.username}:${fromData.password}`
                  ),
                },
              })
            );
            localStorage.setItem(
              "user",
              JSON.stringify({
                userId: json,
                ...{
                  authdata: window.btoa(
                    `${fromData.username}:${fromData.password}`
                  ),
                },
              })
            );
            console.log(localStorage.getItem("user"));
            this.props.history.push("/");
          });
        }
      });
    } else if (document.getElementById("udruga").selected) {
      fetch(`${BACKEND_URL}/login/association`, options).then((response) => {
        if (response.status === 400) {
          toast.error("❌ Korisnik ne postoji!", errorToastConfiguration);
        } else {
          response.json().then((json) => {
            console.log(json);
            this.props.dispatch({
              id: json,
              userType: "udruga",
            });
            localStorage.setItem(
              "user",
              JSON.stringify({
                userId: json,
                ...{
                  authdata: window.btoa(
                    `${fromData.username}:${fromData.password}`
                  ),
                },
              })
            );
            this.props.history.push("/home");
          });
        }
      });
    }
  };
  render() {
    return (
      <div className="Login">
        <div className="d-flex flex-column bg justify-content-between">
          <form>
            <div className="next text-center mx-auto">
              <div className="lead rounded mx-auto white-container justify-content-center">
                <div className="form-reg center justify-content-center mx-auto">
                  <div className="mx-auto mt-2 text-center">
                    <label>
                      <strong className="title-log">PRIJAVA</strong>
                    </label>
                  </div>
                  <div className="mx-auto text-center col-sm-8 my-2">
                    <input
                      className="form-control"
                      name={"username"}
                      type="username"
                      placeholder="Upišite svoj username"
                      onChange={this.onChange}
                      value={this.state.username}
                    ></input>
                  </div>
                  <div className="mx-auto text-center col-sm-8 my-2">
                    <input
                      className="form-control"
                      name={"password"}
                      placeholder="Upišite svoju šifru"
                      type="password"
                      onChange={this.onChange}
                      value={this.state.password}
                    ></input>
                  </div>
                  <div className="mx-auto text-center col-auto col-sm-8 my-2">
                    <select
                      className="custom-select mr-sm-2"
                      id="inlineFormCustomSelect"
                    >
                      <option selected>Tip korisnika...</option>
                      <option id="setac">Šetač pasa</option>
                      <option id="udruga">Udruga</option>
                    </select>
                  </div>
                  <button
                    className="btn btn-dark mt-2 text-align text-center"
                    float="center"
                    type="submit"
                    onClick={this.onSubmit}
                  >
                    PRIJAVA
                  </button>
                  <div className="mx-auto text-center col-auto col-sm-8 my-2 justify-content-center">
                    <span className="space">Niste korisnik?</span>
                    <a
                      className="btn btn-dark mt-2 text-align text-center"
                      href="/registration"
                      role="button"
                    >
                      REGISTRACIJA
                    </a>
                  </div>
                </div>
              </div>
            </div>
          </form>
        </div>
        <ToastContainer />
      </div>
    );
  }
}

const mapDispatchToProps = (dispatch) => {
  return {
    dispatch: (user) => dispatch(loginUser(user)),
  };
};

const mapStateToProps = (state) => {
  return {
    logged: !(state.user == {}),
  };
};

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Login));
