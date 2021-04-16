import React, { useState } from "react";

import {
  Calendar,
  Form,
  Modal,
  Button,
  Alert,
  Popconfirm,
  Table,
  Select,
} from "antd";
import { ToastContainer, toast } from "react-toastify";
import ReservationFormCss from "../../static/ReservationForm.css";
import { DatePicker, TimePicker } from "antd";
import PropTypes from "prop-types";
import moment from "moment";
import "moment/locale/eu";
import { authHeader } from "./../../helpers/authHeader";
// const { RangePicker } = TimePicker;

var momentRange = require("moment-range");
momentRange.extendMoment(moment);

//const BACKEND_URL = "http://localhost:8080";
const BACKEND_URL = "https://exception-doggo-backend-dev.herokuapp.com";
// const { RangePicker } = DatePicker;

const { Option } = Select;

const now = moment();
now.locale("eu").utcOffset(0);

const validateMessages = {
  required: "${label} je nužno upisati!",
  types: {
    number: "Neispravan broj",
  },
  number: {
    min: "${label} mora biti veća od 0!",
  },
};

const defaultCalendarValue = now.clone();
defaultCalendarValue.add(-1, "month");

class ReservationForm extends React.Component {
  constructor(props) {
    super(props);

    this.calendarContainerRef = React.createRef();

    this.state = {
      dogsIDList: [],
      dogListFetch: [],
      startDate: moment(this.props.start),

      mytimestart: "",

      selectedStartDate: "",
      selectedEndDate: "",
      selectedEndTime: "",
      selectedStartTime: "",

      showTime: true,
      showDateInput: true,
      disabled: false,

      dateRetVal: "",
      startRetVal: "",
      endRetVal: "",

      timeValueStart: props.defaultValue,
      timeValueEnd: props.defaultValue,
      loaded: false,
      visible: false,
      initialValues: {
        dogId: this.props.dogId,
        walkStyle: this.props.walkStyle,
      },
    };
  }

  disabledDate2 = (current) => {
    //console.log("good_dates =" + this.props.GOOD_DATES);
    //console.log(current);
    for (var i = 0; i < this.props.GOOD_DATES.length; i++) {
      if (
        (current.isBefore(this.props.GOOD_DATES[i].end) &&
          current.isAfter(this.props.GOOD_DATES[i].start)) ||
        current === this.props.GOOD_DATES[i].end ||
        current === this.props.GOOD_DATES[i].start
      ) {
        return false;
      }
    }
    return true;
  };

  range(start, end) {
    const result = [];
    for (let i = start; i < end; i++) {
      result.push(i);
    }
    return result;
  }
  disabledHours2 = () => {
    console.log(this.state.dateRetVal.format("yyyy-MM-DD"));
    //if (this.state.dateRetVal == "") return true;
    // za selektirani datum, disableaj vrijeme izvan rangea
    for (var i = 0; i < this.props.availabilitySet.length; i++) {
      var rangeDate = moment.range(
        moment(this.props.availabilitySet[i].startDate, "yyyy-MM-DD"),
        moment(this.props.availabilitySet[i].endDate, "yyyy-MM-DD")
      );
      if (rangeDate.contains(this.formatDate(this.state.dateRetVal._d))) {
        var rangeTime = moment.range(
          moment(this.props.availabilitySet[i].startTime, "HH"),
          moment(this.props.availabilitySet[i].endTime, "HH")
        ); /**
          const hourStart = this.props.availabilitySet[i].startTime;
          const minuteStart = this.props.availabilitySet[i].startTime;
          const hourEnd = this.props.availabilitySet[i].endTime;
          const minuteEnd = this.props.availabilitySet[i].endTime;
            */
        return rangeTime;
      }
      //console.log(current);
    }
  };

  disabledDate = (current) => {
    if (current < moment().toDate()) {
      return true;
    }
    return (
      current.format("yyyy-MM-DD") < this.state.selectedStartDate ||
      current.format("yyyy-MM-DD") > this.state.selectedEndDate
    );
  };

  disabledTimeStart = () => {
    var hours = [];
    for (var i = 0; i < moment().hour(); i++) {
      hours.push(i);
    }
    return hours;
  };
  disabledTimeEnd = (current) => {
    //console.log(current, this.state.selectedEndTime);
    return current < this.state.selectedEndTime;
  };

