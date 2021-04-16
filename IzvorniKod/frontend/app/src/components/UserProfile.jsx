import React, { useState, setState } from "react";
import "./../static/userprofile.css";
import avatar from "./../static/avatardoggo.png";
import { withRouter } from "react-router-dom";
import { connect } from "react-redux";
import { logoutUser } from "../redux/actions";
import Tabs from "react-bootstrap/Tabs";
import Tab from "react-bootstrap/Tab";
import Calendar from "react-calendar";
import "react-calendar/dist/Calendar.css";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { Button as ButtonA, Popconfirm } from "antd";
import { authHeader } from "./../helpers/authHeader";

import ReservationTable from "./ReservationTable";
//const BACKEND_URL = "http://localhost:8080";
const BACKEND_URL = "https://exception-doggo-backend-dev.herokuapp.com";
const USER_PATH_ID = "/user/:id";
const USER_PATH_PROFILE = "/userprofile";

class UserProfile extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      date: new Date(),
      user: {
        id: "",
        username: "",
        firstName: "",
        lastName: "",
        email: "",
        phoneNumber: "",
        publicStats: "",
      },
      editUser: {
        id: "",
        username: "",
        firstName: "",
        lastName: "",
        email: "",
        phoneNumber: "",
        publicStats: "",
      },
      reservations: [],
      statistics: {},
      rezervacije: [],
    };
    this.onChange = this.onChange.bind(this);
    this.editContactOnClick = this.editContactOnClick.bind(this);
    this.editPasswordOnClick = this.editPasswordOnClick.bind(this);
    this.onCancel = this.onCancel.bind(this);
    this.downloadFile = this.downloadFile.bind(this);
  }

  onChange = (event) => {
    //console.log(event);
    if (event.target === undefined) {
      this.setState({
        date: event,
      });
      this.updateReservations(event);
      return true;
    }
    const { name, value } = event.target;
    this.setState({
      editUser: {
        [name]: value,
      },
    });
  };

  componentDidMount() {
    const id = this.props.location.search.split("=")[1];
    var extension = "";
    if (
      this.props.match.path === USER_PATH_PROFILE &&
      this.props.userType !== "admin"
    )
      extension = this.props.id;
    else extension = id;
    const options = {
      method: "GET",
      headers: {
        "Content-Type": "application/json; charset=UTF-8",
        Authorization: authHeader(),
      },
    };
    console.log(options);
    fetch(`${BACKEND_URL}/walker?walkerId=${extension}`, options).then(
      (response) => {
        response.json().then((json) => {
          this.setState({
            user: {
              id: json.id,
              username: json.username,
              firstName: json.firstName,
              lastName: json.lastName,
              email: json.email,
              phoneNumber: json.phoneNumber,
              publicStats: json.publicStats,
            },
            reservations: [],
            statistics: {},
          });
          this.setState({
            editUser: this.state.user,
          });
          this.updateStatistics();
        });
      }
    );
    this.updateReservations(this.state.date);
  }

  updateReservations(date) {
    const options = {
      method: "GET",
      headers: {
        "Content-Type": "application/json; charset=UTF-8",
        Authorization: authHeader(),
      },
    };
    console.log(date);
    this.setState({
      reservations: [],
    });
    this.props.userType !== "admin"
      ? fetch(
          `${BACKEND_URL}/reservations/get/bywalker?walkerId=${this.props.id}`,
          options
        ).then((response) =>
          response.json().then((json) => {
            var day =
              date.getDate() >= 1 && date.getDate() <= 9
                ? "0" + date.getDate()
                : date.getDate();
            var month =
              date.getMonth() + 1 >= 1 && date.getMonth() + 1 <= 9
                ? "0" + (date.getMonth() + 1)
                : date.getMonth() + 1;
            //console.log(date.getFullYear() + "-" + );
            var datum = date.getFullYear() + "-" + month + "-" + day;
            console.log("datum:" + datum);
            this.setState({
              reservations: json.filter(
                (reservation) => reservation.date === datum
              ),
            });
            this.setState({
              rezervacije: json,
            });
            console.log(this.state.reservations);
          })
        )
      : fetch(
          `${BACKEND_URL}/reservations/get/bywalker?walkerId=${
            this.props.location.search.split("=")[1]
          }`,
          options
        ).then((response) =>
          response.json().then((json) => {
            var day =
              date.getDate() >= 1 && date.getDate() <= 9
                ? "0" + date.getDate()
                : date.getDate();
            var month =
              date.getMonth() + 1 >= 1 && date.getMonth() + 1 <= 9
                ? "0" + (date.getMonth() + 1)
                : date.getMonth() + 1;
            var datum = date.getFullYear() + "-" + month + "-" + day;
            console.log(datum);
            this.setState({
              reservations: json.filter(
                (reservation) => reservation.date === datum
              ),
            });
            this.setState({
              rezervacije: json,
            });
            console.log(this.state.reservations);
          })
        );
  }

  updateStatistics() {
    const options = {
      method: "GET",
      headers: {
        "Content-Type": "application/json; charset=UTF-8",
        Authorization: authHeader(),
      },
    };
    this.props.userType !== "admin"
      ? fetch(
          `${BACKEND_URL}/walker/ranking/byid?walkerId=${this.props.id}`,
          options
        ).then((response) =>
          response.json().then((json) => {
            this.setState({
              statistics: json,
            });
          })
        )
      : fetch(
          `${BACKEND_URL}/walker/ranking/byid?walkerId=${
            this.props.location.search.split("=")[1]
          }`,
          options
        ).then((response) =>
          response.json().then((json) => {
            this.setState({
              statistics: json,
            });
            console.log(this.state);
          })
        );
  }

  editProfile() {
    document.getElementById("view1").classList.toggle("d-none");
    document.getElementById("view2").classList.toggle("d-none");
    document.getElementById("edit").classList.toggle("d-none");
  }

  onCancel() {
    document.getElementById("public").value = this.state.user.publicStats;
    document.getElementById("trenutna").value = "";
    document.getElementById("nova").value = "";
    document.getElementById("potvrdanove").value = "";
    this.setState({
      editUser: this.state.user,
    });
    this.editProfile();
  }

  editContactOnClick() {
    var username = document.getElementById("username").value;
    var firstName = document.getElementById("ime").value;
    var lastName = document.getElementById("prezime").value;
    var email = document.getElementById("email").value;
    var phoneNumber = document.getElementById("phoneNumber").value;
    var publicStats = document.getElementById("public").value;
    var password = document.getElementById("edit-pass").value;
    console.log(password);

    if (username === "") {
      username = null;
    }
    if (!this.usernameCheck) {
      return false;
    }

    console.log(username);

    if (firstName === "") {
      firstName = null;
    }
    if (lastName === "") {
      lastName = null;
    }

    if (email === "") {
      email = null;
    }
    if (!this.emailCheck) {
      return false;
    }

    if (phoneNumber === "") {
      phoneNumber = null;
    }
    if (publicStats === this.state.user.publicStats) {
      publicStats = this.state.user.publicStats;
    }

    let contactData = {
      id: this.state.user.id,
      username: username,
      firstName: firstName,
      lastName: lastName,
      email: email,
      phoneNumber: phoneNumber,
      publicStats: publicStats,
    };

    if (contactData.username == "" || contactData.username == null)
      contactData.user = this.state.user.username;
    console.log(contactData);

    const options = {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: authHeader(),
          },
          body: JSON.stringify(contactData),
        };

    fetch(`${BACKEND_URL}/walker/edit-profile`, options).then((response) => {
      if (response.status == 400 || response.status == 401) {
        toast.error("❌ Vaša promjena podataka nije uspjela!", {
          position: "top-right",
          autoClose: 5000,
          hideProgressBar: true,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: true,
          progress: undefined,
        });
      } else {
        console.log("uspješna promjena profilnih podataka");
        this.setState({
          user: this.state.editUser,
        });
        if (this.props.userType !== "admin") {
        localStorage.setItem(
              "user",
              JSON.stringify({
                userId: contactData.id,
                ...{
                  authdata: window.btoa(`${contactData.username}:${password}`),
                },
              })
            );
        }
        this.onCancel();
        this.componentDidMount();
        this.render();
      }
    });
  }

  usernameCheck = () => {
    var username = document.getElementById("username").value;
    if (username === this.state.user.username) {
      console.log("isti username");
      return false;
    }
    const options = {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        Authorization: authHeader(),
      },
    };
    fetch(
      `${BACKEND_URL}/registration/walker/username-available/?username=${username}`,
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
  };

  emailCheck = () => {
    var email = document.getElementById("email").value;
    if (email === this.state.user.email) {
      console.log("isti email");
      return false;
    }
    const options = {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        Authorization: authHeader(),
      },
    };
    fetch(
      `${BACKEND_URL}/registration/walker/email-available/?email=${email}`,
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
  };

  editPasswordOnClick() {
    var trenutna = document.getElementById("trenutna").value;
    var nova = document.getElementById("nova").value;
    var potvrdanove = document.getElementById("potvrdanove").value;

    console.log(trenutna + " " + nova + " " + potvrdanove + " ");

    if (!this.checkPassword(nova, potvrdanove)) {
      toast.error("❌ Polja nove lozinke i njezine potvrde nisu ista.", {
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

    const passwordData = {
      id: this.state.user.id,
      oldPassword: trenutna,
      newPassword: nova,
    };
    console.log(passwordData);

    const options = {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: authHeader(),
      },
      body: JSON.stringify(passwordData),
    };
    fetch(`${BACKEND_URL}/walker/edit-password`, options).then((response) => {
      if (response.status === 400) {
        toast.error("❌ Unijeli ste krivu trenutnu lozinku!", {
          position: "top-right",
          autoClose: 5000,
          hideProgressBar: true,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: true,
          progress: undefined,
        });
      } else {
        console.log("uspjeh");
        if (this.props.userType !== "admin") {
            localStorage.setItem(
            "user",
            JSON.stringify({
                userId: this.state.user.id,
                ...{
                 authdata: window.btoa(`${this.state.user.username}:${nova}`),
             },
            })
            );
        }
        this.onCancel();
        this.componentDidMount();
        this.render();
      }
    });
  }

  checkPassword = (nova, potvrdanove) => {
    return nova === potvrdanove;
  };

  checkPasswordLength = () => {
    var pass = document.getElementById("nova").value;
    //console.log(pass);
    if (pass !== undefined && (pass.length < 6 || pass.length > 20)) {
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

  deleteHandler = (id) => {
    console.log(id);
    fetch(`${BACKEND_URL}/walker/delete?walkerId=${id}`, {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json; charset=UTF-8",
        Authorization: authHeader(),
      },
      //body: JSON.stringify(this.state.initialValues),
    })
      .then((data) => {
        if (data.error) {
          console.log("data", data);
          alert(data.message);
        } else {
          window.location.reload();
        }
      })
      .catch((err) => {
        console.log(err);
      });
    this.props.dispatch();
  };

  downloadFile() {
    const options = {
      method: "GET",
      headers: {
        "Content-Type": "application/json; charset=UTF-8",
        Authorization: authHeader(),
      },
    };
    this.props.userType !== "admin"
      ? fetch(
          `${BACKEND_URL}/walker/schedule?walkerId=${this.props.id}`,
          options
        ).then((response) => {
          response.blob().then((blob) => {
            console.log(blob);
            var objectURL = URL.createObjectURL(blob);
            const link = document.createElement("a");
            link.href = objectURL;
            link.setAttribute("download", "pdfFileName");
            document.body.appendChild(link);
            link.click();
          });
        })
      : fetch(
          `${BACKEND_URL}/walker/schedule?walkerId=${
            this.props.location.search.split("=")[1]
          }`,
          options
        ).then((response) => {
          response.blob().then((blob) => {
            console.log(blob);
            var objectURL = URL.createObjectURL(blob);
            const link = document.createElement("a");
            link.href = objectURL;
            link.setAttribute("download", "pdfFileName");
            document.body.appendChild(link);
            link.click();
          });
        });
  }

  render() {
    if (!this.props.logged) {
      this.props.history.push("/login");
    }
    return (
      <section className="section about-section gray-bg" id="about">
        <div className="container">
          <div className="row align-items-center flex-row-reverse" id="view1">
            <div className="col-lg-3">
              <div className="calendar">
                <Calendar
                  onChange={this.onChange}
                  value={this.state.date}
                  name="calendar"
                />
              </div>
            </div>
            <div className="col-lg-6">
              <div className="about-text go-to">
                <div className="row align-items-center">
                  <div className="col-lg-6">
                    <h3 className="dark-color">
                      {this.state.editUser.username}
                    </h3>
                  </div>
                  <div className="col-lg-6">
                    <h6 className="datum">
                      Popis rezervacija za{" "}
                      {this.state.date.getDate() +
                        "." +
                        (this.state.date.getMonth() + 1) +
                        "." +
                        this.state.date.getFullYear() +
                        "."}
                    </h6>
                    {this.state.reservations !== undefined &&
                    this.state.reservations.length > 0 ? (
                      this.state.reservations.map((reservation) =>
                        reservation.dogs.map((dog) => (
                          <div
                            className="reservations"
                            id={reservation.reservationId + dog.dogId}
                            key={reservation.reservationId + dog.dogId}
                          >
                            <label>
                              {reservation.startTime.substring(0, 5) +
                                "-" +
                                reservation.returnTime.substring(0, 5) +
                                " " +
                                dog.name +
                                " " +
                                "(" +
                                dog.associationName +
                                ")"}
                            </label>
                          </div>
                        ))
                      )
                    ) : (
                      <div>
                        <p>Nemate rezervacija danas.</p>
                      </div>
                    )}
                  </div>
                </div>
                <div className="row about-list">
                  <div className="col-lg-6">
                    <div className="media">
                      <label>Ime</label>
                      <p>{this.state.editUser.firstName}</p>
                    </div>
                    <div className="media">
                      <label>E-mail</label>
                      <p>{this.state.editUser.email}</p>
                    </div>
                  </div>
                  <div className="col-lg-6">
                    <div className="media">
                      <label>Prezime</label>
                      <p>{this.state.editUser.lastName}</p>
                    </div>
                    <div className="media">
                      <label>Kontakt</label>
                      <p>{this.state.editUser.phoneNumber}</p>
                    </div>
                  </div>
                </div>
                <div className="row align-items-center">
                  <div className="col-lg-6">
                    <button
                      className="btn btn-dark mt-2 text-align text-center"
                      onClick={this.editProfile}
                    >
                      UREDI PROFIL
                    </button>
                    <button
                      className="btn btn-dark mt-2 text-align text-center"
                      onClick={this.downloadFile}
                    >
                      RASPORED
                    </button>
                  </div>
                  <div className="col-lg-6">
                    <span className="text-muted">
                      *
                      {this.state.editUser.publicStats
                        ? "Vaše podatke o šetnjama javno mogu vidjeti svi korisnici."
                        : "Vaši podaci o šetnjama vidljivi su samo Vama."}
                    </span>
                  </div>
                </div>
              </div>
            </div>
            <div className="col-lg-3">
              <div className="about-avatar">
                <img
                  src={avatar}
                  height="260px"
                  width="250px"
                  title=""
                  alt=""
                />
              </div>
            </div>
          </div>
          <div className="counter " id="view2">
            <div className="row">
              <div className="col-6 col-lg-4">
                <div className="count-data text-center">
                  <h6 className="count h2" data-to="500" data-speed="500">
                    {this.statistics === undefined
                      ? this.state.statistics.totalDuration * 60
                      : 0}
                  </h6>
                  <p className="m-0px font-w-600">Prošetanih Minuta</p>
                </div>
              </div>
              <div className="col-6 col-lg-4">
                <div className="count-data text-center">
                  <h6 className="count h2" data-to="150" data-speed="150">
                    {this.statistics === undefined
                      ? this.state.statistics.numOfDogs
                      : 0}
                  </h6>
                  <p className="m-0px font-w-600">Prošetanih Pasa</p>
                </div>
              </div>
              <div className="col-6 col-lg-4">
                <div className="count-data text-center">
                  <h6 className="count h2" data-to="850" data-speed="850">
                    {this.statistics === undefined
                      ? this.state.statistics.numOfWalks
                      : 0}
                  </h6>
                  <p className="m-0px font-w-600">Prošetanih Šetnji</p>
                </div>
              </div>
            </div>
          </div>
          <div className="well align-items-center d-none" id="edit">
            <Tabs defaultActiveKey="profile" id="uncontrolled-tab">
              <Tab eventKey="profile" title="Profil">
                <form className="edit-form-control">
                  <label>Ime</label>
                  <input
                    id="ime"
                    type="text"
                    value={this.state.editUser.firstName}
                    name="firstName"
                    onChange={this.onChange}
                  />
                  <label>Prezime</label>
                  <input
                    id="prezime"
                    type="text"
                    value={this.state.editUser.lastName}
                    name="lastName"
                    onChange={this.onChange}
                  />
                  <label>Korisničko ime</label>
                  <input
                    id="username"
                    type="text"
                    value={this.state.editUser.username}
                    onBlur={this.usernameCheck}
                    name="username"
                    onChange={this.onChange}
                  />
                  <label>E-mail</label>
                  <input
                    id="email"
                    type="text"
                    value={this.state.editUser.email}
                    onBlur={this.emailCheck}
                    name="email"
                    onChange={this.onChange}
                  />
                  <label>Kontakt</label>
                  <input
                    id="phoneNumber"
                    type="text"
                    value={this.state.editUser.phoneNumber}
                    name="phoneNumber"
                    onChange={this.onChange}
                  />
                  <label>Javnost podataka</label>
                  <select
                    name="publicStats"
                    id="public"
                    defaultValue={this.state.editUser.publicStats}
                  >
                    <option value="true">Javno</option>
                    <option value="false">Privatno</option>
                  </select>

                  <label  className="promijeni-sifru">
                  UPIŠITE LOZINKU!</label>
                  <input required id="edit-pass" type="password" />
                </form>
                <button
                  className="btn btn-dark mt-2 text-align text-center"
                  type="submit"
                  onClick={this.onCancel}
                >
                  ODUSTANI
                </button>
                <button
                  className="btn btn-dark mt-2 text-align text-center"
                  type="submit"
                  onClick={this.editContactOnClick}
                >
                  SPREMI PROMJENE
                </button>
                <div className="buttonA">
                  <Popconfirm
                    placement="topRight"
                    title={
                      "Jeste li sigurni da želite obrisati profil?\n Ova radnja se ne može poništiti!"
                    }
                    onConfirm={(e) => this.deleteHandler(this.state.user.id, e)}
                    okText="Da"
                    cancelText="Ne"
                  >
                    <ButtonA type="danger">OBRIŠI PROFIL</ButtonA>
                  </Popconfirm>
                </div>
              </Tab>
              <Tab eventKey="password" title="Lozinka">
                <form className="edit-form-control">
                  <label>Upišite trenutnu lozinku</label>
                  <input id="trenutna" type="password" />
                  <label>Upišite novu lozinku</label>
                  <input
                    id="nova"
                    type="password"
                    onBlur={this.checkPasswordLength}
                  />
                  <label>Potvrdite novu lozinku</label>
                  <input id="potvrdanove" type="password" />
                </form>
                <button
                  className="btn btn-dark mt-2 text-align text-center"
                  type="submit"
                  onClick={this.onCancel}
                >
                  ODUSTANI
                </button>
                <button
                  className="btn btn-dark mt-2 text-align text-center"
                  type="submit"
                  onClick={this.editPasswordOnClick}
                >
                  SPREMI PROMJENE
                </button>
              </Tab>
            </Tabs>
          </div>
        </div>
        <div style={{ margin: "5%" }}>
          <h1> Sve rezervacije</h1>
          {this.state.rezervacije !== undefined &&
          this.state.rezervacije.length > 0 ? (
            <ReservationTable rows={this.state.rezervacije}></ReservationTable>
          ) : (
            "Nemate rezerviranih šetnji 😢"
          )}
        </div>
        <ToastContainer />
      </section>
    );
  }
}

const mapStateToprops = (state) => {
  return {
    id: state.user.id,
    firstName: state.user.firstName,
    lastName: state.user.lastName,
    username: state.user.username,
    email: state.user.email,
    phoneNumber: state.user.phoneNumber,
    publicStats: state.user.publicStats,
    logged: state.user.logged,
    userType: state.user.userType,
  };
};

const mapDispatchToProps = (dispatch) => {
  return {
    dispatch: () => dispatch(logoutUser()),
  };
};

export default withRouter(
  connect(mapStateToprops, mapDispatchToProps)(UserProfile)
);
