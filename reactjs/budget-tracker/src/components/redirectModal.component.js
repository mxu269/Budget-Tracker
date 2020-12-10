import React, { Component } from 'react';
import { Redirect } from 'react-router-dom';

/**
 * a modal prompt that eventaully confrims redirect
 */
class RedirectModal extends Component {

    render() {
        return (
            <div className="modal fade" id="redirectModal" tabIndex="-1" role="dialog">
                <div className="modal-dialog" role="document">
                    <div className="modal-content">
                        <div className="modal-header">
                            <h5 className="modal-title" id="redirectModalLabel">{this.props.title}</h5>
                            {/* <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button> */}
                        </div>
                        <div className="modal-body">
                            {this.props.message}
                            </div>
                        <div className="modal-footer">
                            <button type="button" className="btn btn-primary" onClick={this.props.onConfirm}>OK</button>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

export default RedirectModal;
