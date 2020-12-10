import React, { Component } from 'react';
import UserService from '../services/user.service';
import TransactionTable from './transaction-table.component';

/**
 * search page
 */
class Search extends Component {
    constructor(props) {
        super(props);
        this.state = {
            key: '',
            transactions: [],
        }
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;

        let newState = Object.assign({}, this.state);
        newState[name] = value;

        this.setState(newState);
    }

    handleSubmit(event) {
        console.log(this.state)
        event.preventDefault();
        UserService.getSearchTransaction(this.state.key)
            .then(response => {
                console.log(response);
                this.setState({
                    transactions: response.data.transactions
                });
            })
            .catch(error => {
                console.log(error);
                this.props.history.push("/page-not-found");
                window.location.reload();
            })
    }

    render() {
        return (
            <div className="container">
            <h2 className="mt-4 mb-4 text-center ">Search</h2>
                <form className="form-inline justify-content-center mb-4" onSubmit={this.handleSubmit}>
                    <input type="text" name="key" className="form-control col-8 mr-2" id="inputKeyword" placeholder="Keywords" onChange={this.handleChange}/>
                    <button type="submit" className="btn btn-primary col-2">Search</button>
                </form>
                <TransactionTable transactions={this.state.transactions} />
            </div>

        )
    }
}

export default Search;
