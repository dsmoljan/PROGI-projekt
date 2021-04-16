import React, { useState } from "react";
import { Calendar, Form, Modal, Button, Alert, Popconfirm, Table } from "antd";
import ReservationFormCss from "../../static/ReservationForm.css";
import { DatePicker, TimePicker } from "antd";
import moment from "moment";
import "moment/locale/eu";
import { authHeader } from "./../../helpers/authHeader";

//const BACKEND_URL = "http://localhost:8080";
const BACKEND_URL = "https://exception-doggo-backend-dev.herokuapp.com";
const { RangePicker } = DatePicker;
const { RangePickerTime } = TimePicker;

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

function disabledDate(current) {
  if (!current) {
    // allow empty select
    return false;
  }
  const date = moment();
  date.hour(0);
  date.minute(0);
  date.second(0);
  return current.valueOf() < date.valueOf(); // can not select days before today
}
class EditAvailability extends React.Component {
  constructor(props) {
    super(props);

    this.calendarContainerRef = React.createRef();

    this.state = {
      showTime: true,
      showDateInput: true,
      disabled: false,
      dateStart: props.defaultValue,
      dateEnd: props.defaultValue,
      timeValueStart: props.defaultValue,
      timeValueEnd: props.defaultValue,
      loaded: false,
      visible: true,
      initialValues: {
        dogId: this.props.dogId,
        dogAvailabilitySet: [],
        walkStyle: this.props.walkStyle,
      },
    };
  }

  onChange = (value) => {
    console.log("DatePicker change: ", value);
    this.setState({
      dateStart: value[0],
      dateEnd: value[1],
    });
  };
  onChangeTimeStart = (value) => {
    this.setState({
      timeValueStart: value,
    });
    console.log(this.state.timeValueStart);
  };
  onChangeTimeEnd = (value) => {
    this.setState({
      timeValueEnd: value,
    });
    console.log(value.format("HH:MM"));
  };

  onFinish = (values) => {
    console.log(this.state.value);
    fetch(`${BACKEND_URL}/dog/availability/set`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json; charset=UTF-8",
        Authorization: authHeader(),
      },
      body: JSON.stringify({
        dogID: this.props.dogId,
        startDate: this.state.dateStart.format("yyyy-MM-DD"),
        endDate: this.state.dateEnd.format("yyyy-MM-DD"),
        startTime: this.state.timeValueStart.format("HH:mm:ss"),
        endTime: this.state.timeValueEnd.format("HH:mm:ss"),
      }),
    }).then((data) => {
      console.log({
        dogID: this.props.dogId,
        startDate: this.state.dateStart.format("yyyy-MM-DD"),
        endDate: this.state.dateEnd.format("yyyy-MM-DD"),
        startTime: this.state.timeValueStart.format("HH:mm:ss"),
        endTime: this.state.timeValueEnd.format("HH:mm:ss"),
      });

      if (data.error) {
        console.log("data", data);
        alert(data.message);
      } else {
        window.location.reload();
      }
    });
  };

  columns = [
    {
      title: "Svaki dan od",
      dataIndex: "startDate",
      key: "startDate",
    },
    {
      title: "Do",
      dataIndex: "endDate",
      key: "endDate",
    },
    {
      title: "U razdoblju od",
      dataIndex: "startTime",
      key: "startTime",
    },

    {
      title: "Do",
      dataIndex: "endTime",
      key: "endTime",
    },
    {
      title: "Obriši",
      data: "data",
      key: "delete",
      render: (data) => (
        <Popconfirm
          placement="topRight"
          title={"Jeste li sigurni da želite obrisati ovu dostupnost?"}
          onConfirm={async () => {
            console.log(data);
            fetch(
              `${BACKEND_URL}/dog/availability/delete?availabilityId=` +
                data.id,
              {
                method: "DELETE",
                headers: {
                  "Content-Type": "application/json; charset=UTF-8",
                  Authorization: authHeader(),
                },
              }
            ).then((response) => {
              if (response.status !== 400) {
                response.json().then((json) => {
                  window.location.reload();
                });
              }
            });
          }}
          okText="Da"
          cancelText="Ne"
        >
          <Button type="danger">Obriši</Button>
        </Popconfirm>
      ),
    },
  ];

  deleteHandler = (id) => {
    fetch(`${BACKEND_URL}/dog/availability/delete?availabilityId=${id}`, {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json; charset=UTF-8",
        Authorization: authHeader(),
      },
    })
      .then((data) => {
        if (data.error) {
          console.log("data", data);
          alert(data.message);
        } else {
        }
      })
      .catch((err) => {
        console.log(err);
      });
  };

  render() {
    const state = this.state;
    return (
      <Modal
        visible={this.state.visible}
        title="Uredite dostupnost psa"
        okText="Spremi"
        cancelText="Odustani"
        onCancel={() => {
          window.location.reload();
        }}
        onOk={() => {
          this.onFinish();
          window.location.reload();
        }}
      >
        <Form
          initialValues={this.state.initialValues}
          name="nest-messages"
          validateMessages={validateMessages}
          layout="vertical"
          name="form_in_modal"
        >
          <Form.Item
            name="dogAvailabilitySet"
            label="Dostupnost"
            rules={[
              {
                required: true,
                message: "Unesite dostupnost psa!",
              },
            ]}
          >
            <div className="site-calendar-demo-card">
              <RangePicker
                format="yyyy-MM-DD"
                onChange={this.onChange}
                defaultValue={this.props.defaultCalendarValue}
                disabledDate={disabledDate}
                value={state.value}
                placeholder={["Dostupan od", "Dostupan do"]}
              />
            </div>
            <div className="site-calendar-demo-card">
              <TimePicker
                format="HH:mm"
                onChange={this.onChangeTimeStart}
                value={state.timeValueStart}
                placeholder={"Dostupan od"}
              />
              <TimePicker
                format="HH:mm"
                onChange={this.onChangeTimeEnd}
                value={state.timeValueEnd}
                placeholder={"Dostupan do"}
              />
            </div>
          </Form.Item>
          <Form.Item tyle={{ marginTop: "2%" }}>
            Trenutna dostupnost psa:
            <div style={{ marginTop: "1%" }}>
              <Table
                columns={this.columns}
                dataSource={this.props.availabilitySet}
              />
            </div>
          </Form.Item>
        </Form>
      </Modal>
    );
  }
}

export default EditAvailability;
