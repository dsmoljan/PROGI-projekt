import React, { useState } from "react";
import { Calendar, Form, Modal, Button, Input, Select, Popconfirm } from "antd";
import { authHeader } from "./../../helpers/authHeader";
const { Option } = Select;

//const BACKEND_URL = "http://localhost:8080";
const BACKEND_URL = "https://exception-doggo-backend-dev.herokuapp.com";

const validateMessages = {
  required: "${label} je nužno upisati!",
};

class DogEditForm extends React.Component {
  constructor(props) {
    super(props);

    this.calendarContainerRef = React.createRef();

    this.state = {
      value: props.defaultValue,
      loaded: false,
      visible: true,
      initialValues: {
        associationId: this.props.associationId,
        dogId: this.props.dogId,
        name: this.props.dogName,
        breed: this.props.breed,
        description: this.props.description,
        picture: this.props.picture,
        deleted: this.props.deleted,
        walkStyle: this.props.walkStyle,
      },
    };
    this.handleChange = this.handleChange.bind(this);
  }
  showModal = () => {
    this.setState({ visible: true });
  };

  hideModal = () => {
    this.setState({ visible: false });
    return false;
  };

  handleChange = (event) => {
    const { name, value } = event.target;

    const { initialValues } = this.state;
    this.setState({
      initialValues: {
        ...initialValues,
        [name]: value,
      },
    });
    console.log(this.state.initialValues);
  };

  onStyleChange = (value) => {
    switch (value) {
      case "INDIVIDUAL":
        const { initialValues } = this.state;
        this.setState({
          initialValues: {
            ...initialValues,
            walkStyle: value,
          },
        });
        return;
      case "GROUP":
        this.setState({
          initialValues: {
            ...this.state.initialValues,
            walkStyle: value,
          },
        });
    }
  };

  deleteHandler = (dogId) => {
    fetch(`${BACKEND_URL}/dog/delete?dogID=${dogId}`, {
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
  };

  onFinish = (values) => {
    console.log(this.state.initialValues);
    fetch(`${BACKEND_URL}/dog/edit`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json; charset=UTF-8",
        Authorization: authHeader(),
      },
      body: JSON.stringify(this.state.initialValues),
    }).then((data) => {
      if (data.error) {
        console.log("data", data);
        alert(data.message);
      } else {
        window.location.reload();
      }
    });
  };

  render() {
    return (
      <Modal
        visible={this.props.visible}
        title="Uredite podatke psa"
        okText="Spremi"
        cancelText="Odustani"
        onCancel={() => {
          this.hideModal();
          window.location.reload();
        }}
        onOk={() => {
          this.onFinish();
          this.hideModal();
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
            onChange={this.handleChange}
            name="name"
            label="Ime"
            rules={[
              {
                required: true,
                message: "Unesite ime psa!",
              },
            ]}
          >
            <Input value={this.state.initialValues.name} name="name" />
          </Form.Item>

          <Form.Item
            onChange={this.handleChange}
            name="picture"
            label="URL slike"
            rules={[
              {
                required: false,
                message: "Unesite url slike!",
              },
            ]}
          >
            <Input value={this.state.initialValues.picture} name="picture" />
          </Form.Item>
          <Form.Item
            onChange={this.handleChange}
            name="breed"
            label="Vrsta"
            rules={[
              {
                required: true,
                message: "Unesite vrstu psa!",
              },
            ]}
          >
            <Input value={this.state.initialValues.breed} name="breed" />
          </Form.Item>
          <Form.Item
            onChange={this.handleChange}
            name="description"
            label="Opis"
            rules={[
              {
                required: true,
                message: "Unesite opis psa!",
              },
            ]}
          >
            <Input
              value={this.state.initialValues.description}
              name="description"
            />
          </Form.Item>
          <Form.Item
            name="walkStyle"
            label="Preferirani način šetnje"
            rules={[
              {
                required: true,
                message: "Unesite preferirani način šetnje!",
              },
            ]}
          >
            <Select
              onChange={this.onStyleChange}
              name="walkStyle"
              optionFilterProp="children"
              filterOption={(input, option) =>
                option.children.indexOf(input) >= 0
              }
            >
              <Option value="INDIVIDUAL">Individualni</Option>
              <Option value="GROUP">Grupni</Option>
            </Select>
          </Form.Item>
          <Popconfirm
            placement="topRight"
            title={"Jeste li sigurni da želite obrisati psa?"}
            onConfirm={(e) =>
              this.deleteHandler(this.state.initialValues.dogId, e)
            }
            okText="Da"
            cancelText="Ne"
          >
            <Button type="danger">Obriši</Button>
          </Popconfirm>
        </Form>
      </Modal>
    );
  }
}

export default DogEditForm;
