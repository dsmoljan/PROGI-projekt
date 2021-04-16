import React from "react";
import "./../static/userprofile.css";
import "./../static/adminProfile.css";
import "./../static/associationprofile.css";
import { withRouter } from "react-router-dom";
import { connect } from "react-redux";
import Calendar from "react-calendar";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TableContainer from "@material-ui/core/TableContainer";
import TableRow from "@material-ui/core/TableRow";
import Button from "@material-ui/core/Button";
import { Tabs, Tab, TabPanel, TabList } from "react-tabs";
import DogEditForm from "./dog/DogEditForm";
import defaultDog from "./../static/dog.jpg";
import EditAvailability from "./dog/EditAvailabilityForm";
import ReservationTable from "./ReservationTable";
import { authHeader } from "./../helpers/authHeader";

//const BACKEND_URL = "http://localhost:8080";
const BACKEND_URL = "https://exception-doggo-backend-dev.herokuapp.com";
const ASSOCIATION_PATH_ID = "/association/:id";
const ASSOCIATION_PATH_PROFILE = "/associationprofile";

const udrugeEg = [
  {
    id: "1",
    name: "John Brown",
    age: 32,
    address: "New York No. 1 Lake Park",
  },
  {
    id: "2",
    name: "Joe Black",
    age: 42,
    address: "London No. 1 Lake Park",
  },
];

class AdminProfile extends React.Component {
  constructor(props) {
    super(props);
    this.showModal = this.showModal.bind(this);
    this.hideModal = this.hideModal.bind(this);
    this.onChange = this.onChange.bind(this);
    this.onCancel = this.onCancel.bind(this);
    this.state = {
      date: new Date(),
      udruge: udrugeEg,
      korisnici: [],
      psi: [],
      rezervacije: [],
      statKorisnici: 0,
      statUdruge: 0,
      statPsi: 0,
      visibleEdit: false,
      availabilitySet: [],
      availabilitySetEdit: [],
      visibleAvailability: false,
      visibleReservation: false,
      groupDogs: "",
      open: false,
      association: {
        id: "",
        oib: "",
        name: "",
        firstName: "",
        lastName: "",
        username: "",
        email: "",
        webAddress: "",
        description: "",
        pictureURL: "",
        phoneNumber: "",
        deleted: "",
        dogList: [],
        associationLocationPreview: {},
      },
      dogs: [
        {
          dogId: "1",
          picture: null,
          name: "pesek",
        },
      ],
    };
  }

  componentDidMount() {
    // this.updateReservations();
    // this.updateStatistics();
    console.log(this.props);
    const requestOptions = {
      method: "GET",
      headers: {
        "Content-Type": "application/json; charset=UTF-8",
        Authorization: authHeader(),
      },
    };
    fetch(`${BACKEND_URL}/walker/number-of-walkers`, requestOptions).then(
      (response) => {
        response.json().then((json) => {
          this.setState({
            statKorisnici: json,
          });
        });
      }
    );

    fetch(
      `${BACKEND_URL}/association/number-of-all-associations`,
      requestOptions
    ).then((response) => {
      response.json().then((json) => {
        this.setState({
          statUdruge: json,
        });
      });
    });

    fetch(`${BACKEND_URL}/dog/number-of-all-dogs`, requestOptions).then(
      (response) => {
        response.json().then((json) => {
          this.setState({
            statPsi: json,
          });
        });
      }
    );

    fetch(`${BACKEND_URL}/association/all`, requestOptions).then((response) => {
      response.json().then((json) => {
        this.setState({
          udruge: json,
        });
      });
    });
    fetch(`${BACKEND_URL}/walker/all`, requestOptions).then((response) => {
      response.json().then((json) => {
        this.setState({
          korisnici: json,
        });
      });
    });
    fetch(`${BACKEND_URL}/dog/all`, requestOptions).then((response) => {
      response.json().then((json) => {
        this.setState({
          psi: json,
        });
      });
    });
    fetch(`${BACKEND_URL}/reservations/all`, requestOptions).then(
      (response) => {
        response.json().then((json) => {
          this.setState({
            rezervacije: json,
          });
        });
      }
    );
  }