  hideModal = () => {
    this.setState({ visible: false });
    return false;
  };
  /**
  disabledHours = () => {
    console.log(this.target.defaultValue, this.state.selectedStartTime);
    return this.state.selectedStartTime > this.state.mytimestart;
  }
*/
  onChange(time, timeString) {
    //console.log(time, timeString);
    this.setState({
      mytimestart: this.formatTime(time._d),
    });
  }
  onChangeDate = (value) => {
    console.log(this.state.dateRetVal);
    this.setState({
      dateRetVal: value,
    });
  };
  onChangeTimeStart = (value) => {
    this.setState({
      startRetVal: value,
    });
    //console.log(this.state.startRetVal);
  };
  onChangeTimeEnd = (value) => {
    this.setState({
      endRetVal: value,
    });
    //console.log(value.format("HH:MM"));
  };
  /**
  addADay = () => {
    this.setState({
      dateRetVal: moment(this.state.dateRetVal, "yyyy-MM-DD").add(1, "days"),
    });
  };
*/
  onFinish = () => {
    //this.addADay();
    //console.log(this.state.dateRetVal._d.getMonth());
    //console.log(this.state.startRetVal);
    //console.log(this.state.endRetVal);

    //var dateRel = this.formatDate(this.state.dateRetVal._d);
    //var startRel = this.formatTime(this.state.startRetVal._d);
    //var endRel = this.formatTime(this.state.endRetVal._d);
    //console.log(dateRel + " " + startRel + " " + endRel)

    //console.log(this.state.dateRetVal);
    //
    //JELI OVO KAO ZA INDIVIDUALNE ???
    var date =
      this.state.dateRetVal._d === undefined
        ? new Date()
        : this.state.dateRetVal._d;
    if (this.state.dogListFetch.length == 0) {
      this.state.dogsIDList.push(this.props.dogId);
      //console.log(this.state.dogsIDList);
      console.log(date);
      fetch(`${BACKEND_URL}/reservations/add`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json; charset=UTF-8",
          Authorization: authHeader(),
        },
        body: JSON.stringify({
          walkerID: this.props.walkerId,
          date: this.formatDate(date),
          startTime: this.formatTime(this.state.startRetVal._d),
          returnTime: this.formatTime(this.state.endRetVal._d),
          dogsIDList: [this.props.dogId],
        }),
      }).then((data) => {
        if (data.status === 400) {
          toast.error("❌ Odabrani termin je nedostupan!", {
            position: "top-right",
            autoClose: 5000,
            hideProgressBar: true,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
          });
        } else {
          data.json().then((response) => {
            console.log(response);
          });
          window.location.reload();
          toast.success("🐶 Uspješna rezervacija!", {
            position: "top-right",
            autoClose: 5000,
            hideProgressBar: true,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
          });
        }
        console.log({
          walkerID: this.props.walkerId,
          date: this.formatDate(date),
          startTime: this.formatTime(this.state.startRetVal._d),
          returnTime: this.formatTime(this.state.endRetVal._d),
          dogsIDList: [this.props.dogId],
        });
      });
    } else {
      //let a = this.state.dogsIDList.slice(); //creates the clone of the state
      // console.log(a.length);
      // console.log(a);
      // a[a.length] = this.props.dogId;
      // this.setState({ dogsIDList: a });
      // console.log(this.props.dogId);
      // this.setState({
      //   dogsIDList: [...this.state.dogsIDList, this.props.dogId]
      // });
      this.state.dogsIDList.push(this.props.dogId);
      console.log(this.state.dogsIDList);
      fetch(`${BACKEND_URL}/reservations/add`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json; charset=UTF-8",
          Authorization: authHeader(),
        },
        body: JSON.stringify({
          walkerID: this.props.walkerId,
          date: this.formatDate(date),
          startTime: this.formatTime(this.state.startRetVal._d),
          returnTime: this.formatTime(this.state.endRetVal._d),
          dogsIDList: this.state.dogsIDList,
        }),
      }).then((data) => {
        if (data.status === 400) {
          toast.error("❌  Odabrani termin je nedostupan!", {
            position: "top-right",
            autoClose: 5000,
            hideProgressBar: true,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
          });
        } else {
          data.json().then((response) => {
            console.log(response);
          });
          toast.success("🐶 Uspješna rezervacija!", {
            position: "top-right",
            autoClose: 5000,
            hideProgressBar: true,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
          });
          window.location.reload();
        }
        console.log({
          walkerID: this.props.walkerId,
          date: this.formatDate(date),
          startTime: this.formatTime(this.state.startRetVal._d),
          returnTime: this.formatTime(this.state.endRetVal._d),
          dogsIDList: this.state.dogsIDList,
        });
      });
    }
    //this.state.dogListFetch.push(this.props.id);
  };

  handleOnSelect = (value) => {
    if (value !== this.props.dogId) {
      this.setState({
        dogsIDList: [...this.state.dogsIDList, value],
      });
      // console.log(this.state.dogsIDList);
    }
  };
  zastavica = true;

  fetchDogList() {
    if (this.zastavica) {
      fetch(`${BACKEND_URL}/reservations/doglist-by-availability`, {
        method: "POST",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
          Authorization: authHeader(),
        },
        body: JSON.stringify({
          associationId: this.props.associationId,
          date: this.formatDate(this.state.dateRetVal._d),
          startTime: this.formatTime(this.state.startRetVal._d),
          endTime: this.formatTime(this.state.endRetVal._d),
        }),
      }).then((response) => {
        if (response.status === 400) {
          toast.error("❌ Nema dostupnih pasa u navedenom terminu!", {
            position: "top-right",
            autoClose: 5000,
            hideProgressBar: true,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
          });
        } else {
        }
        response.json().then((json) => {
          this.setState({
            dogListFetch: json,
          });
          console.log(this.state.dogListFetch);
        });
      });
      this.zastavica = false;
    }
  }

  formatTime(time) {
    var hours =
      time.getHours() >= 0 && time.getHours() <= 9
        ? "0" + time.getHours()
        : time.getHours();
    var minutes =
      time.getMinutes() >= 0 && time.getMinutes() <= 9
        ? "0" + time.getMinutes()
        : time.getMinutes();
    return hours + ":" + minutes + ":00";
  }

  formatDate(date) {
    console.log(date);
    var day =
      date.getDate() >= 1 && date.getDate() <= 9
        ? "0" + date.getDate()
        : date.getDate();
    var month =
      date.getMonth() + 1 >= 1 && date.getMonth() + 1 <= 9
        ? "0" + (date.getMonth() + 1)
        : date.getMonth() + 1;
    console.log(date.getFullYear() + "-" + month + "-" + day);
    return date.getFullYear() + "-" + month + "-" + day;
  }

  columns = [
    {
      dataIndex: "startDate",
      key: "startDate",
    },
    {
      dataIndex: "endDate",
      key: "endDate",
    },
    {
      dataIndex: "startTime",
      key: "startTime",
    },

    {
      dataIndex: "endTime",
      key: "endTime",
    },
  ];

  render() {
    console.log(this.state.dateRetVal);
    const state = this.state;
    return (
      <Modal
        visible={this.props.visible}
        title="Rezerviraj svoju šetnju"
        okText="Rezerviraj"
        cancelText="Odustani"
        onCancel={() => {
          this.hideModal();

          window.location.reload();
        }}
        onOk={() => {
          this.onFinish();
          // this.hideModal();

          // window.location.reload();
        }}
        //onFinish={onFinish}
      >
        <Form
          initialValues={this.state.initialValues}
          name="nest-messages"
          validateMessages={validateMessages}
          layout="vertical"
          name="form_in_modal"
        >
          <Form.Item
            name="availabilitySet"
            label="Odaberi termin šetnje u zadanom rasponu"
            rules={[
              {
                required: true,
                message: "Unesite dostupnost psa!",
              },
            ]}
          >
            {/**
            {this.props.availabilitySet !== undefined ? (
              <div>
                <Form.Item tyle={{ marginTop: "2%" }}>
                  <div style={{ marginTop: "1%" }}>
                    <Table
                      columns={this.columns}
                      expandable={{
                        expandedRowRender: (record) => (
                          console.log(record),
                          (this.state.dateStart = record.dateStart),
                          (
                            <p style={{ margin: 0 }}>
                              <div>
                                <DatePicker
                                  style={{ margin: "1%" }}
                                  format="yyyy-MM-DD"
                                  onClick={async () =>
                                    this.setState({
                                      selectedStartDate: record.startDate,
                                      selectedEndDate: record.endDate,
                                    })
                                  }
                                  dateRender={(current) => {
                                    const style = {};

                                    if (
                                      current.format("yyyy-MM-DD") >=
                                        this.state.selectedStartDate &&
                                      current.format("yyyy-MM-DD") <=
                                        this.state.selectedEndDate
                                    ) {
                                      style.border = "1px solid #1890ff";
                                      style.borderRadius = "50%";
                                    }
                                    return (
                                      <div
                                        className="ant-picker-cell-inner"
                                        style={style}
                                      >
                                        {current.date()}
                                      </div>
                                    );
                                  }}
                                  selected={moment(this.state.startDate)}
                                  //value={moment(this.state.value)}
                                  placeholder={"Odaberi datum"}
                                  // defaultValue={moment(this.state.selectedStartDate)}
                                  disabledDate={this.disabledDate2}
                                  onChange={this.onChangeDate}

                                  //  }}
                                />
                              </div>
                              <TimePicker
                                style={{ margin: "1%" }}
                                format="HH:mm:ss"
                                onClick={async (time) =>
                                  this.setState({
                                    selectedStartTime: record.startTime,
                                    mytimestart: time,
                                  })
                                }
                                defaultValue={this.state.selectedStartTime}
                                onChange={this.onChangeTimeStart}
                                value={state.timeValueStart}
                                placeholder={"Rezerviraj od"}
                                //disabledHours={this.disabledHours}
                                //disabledMinutes={this.getDisabledMinutes()}
                              />
                              <TimePicker
                                style={{ margin: "1%" }}
                                format="HH:mm:ss"
                                onClick={async () =>
                                  this.setState({
                                    selectedEndTime: record.endTime,
                                  })
                                }
                                onChange={this.onChangeTimeEnd}
                                value={state.timeValueEnd}
                                placeholder={"Rezerviraj do"}
                              />
                             
                            </p>
                          )
                        ),
                      }}
                      dataSource={this.props.availabilitySet}
                    />
                  </div>
                </Form.Item>
              </div>
            ) : (
              <div> Trenutno nema raspoloživih termina za šetnju.</div>
            )}*/}
            {this.props.availabilitySet !== undefined ? (
              <div>
                <Form.Item tyle={{ marginTop: "2%" }}>
                  <div style={{ marginTop: "1%" }}>
                    <div className="site-calendar-demo-card">
                      <Calendar
                        //defaultValue={moment(now)}
                        fullscreen={false}
                        disabledDate={this.disabledDate2}
                        onChange={this.onChangeDate}
                      />
                    </div>
                    <div style={{ marginTop: "2%" }}>
                      <TimePicker
                        format="HH:mm"
                        defaultValue={this.state.selectedStartTime}
                        //disabledHours={this.disabledHours2}
                        //disabledHours={this.disabledHours2}
                        //minTime={this.disabledHours2}
                        onChange={this.onChangeTimeStart}
                        //value={state.timeValueStart}
                      />
                      <TimePicker
                        format="HH:mm"
                        defaultValue={this.state.selectedStartTime}
                        //disabledHours={this.disabledHours2}
                        //disabledHours={this.disabledHours2}
                        //minTime={this.disabledHours2}
                        onChange={this.onChangeTimeEnd}
                        //value={state.timeValueStart}
                      />
                    </div>
                  </div>
                </Form.Item>
                <Form.Item>
                  {this.props.walkStyle === "GROUP" &&
                  this.state.dateRetVal !== "" &&
                  this.state.endRetVal !== "" &&
                  this.state.startRetVal !== "" ? (
                    //console.log(this.props.walkStyle, this.state.dateRetVal),
                    <Form.Item
                      style={{ margin: "1%" }}
                      name="group"
                      hasFeedback
                      rules={[{ required: false }]}
                    >
                      <Select
                        placeholder="Odaberite pse za grupnu šetnju"
                        onClick={this.fetchDogList()}
                        onSelect={(value) => this.handleOnSelect(value)}
                      >
                        {this.state.dogListFetch !== []
                          ? this.state.dogListFetch.map((dog) => {
                              return (
                                <Option
                                  value={dog.dogId}
                                  multiple
                                  disabled={dog.dogId == this.props.dogId}
                                >
                                  {dog.name}
                                </Option>
                              );
                            })
                          : ""}
                      </Select>
                    </Form.Item>
                  ) : (
                    ""
                  )}
                </Form.Item>
              </div>
            ) : (
              <div> Trenutno nema raspoloživih termina za šetnju.</div>
            )}
          </Form.Item>
        </Form>
        <ToastContainer></ToastContainer>
      </Modal>
    );
  }
}
ReservationForm.propTypes = {
  start: PropTypes.object.isRequired,
};

export default ReservationForm;
