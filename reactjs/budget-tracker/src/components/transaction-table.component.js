import React, { Component } from 'react';
import TransactionCell from './transaction-cell.component';

/**
 * transaction table component
 */
class TransactionTable extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        const typeStr = {
            "cash": "Cash",
            "credit": "Credit/Debit",
            "payment": "Payment",
            "transfer": "Transfer",
        }
        return (

            <div>
                {this.props.transactions.map((t) => (
                    <TransactionCell
                    id={t.id}
                    key={t.id}
                    merchantName={t.merchantName}
                    type={typeStr[t.type]}
                    location={t.location}
                    date={new Date(t.date).toDateString()}
                    amount={(t.status === 'income' ? '+' : '-') + '$' + t.amount}
                />
                ))}
            </div>
        )
    }
}

export default TransactionTable;
