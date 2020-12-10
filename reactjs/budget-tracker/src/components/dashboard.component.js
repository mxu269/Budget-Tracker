import React, { Component } from 'react';
import DailyChart from './daily-chart.component'
import UserService from '../services/user.service';
import TransactionTable from './transaction-table.component';
import '../styles/navbar.css';

/**
 * Dashboard components including the graph, balance, and recent transactions
 */
class Dashboard extends Component {
    constructor(props) {
        super(props);
        this.state = {
            amounts: [0, 0, 0, 0, 0, 0, 0],
            balance: 0,
            recentTransactions: [],
        }
    }

    componentDidMount() {
        UserService.getWeeklySummary()
            .then((response) => {
                console.log(response)
                this.setState({
                    amounts: response.data.amounts,
                })
            })
            .catch((error) => {
                console.log(error);
            })

            UserService.getBalance()
            .then((response) => {
                console.log(response)
                this.setState({
                    balance: response.data.balance,
                })
            })
            .catch((error) => {
                console.log(error);
            })

            UserService.getRecentTransaction()
            .then((response) => {
                console.log(response)
                this.setState({
                    recentTransactions: response.data.transactions,
                })
            })
            .catch((error) => {
                console.log(error);
            })


    }

    render() {
        return (
            <div className="containter">
                <h2 className="mt-4 mb-4 text-center ">Dashboard</h2>
                <div className="row content-center">
                    <div className="ml-5 mb-0 col-sm-12 col-md-8 col-lg-6">
                        <div className="content-center">
                            <DailyChart amounts={this.state.amounts} />
                        </div>
                    </div>

                    <div className="ml-5 mr-5 col-md-6 col-lg-4">
                        <div className="content-center pt-5">
                            <h1>Balance: ${this.state.balance}</h1>
                        </div>
                    </div>
                </div>

                <div className="container mt-4">
                    <h3>Recent</h3>
                    <TransactionTable transactions={this.state.recentTransactions} />
                </div>
                


            </div>
        )
    }
}

export default Dashboard;
