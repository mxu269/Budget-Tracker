import React, { Component } from 'react';
import DatePicker from 'react-datepicker';
import RedirectModal from './redirectModal.component';

import UserService from '../services/user.service';

import "react-datepicker/dist/react-datepicker.css";
import '../styles/newtransaction.css';

/**
 * new transaction page
 */
class NewTransaction extends Component {
    constructor(props) {
        super(props);
        this.state = {
            loading: false,
            form: {
                merchantName: '',
                location: '',
                amount: '',
                status: 'expense',
                date: new Date(),
                type: '',
                description: '',
            },
            resTitle: '',
            resMessage: '',
            resRedir: '',
        }

        this.handleChange = this.handleChange.bind(this);
        this.handleDateChange = this.handleDateChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleSuccess = this.handleSuccess.bind(this);
    }

    handleSuccess(event) {
        this.props.history.push("/dashboard");
        window.location.reload();
    }

    handleDateChange(date) {
        let newState = Object.assign({}, this.state.form);
        newState.date = date;

        this.setState({
            form: newState
        });
        //console.log(date);
    }

    handleChange(event) {
        console.log(event);
        const target = event.target;
        const value = target.value;
        const name = target.name;

        let newState = Object.assign({}, this.state.form);
        newState[name] = value;

        this.setState({
            form: newState
        });
        console.log(newState);
    }

    handleSubmit(event) {
        event.preventDefault();
        let data = Object.assign({}, this.state.form);
        data.date = data.date.toISOString();
        UserService.postNewTransaction(data)
            .then(response => {
                console.log("response", response);
                this.setState({
                    resTitle: 'Success',
                    resMessage: response.data.msg,
                    resRedir: '/dashboard',
                })
                document.getElementById('launchRedirectModalButton').click();
            }
            ).catch(error => {
                console.log("error", error);
                this.setState({
                    resTitle: 'Fail',
                    resMessage: error.response.data.msg,
                    resRedir: '/new',
                })
                document.getElementById('launchRedirectModalButton').click();
            }
            );

    }

    render() {
        return (
            <div className="container col-lg-8">
                <h2 className="mt-4 mb-4 text-center ">Create New Transaction</h2>
                <form onSubmit={this.handleSubmit}>
                    <div className="form-group row align-items-center">
                        <label forhtml="inputMerchantName" className="col-sm-2 col-form-label">Merchant name</label>
                        <div className="col-sm-10">
                            <input
                                type="text"
                                className="form-control"
                                name="merchantName"
                                id="inputMerchantName"
                                placeholder="Marriott"
                                onChange={this.handleChange}
                                required
                                autoFocus
                            />
                        </div>
                    </div>

                    <div className="form-group row align-items-center">
                        <label forhtml="inputLocation" className="col-sm-2 col-form-label">Location</label>
                        <div className="col-sm-10">
                            <input
                                type="text"
                                className="form-control"
                                name="location"
                                id="inputLocation"
                                placeholder="New York, NY"
                                onChange={this.handleChange}
                            />
                        </div>
                    </div>

                    <div className="form-group row align-items-center">
                        <label forhtml="inputAmount" className="col-sm-2 col-form-label">Amount</label>

                        <div className="col-sm-10">
                            <div className="input-group">
                                <div className="input-group-prepend">
                                    <div className="input-group-text">{this.state.form.status === 'income' ? '+' : '-'}</div>
                                </div>
                                <input
                                    type="number"
                                    className="form-control"
                                    id="inputAmount"
                                    placeholder="0.00"
                                    min="0"
                                    step="0.01"
                                    name="amount"
                                    onChange={this.handleChange}
                                    required
                                />
                                <div className="input-group-append">
                                    <select name="status" value={this.state.form.status} onChange={this.handleChange} id="inputAmountStatus" className="form-control">
                                        <option value="expense">Expense</option>
                                        <option value="income">Income</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div className="form-group row align-items-center">
                        <label forhtml="inputDate" className="col-sm-2 col-form-label">Date</label>
                        <div className="col-sm-10">
                            <DatePicker
                                className="form-control"
                                name="date"
                                placeholderText="Date"
                                selected={this.state.form.date}
                                onChange={this.handleDateChange}
                            />
                        </div>
                    </div>

                    <div className="form-group row align-items-center">
                        <label forhtml="inputType" className="col-sm-2 col-form-label">Type</label>

                        <div className="col-sm-10">
                            <div className="form-control" id="inputType" >
                                <div className="form-check form-check-inline" onChange={this.handleChange}>
                                    <input className="form-check-input" type="radio" name="type" id="inlineRadio1" value="cash" required />
                                    <label className="form-check-label" forhtml="inlineRadio1">Cash</label>
                                </div>
                                <div className="form-check form-check-inline" onChange={this.handleChange}>
                                    <input className="form-check-input" type="radio" name="type" id="inlineRadio2" value="credit" />
                                    <label className="form-check-label" forhtml="inlineRadio2">Credit/Debit</label>
                                </div>
                                <div className="form-check form-check-inline" onChange={this.handleChange}>
                                    <input className="form-check-input" type="radio" name="type" id="inlineRadio3" value="payment" />
                                    <label className="form-check-label" forhtml="inlineRadio3">Payment</label>
                                </div>
                                <div className="form-check form-check-inline" onChange={this.handleChange}>
                                    <input className="form-check-input" type="radio" name="type" id="inlineRadio4" value="transfer" />
                                    <label className="form-check-label" forhtml="inlineRadio4">Transfer</label>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div className="form-group row">
                        <label className="col-sm-2 col-form-label" forhtml="inputDescription">Description</label>
                        <div className="col-sm-10">
                            <textarea
                                className="form-control"
                                name="description"
                                id="inputDescription"
                                rows="3"
                                onChange={this.handleChange}
                            />

                        </div>
                    </div>

                    <button
                        type="submit"
                        className="btn btn-primary btn-block btn-smbt col-6 mx-auto"
                        disabled={this.state.loading}
                    >
                        Add Transaction
                </button>
                </form>

                <button type="button" id="launchRedirectModalButton" className="btn btn-primary" data-toggle="modal" data-target="#redirectModal" hidden>
                    Launch redirect Modal
                </button>

                <RedirectModal title={this.state.resTitle} to={this.state.resRedir} message={this.state.resMessage} onConfirm={this.handleSuccess} />

            </div>

        )
    }
}

export default NewTransaction;