  onCancel() {
    document.getElementById("lozinka").value = "";
    document.getElementById("nova").value = "";
    document.getElementById("potvrdanove").value = "";
    this.setState({
      editAssociation: this.state.association,
    });
    this.editProfile();
  }

  onChange(clickedDate) {
    this.setState({ date: clickedDate });
  }

  seeGroupDogs() {
    this.setState({ open: !this.state.open });
  }

  render() {
    console.log(this.state.psi);
    if (!this.props.logged) {
      this.props.history.push("/login");
    }
    return (
      <div>
        <section className="section about-section gray-bg" id="about">
          <div className="container">
            <div className="row align-items-start flex-row-reverse" id="view1">
              <div className="col-lg-3">
                <div className="calendar">
                  <Calendar
                    onChange={this.onChange}
                    value={this.state.date}
                    // onClick={this.updateReservations()}
                  />
                </div>
              </div>
              <div className="col-lg-6">
                <h1 className="admin-text">
                  {" "}
                  DogGO
                  <br></br>
                  <span className="bigger-text">ADMIN</span>
                </h1>
              </div>
              <div className="col-lg-3">
                <div className="about-avatar">
                  <img
                    src="https://bootdey.com/img/Content/avatar/avatar7.png"
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
                      {this.state.statKorisnici}
                    </h6>
                    <p className="m-0px font-w-600">Aktivnih Korisnika</p>
                  </div>
                </div>
                <div className="col-6 col-lg-4">
                  <div className="count-data text-center">
                    <h6 className="count h2" data-to="150" data-speed="150">
                      {this.state.statUdruge}
                    </h6>
                    <p className="m-0px font-w-600">Aktivnih Udruga</p>
                  </div>
                </div>
                <div className="col-6 col-lg-4">
                  <div className="count-data text-center">
                    <h6 className="count h2" data-to="850" data-speed="850">
                      {this.state.statPsi}
                    </h6>
                    <p className="m-0px font-w-600">Raspoloživih pasa</p>
                  </div>
                </div>
              </div>
            </div>
            <div></div>
          </div>
        </section>
        <div className="adminChoose">
          <Tabs>
            <TabList>
              <Tab>
                <p>REZERVACIJE</p>
              </Tab>
              <Tab>
                <p></p>KORISNICI
              </Tab>
              <Tab>
                <p>UDRUGE</p>
              </Tab>
              <Tab>
                <p>PSI</p>
              </Tab>
            </TabList>

            <TabPanel>
              <div className="panel-content">
                <TableContainer>
                  <div style={{ margin: "5%" }}>
                    <Table aria-label="custom pagination table">
                      <TableBody>
                        {this.state.rezervacije !== undefined &&
                        this.state.rezervacije.length > 0 ? (
                          <ReservationTable
                            rows={this.state.rezervacije}
                          ></ReservationTable>
                        ) : (
                          <TableCell component="th" scope="row">
                            Nema dostupnih rezervacija
                          </TableCell>
                        )}
                      </TableBody>
                    </Table>
                  </div>
                </TableContainer>
              </div>
            </TabPanel>
            <TabPanel>
              <div className="panel-content">
                <TableContainer>
                  {" "}
                  <div style={{ margin: "5%" }}>
                    <Table aria-label="custom pagination table">
                      <TableBody>
                        {this.state.korisnici !== undefined &&
                        this.state.korisnici.length > 0 ? (
                          this.state.korisnici.map((row) => (
                            <TableRow key={row.id}>
                              <TableCell component="th" scope="row">
                                {row.id}
                              </TableCell>
                              <TableCell component="th" scope="row">
                                {row.username}
                              </TableCell>
                              <TableCell component="th" scope="row">
                                {row.firstName + " " + row.lastName}
                              </TableCell>
                              <TableCell style={{ width: 160 }} align="right">
                                <Button
                                  variant="contained"
                                  color="primary"
                                  href={`userprofile/?id=${row.id}`}
                                >
                                  Detalji
                                </Button>
                              </TableCell>
                            </TableRow>
                          ))
                        ) : (
                          <TableCell component="th" scope="row">
                            Nema dostupnih korisnika
                          </TableCell>
                        )}
                      </TableBody>
                    </Table>
                  </div>
                </TableContainer>
              </div>
            </TabPanel>
            <TabPanel>
              <div className="panel-content">
                <TableContainer>
                  {" "}
                  <div style={{ margin: "5%" }}>
                    <Table aria-label="custom pagination table">
                      <TableBody>
                        {this.state.udruge !== undefined &&
                        this.state.udruge.length > 0 ? (
                          this.state.udruge.map((row) => (
                            <TableRow key={row.id}>
                              <TableCell component="th" scope="row">
                                {row.id}
                              </TableCell>
                              <TableCell component="th" scope="row">
                                {row.name}
                              </TableCell>
                              <TableCell component="th" scope="row">
                                {row.city}
                              </TableCell>
                              <TableCell style={{ width: 160 }} align="right">
                                {row.address}
                              </TableCell>
                              <TableCell style={{ width: 160 }} align="right">
                                <Button
                                  variant="contained"
                                  color="primary"
                                  onClick={async () =>
                                    fetch(
                                      `${BACKEND_URL}/association?associationId=` +
                                        row.id,
                                      {
                                        method: "GET",
                                        headers: {
                                          "Content-Type":
                                            "application/json; charset=UTF-8",
                                          Authorization: authHeader(),
                                        },
                                      }
                                    ).then((response) => {
                                      console.log(response);
                                      if (response.status !== 400) {
                                        response.json().then((json) => {
                                          console.log(json);
                                          this.props.history.push(
                                            "/association/" + row.id
                                          );
                                        });
                                      }
                                    })
                                  }
                                >
                                  Detalji
                                </Button>
                              </TableCell>
                            </TableRow>
                          ))
                        ) : (
                          <TableCell component="th" scope="row">
                            Nema dostupnih udruga
                          </TableCell>
                        )}
                      </TableBody>
                    </Table>
                  </div>
                </TableContainer>
              </div>
            </TabPanel>
            <TabPanel>
              <div className="panel-content">
                <TableContainer>
                  {" "}
                  <div style={{ margin: "5%" }}>
                    <Table aria-label="custom pagination table">
                      <TableBody>
                        {this.state.psi !== undefined &&
                        this.state.psi.length > 0 ? (
                          this.state.psi.map((row) => (
                            <TableRow id={row.dogId} key={row.dogId}>
                              <TableCell component="th" scope="row">
                                {row.dogId}
                              </TableCell>
                              <TableCell component="th" scope="row">
                                {row.picture !== null && row.picture !== "" ? (
                                  <div className="p-2 w-40 bd-highlight">
                                    <img
                                      src={row.picture}
                                      width="100px"
                                      className="rounded-sm ml-n2"
                                    />
                                  </div>
                                ) : (
                                  <div className="p-2 w-40 bd-highlight">
                                    <img
                                      src={defaultDog}
                                      width="100px"
                                      className="rounded-sm ml-n2"
                                    />
                                  </div>
                                )}
                              </TableCell>
                              <TableCell component="th" scope="row">
                                {row.name}
                              </TableCell>
                              <TableCell style={{ width: 160 }} align="right">
                                {row.associationName}
                              </TableCell>
                              <TableCell style={{ width: 160 }} align="right">
                                {((this.props.match.path ===
                                  ASSOCIATION_PATH_PROFILE ||
                                  this.props.id ==
                                    this.props.match.params.id) &&
                                  this.props.userType === "udruga") ||
                                this.props.userType === "admin" ? (
                                  <Button
                                    variant="contained"
                                    color="primary"
                                    //onClick={this.showModalEdit}
                                    onClick={async () =>
                                      fetch(
                                        `${BACKEND_URL}/dog?dogId=${row.dogId}`,
                                        {
                                          method: "GET",
                                          headers: {
                                            "Content-Type":
                                              "application/json; charset=UTF-8",
                                            Authorization: authHeader(),
                                          },
                                        }
                                      ).then((json) => {
                                        json.json().then((response) => {
                                          this.setState({
                                            dogId: response.dogId,
                                            dogName: response.name,
                                            breed: response.breed,
                                            description: response.description,
                                            picture: response.picture,
                                            walkStyle: response.walkStyle,
                                            visibleEdit: true,
                                          });
                                        });
                                      })
                                    }
                                  >
                                    {this.state.visibleEdit === true ? (
                                      <DogEditForm
                                        visible={this.state.visibleEdit}
                                        onClick={this.showModalEdit}
                                        handleClose={this.hideModalEdit}
                                        associationId={
                                          this.state.association.id
                                        }
                                        dogId={this.state.dogId}
                                        dogName={this.state.dogName}
                                        breed={this.state.breed}
                                        description={this.state.description}
                                        picture={this.state.picture}
                                        walkStyle={this.state.walkStyle}
                                      />
                                    ) : (
                                      ""
                                    )}
                                    UREDI
                                  </Button>
                                ) : (
                                  ""
                                )}{" "}
                              </TableCell>
                              <TableCell style={{ width: 160 }} align="right">
                                {((this.props.match.path ===
                                  ASSOCIATION_PATH_PROFILE ||
                                  this.props.id ==
                                    this.props.match.params.id) &&
                                  this.props.userType === "udruga") ||
                                this.props.userType === "admin" ? (
                                  <Button
                                    variant="contained"
                                    color="primary"
                                    onClick={async () =>
                                      fetch(
                                        `${BACKEND_URL}/dog/availability/get?dogId=${row.dogId}`,
                                        {
                                          method: "GET",
                                          headers: {
                                            "Content-Type":
                                              "application/json; charset=UTF-8",
                                            Authorization: authHeader(),
                                          },
                                        }
                                      ).then((json) => {
                                        json.json().then((response) => {
                                          this.setState({
                                            availabilitySet: response,
                                            visibleAvailability: true,
                                          });
                                        });
                                      })
                                    }
                                  >
                                    {this.state.visibleAvailability === true ? (
                                      <EditAvailability
                                        visible={this.state.visibleAvailability}
                                        onClick={this.showModalAvailability}
                                        handleClose={this.hideModalAvailability}
                                        availabilitySet={
                                          this.state.availabilitySet
                                        }
                                        associationId={
                                          this.state.association.id
                                        }
                                        dogId={row.dogId}
                                        walkStyle={row.walkStyle}
                                      />
                                    ) : (
                                      ""
                                    )}
                                    UREDI DOSTUPNOST
                                  </Button>
                                ) : (
                                  ""
                                )}
                              </TableCell>
                              <TableCell style={{ width: 160 }} align="right">
                                <Button
                                  variant="contained"
                                  color="secondary"
                                  onClick={async () =>
                                    fetch(
                                      `${BACKEND_URL}/dog/delete?dogID=${row.dogId}`,
                                      {
                                        method: "DELETE",
                                        headers: {
                                          "Content-Type":
                                            "application/json; charset=UTF-8",
                                          Authorization: authHeader(),
                                        },
                                        //body: JSON.stringify(this.state.initialValues),
                                      }
                                    ).then((response) => {
                                      window.location.reload(false);
                                    })
                                  }
                                >
                                  Ukloni
                                </Button>
                              </TableCell>
                            </TableRow>
                          ))
                        ) : (
                          <TableCell component="th" scope="row">
                            Nema peseka
                          </TableCell>
                        )}
                      </TableBody>
                    </Table>
                  </div>
                </TableContainer>
              </div>
            </TabPanel>
          </Tabs>
        </div>
      </div>
    );
  }
  showModal = () => {
    this.setState({ visible: true });
  };

  hideModal = () => {
    this.setState({ visible: false });
  };
  showModalEdit = () => {
    this.setState({ visibleEdit: true });
  };

  hideModalEdit = () => {
    this.setState({ visibleEdit: false });
  };
}

const mapStateToprops = (state) => {
  return {
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

export default withRouter(connect(mapStateToprops, null)(AdminProfile));
