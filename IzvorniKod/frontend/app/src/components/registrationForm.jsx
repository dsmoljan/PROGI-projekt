import React, { useState, useEffect } from "react";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import "./../static/reg.css";
import { createUser } from "../redux/actions";
import { withRouter } from "react-router-dom";
import { connect } from "react-redux";
import { authHeader } from "./../helpers/authHeader";

//const BACKEND_URL = "http://localhost:8080";
const BACKEND_URL = "https://exception-doggo-backend-dev.herokuapp.com";

class Registration extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      user: {
        id: "",
        oib: "",
        name: "",
        firstName: "",
        lastName: "",
        username: "",
        password: "",
        confirmPassword: "",
        email: "",
        // webAddress: null,
        // description: "",
        // pictureUrl: null,
        // phoneNumber: null,
      },
      title: {
        setacSelected: true,
        udrugaSelected: false,
      },
      errors: {
        errors: [],
      },
    };

    this.onChange = this.onChange.bind(this);
    // this.setErrors = this.setErrors.bind(this);
  }

  onChange = (event) => {
    const { name, value } = event.target;
    const { user } = this.state;
    this.setState({
      user: {
        ...user,
        [name]: value,
      },
    });
  };

  useEffect = () => {
    var element = document.getElementsByClassName("setac");
    if (element[0].style.color === "grey") {
      this.state.title.setacSelected = false;
      this.state.title.udrugaSelected = true;
    } else {
      this.state.title.setacSelected = true;
      this.state.title.udrugaSelected = false;
    }
  };

  udrugaClick = (event) => {
    if (this.state.title.setacSelected === true) {
      this.state.title.setacSelected = false;
      this.state.title.udrugaSelected = true;
      event.target.style.color = "black";
      let element = document.getElementsByClassName("setac");
      element[0].style.color = "grey";
      document.getElementById("oib").classList.toggle("d-none");
      document.getElementById("ana").classList.toggle("d-none");
      // document.getElementById("web").classList.toggle("d-none");
      // document.getElementById("desc").classList.toggle("d-none");
    }
  };
  setacClick = (event) => {
    if (this.state.title.udrugaSelected) {
      this.state.title.setacSelected = true;
      this.state.title.udrugaSelected = false;
      event.target.style.color = "black";
      let element = document.getElementsByClassName("udruga");
      element[0].style.color = "grey";
      document.getElementById("oib").classList.toggle("d-none");
      document.getElementById("ana").classList.toggle("d-none");
      // document.getElementById("web").classList.toggle("d-none");
      // document.getElementById("desc").classList.toggle("d-none");
    }
  };

  checkPassword = () => {
    return this.state.user.password !== this.state.user.confirmPassword;
  };

  checkPasswordLength = () => {
    if (
      this.state.user.password !== undefined &&
      (this.state.user.password.length < 6 ||
        this.state.user.password.length > 20)
    ) {
      toast.error("❌ Lozinka mora sadržavati između 6 i 20 znakova!", {
        position: "top-right",
        autoClose: 5000,
        hideProgressBar: true,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
      });
      return false;
    }
    return true;
  };

  usernameCheck = (event) => {
    const options = {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        Authorization: authHeader(),
      },
      body: JSON.stringify(`username=${this.state.user.username}`),
    };
    if (this.state.title.udrugaSelected) {
      fetch(
        `${BACKEND_URL}/registration/association/username-available/?username=${this.state.user.username}`,
        options
      ).then((response) => {
        if (response.status === 400) {
          toast.error("❌ Korisničko ime nije slobodno!", {
            position: "top-right",
            autoClose: 5000,
            hideProgressBar: true,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
          });
        }
      });
    } else {
      fetch(
        `${BACKEND_URL}/registration/walker/username-available/?username=${this.state.user.username}`,
        options
      ).then((response) => {
        if (response.status === 400) {
          toast.error("❌ Korisničko ime nije slobodno!", {
            position: "top-right",
            autoClose: 5000,
            hideProgressBar: true,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
          });
        }
      });
    }
  };

  emailCheck = (event) => {
    const options = {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        Authorization: authHeader(),
      },
      //body: JSON.stringify(`username=${form.username}`),
    };
    if (this.state.title.udrugaSelected) {
      fetch(
        `${BACKEND_URL}/registration/association/email-available/?email=${this.state.user.email}`,
        options
      ).then((response) => {
        if (response.status === 400) {
          toast.error("❌ Email se već koristi!", {
            position: "top-right",
            autoClose: 5000,
            hideProgressBar: true,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
          });
          return false;
        }
      });
    } else {
      fetch(
        `${BACKEND_URL}/registration/walker/email-available/?email=${this.state.user.email}`,
        options
      ).then((response) => {
        if (response.status === 400) {
          toast.error("❌ Email se već koristi!", {
            position: "top-right",
            autoClose: 5000,
            hideProgressBar: true,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
          });
          return false;
        }
      });
    }
    const expression = /\S+@\S+/;
    var checkAt = expression.test(String(this.state.user.email).toLowerCase());
    console.log(checkAt);
    /** if (this.state.email !== "" && this.state.email.len != 0 && !checkAt) {
      toast.error("❌ Email treba sadržavati znak @!", {
        position: "top-right",
        autoClose: 5000,
        hideProgressBar: true,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
      });
      return false;
    }
     */
    return true;
  };

  oibCheck = (event) => {
    const options = {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        Authorization: authHeader(),
      },
    };
    fetch(
      `${BACKEND_URL}/registration/association/oib-available/?oib=${this.state.user.oib}`,
      options
    ).then((response) => {
      if (response.status === 400) {
        toast.error("❌ OIB se već koristi!", {
          position: "top-right",
          autoClose: 5000,
          hideProgressBar: true,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: true,
          progress: undefined,
        });
        return false;
      }
    });
    if (this.state.user.oib.length !== 0 && this.state.user.oib.length !== 11) {
      toast.error("❌ OIB mora biti duljine 11!", {
        position: "top-right",
        autoClose: 5000,
        hideProgressBar: true,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
      });
      return false;
    }
    return true;
  };

  checkNumber = () => {
    if (
      this.state.user.phoneNumber !== null &&
      this.state.user.phoneNumber.length !== 0
    ) {
      if (!/^\d+$/.test(this.state.user.phoneNumber)) {
        toast.error("❌ Telefonski broj smije sadržavati samo brojeve!", {
          position: "top-right",
          autoClose: 5000,
          hideProgressBar: true,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: true,
          progress: undefined,
        });
        return false;
      }
      if (
        this.state.user.phoneNumber.length < 9 ||
        this.state.user.phoneNumber.length > 10
      ) {
        toast.error("❌ Telefonski broj mora imati 9 ili 10 znamenaka!", {
          position: "top-right",
          autoClose: 5000,
          hideProgressBar: true,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: true,
          progress: undefined,
        });
        return false;
      }
    }
    return true;
  };

  find(array, key) {
    for (var i = 0; i < array.length; i++) {
      if (array[i][key] !== undefined) {
        return array[i];
      }
    }
    return null;
  }

  onSubmit = (e) => {
    e.preventDefault();

    console.log(this.state.user);

    const fromData = this.state.user;

    const dataUser = {
      id: fromData.id,
      firstName: fromData.firstName,
      lastName: fromData.lastName,
      username: fromData.username,
      password: fromData.password,
      email: fromData.email,
      // phoneNumber: fromData.phoneNumber,
      // publicStats: true,
    };
    const dataAssociation = {
      id: fromData.id,
      oib: fromData.oib,
      name: fromData.name,
      firstName: fromData.firstName,
      lastName: fromData.lastName,
      username: fromData.username,
      password: fromData.password,
      email: fromData.email,
      // webAddress: fromData.webAddress,
      // description: fromData.description,
      // pictureUrl: fromData.pictureUrl,
      // phoneNumber: fromData.phoneNumber,
    };

    console.log(dataUser);
    console.log(dataAssociation);

    var errors = [];
    //firstname
    if (this.state.title.udrugaSelected && !this.oibCheck()) {
      errors.push("oib");
    }

    if (this.state.title.udrugaSelected && this.state.user.name === "") {
      errors.push("name");
    }

    if (this.state.user.username === "") {
      errors.push("username");
    }

    if (this.state.user.firstName === "") {
      errors.push("firstName");
    }

    if (this.state.user.lastName === "") {
      errors.push("lastName");
    }

    if (!this.emailCheck()) {
      errors.push("email");
    }

    if (!this.checkPasswordLength()) {
      errors.push("password");
    }

    // if (this.state.title.udrugaSelected && this.state.user.description === "") {
    //   errors.push("desc");
    // }

    const { error } = this.state.errors;
    this.setState({
      error: {
        ...error,
        errors: errors,
      },
    });

    if (errors.length > 0) {
      return false;
    }

    if (this.state.title.setacSelected) {
      const options1 = {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: authHeader(),
        },
        body: JSON.stringify(dataUser),
      };
      fetch(`${BACKEND_URL}/registration/walker`, options1).then((response) => {
        if (response.status === 400) {
          toast.error("❌ Registracija nije moguća!", {
            position: "top-right",
            autoClose: 5000,
            hideProgressBar: true,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
          });
        }
        response
          .json()
          .then((json) => {
            console.log(json);
            dataUser.id = json;
          })
          .then((data) => {
            this.props.dispatch({
              ...dataUser,
              // token: data.token,
              userType: "korisnik",
            });

            localStorage.setItem(
              "user",
              JSON.stringify({
                userId: dataUser.id,
                ...{
                  authdata: window.btoa(
                    `${fromData.username}:${fromData.password}`
                  ),
                },
              })
            );

            this.props.history.push("/home");

            toast.success("🐶 Dobrodošli!", {
              position: "top-right",
              autoClose: 5000,
              hideProgressBar: false,
              closeOnClick: true,
              pauseOnHover: true,
              draggable: true,
              progress: undefined,
            });
            console.log(dataUser);
          });
      });
    } else {
      const options2 = {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: authHeader(),
        },
        body: JSON.stringify(dataAssociation),
      };
      fetch(`${BACKEND_URL}/registration/association`, options2).then(
        (response) => {
          if (response.status === 400) {
            toast.error("❌ Registracija nije moguća!", {
              position: "top-right",
              autoClose: 5000,
              hideProgressBar: true,
              closeOnClick: true,
              pauseOnHover: true,
              draggable: true,
              progress: undefined,
            });
          }
          response
            .json()
            .then((json) => {
              dataAssociation.id = json;
            })
            .then((data) => {
              this.props.dispatch({
                ...dataAssociation,
                //token: data.token,
                userType: "udruga",
              });
              localStorage.setItem(
                "user",
                JSON.stringify({
                  userId: dataAssociation.id,
                  ...{
                    authdata: window.btoa(
                      `${fromData.username}:${fromData.password}`
                    ),
                  },
                })
              );
              this.props.history.push("/home");

              toast.success("🐶 Dobrodošli!", {
                position: "top-right",
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
              });
              console.log(dataAssociation);
            });
        }
      );
      //ned
      //alje
    }
  };
  render() {
    return (
      <div className="Registration">
        <div className="d-flex flex-column bg justify-content-between">
          <form className="needs-validation">
            <div className="next text-center mx-auto">
              <div className="lead rounded mx-auto white-container justify-content-center">
                <div className="form-reg center backg justify-content-center mx-auto">
                  <div className="mx-auto mt-2 text-center">
                    <label>
                      <strong className="title-log">REGISTRACIJA</strong>
                    </label>
                  </div>
                  <div className="mx-auto mt-2 text-center">
                    <label>
                      <span className="setac" onClick={this.setacClick}>
                        ŠETAČ
                      </span>
                      {"  "}| {"  "}
                      <span className="udruga" onClick={this.udrugaClick}>
                        UDRUGA
                      </span>
                    </label>
                  </div>
                  <div
                    className="mx-auto text-center col-sm-8 my-2 d-none"
                    id="oib"
                  >
                    <input
                      className={
                        this.find(this.state.errors, "oib") !== null
                          ? "form-control is-invalid"
                          : "form-control"
                      }
                      name="oib"
                      type="text"
                      placeholder="Upišite oib udruge"
                      onChange={this.onChange}
                      value={this.state.user.oib}
                      onBlur={this.oibCheck}
                      required
                    ></input>
                  </div>
                  <div
                    className="mx-auto text-center col-sm-8 my-2 d-none"
                    id="ana"
                  >
                    <input
                      className={
                        this.find(this.state.errors, "ana") !== null
                          ? "form-control is-invalid"
                          : "form-control"
                      }
                      name="name"
                      type="text"
                      placeholder="Upišite naziv udruge"
                      onChange={this.onChange}
                      value={this.state.user.name}
                      required
                    ></input>
                  </div>
                  <div className="mx-auto text-center col-sm-8 my-2" id="fna">
                    <input
                      className={
                        this.find(this.state.errors, "firstName") !== null
                          ? "form-control is-invalid"
                          : "form-control"
                      }
                      name="firstName"
                      type="text"
                      placeholder="Upišite svoje ime"
                      onChange={this.onChange}
                    ></input>
                  </div>
                  <div className="mx-auto text-center col-sm-8 my-2" id="lna">
                    <input
                      className={
                        this.find(this.state.errors, "lastName") !== null
                          ? "form-control is-invalid"
                          : "form-control"
                      }
                      name="lastName"
                      type="text"
                      placeholder="Upišite svoje prezime"
                      onChange={this.onChange}
                      value={this.state.user.lastName}
                    ></input>
                  </div>
                  <div className="mx-auto text-center col-sm-8 my-2" id="use">
                    <input
                      className={
                        this.find(this.state.errors, "username") !== null
                          ? "form-control is-invalid"
                          : "form-control"
                      }
                      name="username"
                      type="text"
                      rules={[{ required: true }]}
                      placeholder="Upišite korisničko ime"
                      onChange={this.onChange}
                      onBlur={this.usernameCheck}
                      value={this.state.user.username}
                      required
                      isValid="false"
                    ></input>
                  </div>
                  <div className="mx-auto text-center col-sm-8 my-2" id="pas">
                    <input
                      className={
                        this.find(this.state.errors, "password") !== null
                          ? "form-control is-invalid"
                          : "form-control"
                      }
                      name="password"
                      type="password"
                      placeholder="Upišite lozinku"
                      onChange={this.onChange}
                      value={this.state.user.password}
                      onBlur={this.checkPasswordLength}
                      required
                    ></input>
                  </div>
                  <div className="mx-auto text-center col-sm-8 my-2" id="cpa">
                    <input
                      className={
                        this.checkPassword()
                          ? "form-control is-invalid"
                          : "form-control"
                      }
                      name="confirmPassword"
                      placeholder="Potvrdite svoju lozinku"
                      type="password"
                      onChange={this.onChange}
                      value={this.state.user.confirmPassword}
                      required
                    ></input>
                  </div>
                  <div className="mx-auto text-center col-sm-8 my-2" id="ema">
                    <input
                      className={
                        this.find(this.state.errors, "email") !== null
                          ? "form-control is-invalid"
                          : "form-control"
                      }
                      name="email"
                      type="email"
                      placeholder="Upišite svoj email"
                      onChange={this.onChange}
                      onBlur={this.emailCheck}
                      value={this.state.user.email}
                      required
                    ></input>
                  </div>
                  {/* <div
                    className="mx-auto text-center col-sm-8 my-2 d-none"
                    id="web"
                  >
                    <input
                      className="form-control"
                      name="webAddress"
                      type="url"
                      placeholder="Upišite web-adresu udruge"
                      onChange={this.onChange}
                      value={this.state.user.webAddress}
                    ></input>
                  </div> */}
                  {/* <div className="mx-auto text-center col-sm-8 my-2" id="pho">
                    <input
                      className={
                        this.find(this.state.errors, "phone") !== null
                          ? "form-control is-invalid"
                          : "form-control"
                      }
                      name="phoneNumber"
                      type="text"
                      placeholder="Upišite svoj kontaktni broj"
                      onChange={this.onChange}
                      value={this.state.user.phoneNumber}
                      onBlur={this.checkNumber}
                    ></input>
                  </div> */}
                  {/* <div
                    className="form-group mx-auto text-center col-sm-8 my-2 d-none id"
                    id="desc"
                  >
                    <textarea
                      placeholder="Upišite opis udruge"
                      className={
                        this.find(this.state.errors, "desc") !== null
                          ? "form-control is-invalid"
                          : "form-control"
                      }
                      id="exampleFormControlTextarea1"
                      rows="3"
                      name="description"
                      onChange={this.onChange}
                      type="text"
                      value={this.state.user.description}
                    ></textarea>
                  </div> */}

                  <button
                    className="btn mt-2 text-align text-center btn-dark"
                    style={{ border: "1px solid black" }}
                    float="center"
                    type="submit"
                    onClick={this.onSubmit}
                    disabled={this.checkPassword()}
                  >
                    REGISTRACIJA
                  </button>
                  <div className="mx-auto text-center col-auto col-sm-8 my-2 justify-content-center">
                    <span className="space other-text">Već ste korisnik?</span>
                    <a
                      className="btn btn-dark mt-2 text-align text-center"
                      href="/login"
                      role="button"
                    >
                      PRIJAVA
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
    dispatch: (user) => dispatch(createUser(user)),
  };
};

export default withRouter(connect(null, mapDispatchToProps)(Registration));
