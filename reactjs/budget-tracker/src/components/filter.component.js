import React, { Component } from 'react';
import DatePicker from "react-datepicker";

import "react-datepicker/dist/react-datepicker.css";

/**
 * unused component
 */
class Filter extends Component {
    constructor(props) {
        super(props);

        this.state = {
            minDate: new Date(),
            maxDate: new Date(),
            minAmount: '20.00',
            maxAmount: '35.00',
            type: '3',
        }

        this.handleMinDateChange = this.handleMinDateChange.bind(this);
        this.handleMaxDateChange = this.handleMaxDateChange.bind(this);
    }

    componentDidUpdate() {
        console.log(this.state.minDate.toISOString())
    }

    handleMinDateChange(date) {
        this.setState({
            minDate: date
        })
        console.log(date);
    }

    handleMaxDateChange(date) {
        this.setState({
            maxDate: date
        })
        console.log(date);
    }

    render() {
        return (

            <div>

                <div className="input-group mb-3">
                    <DatePicker
                        className="form-control"
                        name="minDate"
                        placeholderText="Start date"
                        selected={this.state.minDate}
                        onChange={this.handleMinDateChange}
                    />

                    <span className="input-group-text">↔</span>

                    <DatePicker
                        className="form-control"
                        name="maxDate"
                        placeholderText="EndDate"
                        selected={this.state.maxDate}
                        onChange={this.handleMaxDateChange}
                    />
                </div>

                <form>
                    <div class="form-group">
                        <label for="formControlRange">Example Range input</label>
                        <input type="range" class="form-control-range" id="formControlRange" />
                    </div>
                </form>



            </div>






        )
    }
}

export default Filter;
