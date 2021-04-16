import React from "react";
import "./../static/udruge.css";
import { Link, Redirect } from "react-router-dom";
import { Table, Input, Button, Space } from "antd";
import Highlighter from "react-highlight-words";
import { SearchOutlined } from "@ant-design/icons";
import { authHeader } from "./../helpers/authHeader";
//import Autocomplete, { createFilterOptions } from '@material-ui/lab/Autocomplete';

const { render } = require("react-dom");
//const BACKEND_URL = "http://localhost:8080";
const BACKEND_URL = "https://exception-doggo-backend-dev.herokuapp.com";

class Udruge extends React.Component {
  state = {
    loaded: false,
    udruge: [],
    currentIndex: null,
    searchText: "",
    searchedColumn: "",
    redirect: false,
  };

  getColumnSearchProps = (dataIndex) => ({
    filterDropdown: ({
      setSelectedKeys,
      selectedKeys,
      confirm,
      clearFilters,
    }) => (
      <div style={{ padding: 8 }}>
        <Input
          ref={(node) => {
            this.searchInput = node;
          }}
          placeholder={`Search ${dataIndex}`}
          value={selectedKeys[0]}
          onChange={(e) =>
            setSelectedKeys(e.target.value ? [e.target.value] : [])
          }
          onPressEnter={() =>
            this.handleSearch(selectedKeys, confirm, dataIndex)
          }
          style={{ width: 188, marginBottom: 8, display: "block" }}
        />
        <Space>
          <Button
            type="primary"
            onClick={() => this.handleSearch(selectedKeys, confirm, dataIndex)}
            icon={<SearchOutlined />}
            size="small"
            style={{ width: 90 }}
          >
            Search
          </Button>
          <Button
            onClick={() => this.handleReset(clearFilters)}
            size="small"
            style={{ width: 90 }}
          >
            Reset
          </Button>
        </Space>
      </div>
    ),
    filterIcon: (filtered) => (
      <SearchOutlined style={{ color: filtered ? "#1890ff" : undefined }} />
    ),
    onFilter: (value, record) =>
      record[dataIndex]
        ? record[dataIndex]
            .toString()
            .toLowerCase()
            .includes(value.toLowerCase())
        : "",
    onFilterDropdownVisibleChange: (visible) => {
      if (visible) {
        setTimeout(() => this.searchInput.select(), 100);
      }
    },
    render: (text) =>
      this.state.searchedColumn === dataIndex ? (
        <Highlighter
          highlightStyle={{ backgroundColor: "#ffc069", padding: 0 }}
          searchWords={[this.state.searchText]}
          autoEscape
          textToHighlight={text ? text.toString() : ""}
        />
      ) : (
        text
      ),
  });

  handleSearch = (selectedKeys, confirm, dataIndex) => {
    confirm();
    this.setState({
      searchText: selectedKeys[0],
      searchedColumn: dataIndex,
    });
  };

  handleReset = (clearFilters) => {
    clearFilters();
    this.setState({ searchText: "" });
  };

  componentDidMount() {
    fetch(`${BACKEND_URL}/association/all`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json; charset=UTF-8",
        Authorization: authHeader(),
      },
    }).then((response) => {
      response.json().then((json) => {
        this.setState({
          udruge: json,
        });
        console.log(this.state.udruge);
      });
    });
    console.log(this.props);
  }

  render() {
    console.log(this.state.udruge);

    const columns = [
      {
        key: "name",
        numeric: false,
        disablePadding: true,
        title: "",
        dataIndex: "name",
        ...this.getColumnSearchProps("name"),
      },
      {
        id: "city",
        numeric: false,
        disablePadding: false,
        title: "Mjesto udruge",
        dataIndex: "city",
        key: "city",
        ...this.getColumnSearchProps("city"),
      },
      {
        title: "Detalji udruge",
        key: "operation",
        fixed: "right",
        width: 100,
        render: (data) => (
          <Button
            onClick={async () =>
              fetch(`${BACKEND_URL}/association?associationId=` + data.id, {
                method: "GET",
                headers: {
                  "Content-Type": "application/json; charset=UTF-8",
                  Authorization: authHeader(),
                },
              }).then((response) => {
                if (response.status !== 400) {
                  response.json().then((json) => {
                    console.log(json);
                    this.props.history.push("/association/" + data.id);
                  });
                }
              })
            }
          >
            Pogledaj više
          </Button>
        ),
      },
    ];
    console.log(this.state.udruge);
    /**
    const filterOptions = createFilterOptions({
      matchFrom: 'start',
      stringify: option => option.title,
    });
    */
    // <Autocomplete filterOptions={filterOptions} />
    console.log(this.props.name);

    return (
      <div>
        <div className="udruge-header">
          <div className="paws-down-img"></div>
          <div className="udrugeTitle"> Pronađite peseka u svojoj blizini </div>
        </div>
        <Table
          className="table-udruga"
          columns={columns}
          dataSource={this.state.udruge}
          style={{ margin: "2%", zIndex: "1 !important" }}
        />
      </div>
    );
  }
}

//<Udruga currentIndex={this.state.currentIndex} />
export default Udruge;
