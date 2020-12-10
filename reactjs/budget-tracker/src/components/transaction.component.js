import React, { Component } from 'react';
import RedirectModal from './redirectModal.component';

import UserService from '../services/user.service';

import "react-datepicker/dist/react-datepicker.css";
import '../styles/newtransaction.css';

/**
 * transaction page
 */
class Transaction extends Component {
    constructor(props) {
        super(props);
        this.state = {
            loaded: false,
            loading: false,
            id: props.match.params.id,
            merchantName: 'Apple Store',
            location: 'New York, NY',
            amount: '12.99',
            status: 'expense',
            date: new Date(),
            type: 'credit',
            description: 'some desc some desc some desc some d',
            resTitle: '',
            resMessage: '',
            resRedir: '',
        }

        this.handleDelete = this.handleDelete.bind(this);
        this.handleSuccess = this.handleSuccess.bind(this);

    }

    componentDidMount() {
        console.log(this.state.id);
        UserService.getTransaction(this.state.id)
            .then(response => {
                console.log(response);
                let newState = Object.assign({}, response.data);
                newState.date = new Date(newState.date)
                console.log(newState);
                newState.loaded = true;
                this.setState(newState);
            })
            .catch(error => {
                console.log(error);
                this.props.history.push("/page-not-found");
                window.location.reload();
            })
    }

    handleSuccess(event) {
        this.props.history.push("/dashboard");
        window.location.reload();
    }

    handleDelete() {
        this.setState({
            loading: true,
        });
        UserService.postDeleteTransaction(this.state.id)
            .then(response => {
                console.log(response);
                this.setState({
                    resTitle: 'Success',
                    resMessage: response.data.msg,
                    resRedir: '/dashboard',
                })
                document.getElementById('launchRedirectModalButton').click();
            })
            .catch(error => {
                this.setState({
                    resTitle: 'Fail',
                    resMessage: error.response.data.msg,
                    resRedir: window.location.pathname,
                })
                document.getElementById('launchRedirectModalButton').click();
            })
            .finally(() => {
                this.setState({
                    loading: false,
                });
            })

    }

    render() {
        const typeStr = {
            "cash": "Cash",
            "credit": "Credit/Debit",
            "payment": "Payment",
            "transfer": "Transfer",
        }

        const amountStr = (this.state.status === 'income' ? '+' : '-') + '$' + this.state.amount;

        return this.state.loaded ? (
            <div className="container col-lg-8">
                <h2 className="mt-4 mb-4 text-center ">{amountStr + ', ' + this.state.merchantName}</h2>
                <div className="row justify-content-center">
                    <div className="col-auto">
                        <table className="table mb-0">
                            <tbody>
                                <tr>
                                    <th>Merchant name</th>
                                    <td>{this.state.merchantName}</td>
                                </tr>
                                <tr>
                                    <th>Location</th>
                                    <td>{this.state.location}</td>
                                </tr>
                                <tr>
                                    <th>Amount</th>
                                    <td>{amountStr}</td>
                                </tr>
                                <tr>
                                    <th>Date</th>
                                    <td>{this.state.date.toDateString()}</td>
                                </tr>
                                <tr>
                                    <th>Type</th>
                                    <td>{typeStr[this.state.type]}</td>
                                </tr>
                                <tr>
                                    <th>Description</th>
                                    <td>
                                        <textarea
                                            id="viewTranTextArea"
                                            value={this.state.description}
                                            disabled
                                        />
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                        <button
                        type="submit mt-0 mb-4"
                        className="btn btn-danger btn-block btn-smbt mx-auto"
                        onClick={this.handleDelete}
                        disabled={this.state.loading}
                    >
                        Delete Transaction
                </button>
                    </div>
                    
                </div>
                <button type="button" id="launchRedirectModalButton" className="btn btn-primary" data-toggle="modal" data-target="#redirectModal" hidden>
                    Launch redirect Modal
                </button>

                <RedirectModal title={this.state.resTitle} to={this.state.resRedir} message={this.state.resMessage} onConfirm={this.handleSuccess} />

            </div>

        ) : null
    }
}

export default Transaction;
