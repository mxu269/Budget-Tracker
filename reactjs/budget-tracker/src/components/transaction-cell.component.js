import React, { Component } from 'react';
import '../styles/transactioncard.css';

/**
 * transaction cell component
 */
class TransactionCell extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div>
                <div className="card tran bg-light mb-3">
                    <div className="card-body lil-pad">
                        <a href={'transaction/' + this.props.id}>
                            <h5 className="mb-0">{this.props.merchantName}<span className="badge badge-secondary ml-2">{this.props.type}</span></h5>
                        </a>
                        <div className="row zero-line-height">
                            <p className="card-text col-8 mb-0">{this.props.location}<br/>{this.props.date}</p>
                            <p className="card-text col-4 float-right h5">{this.props.amount}</p>
                        </div>

                    </div>
                </div>
            </div>
        )
    }
}

export default TransactionCell;
