import React, { Component } from 'react';
import Chart from "react-google-charts";

/**
 * Daily chart in the dashboard. A bar graph. Using react-google-charts
 */
class DailyChart extends Component {
    constructor(props) {
        super(props);
        
    }

    render() {
        const amounts = this.props.amounts;
        return (
            <div className="Container">
            <h3>This Week's Expense</h3>
                <Chart
                    width={'500px'}
                    height={'300px'}
                    chartType="Bar"
                    loader={<div>Loading Chart</div>}
                    data={[
                        ['', ''],
                        ['MON', amounts[0]],
                        ['TUE', amounts[1]],
                        ['WED', amounts[2]],
                        ['THU', amounts[3]],
                        ['FRI', amounts[4]],
                        ['SAT', amounts[5]],
                        ['SUN', amounts[6]],
                    ]}
                    options={{
                        legend: { position: 'none' },
                    }}
                />
            </div>
        )
    }
}

export default DailyChart;
