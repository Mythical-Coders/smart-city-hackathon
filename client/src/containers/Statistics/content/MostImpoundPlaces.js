import * as React from 'react';
import Paper from '@material-ui/core/Paper';
import {
  Chart,
  BarSeries,
  Title,
  ArgumentAxis,
  ValueAxis,
} from '@devexpress/dx-react-chart-material-ui';

import { Animation } from '@devexpress/dx-react-chart';

const data = [
  { place: 'Av Borguiba', number: 100 },
  { place: 'Av Farhat Hached', number: 20},
  { place: 'Av Tanja', number: 50 },
  { place: 'Av 15 October', number: 60 },
  { place: 'Av Kods', number: 70 },
  { place: 'Av 14 Jan', number: 80 },
  { place: 'Av aze', number: 12 },
  { place: 'Av Kodzaes', number: 70 },
  { place: 'Av 14 Jazean', number: 80 },
  { place: 'Av Rozaema', number: 12 },
];

export default class MostImpoundPlaces extends React.PureComponent {
  constructor(props) {
    super(props);

    this.state = {
      data,
    };
  }

  render() {
    const { data: chartData } = this.state;

    return (
      <Paper>
        <Chart
          data={chartData}
        >
          <ArgumentAxis />
          <ValueAxis max={7} />

          <BarSeries
            valueField="number"
            argumentField="place"
          />
          <Title text="أكثر الاماكن مخالفة" />
          <Animation />
        </Chart>
      </Paper>
    );
  }
}