import { RouteComponentProps } from 'react-router';
import Chart from '../../components/test/Chart';
import Container from '@mui/material/Container';
import Copyright from '../../components/test/Copyright';
import Deposits from '../../components/test/Deposits';
import Grid from '@mui/material/Grid';
import IComponent from '../../interfaces/component';
import Orders from '../../components/test/Orders';
import Paper from '@mui/material/Paper';
import React from 'react';

const MuiTest: React.FC<IComponent & RouteComponentProps<any>> = () => {
  return (
    <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
      <Grid container spacing={3}>
        {/* Chart */}
        <Grid item xs={12} md={8} lg={9}>
          <Paper
            sx={{
              p: 2,
              display: 'flex',
              flexDirection: 'column',
              height: 240,
            }}
          >
            <Chart name="Example chart" />
          </Paper>
        </Grid>
        {/* Recent Deposits */}
        <Grid item xs={12} md={4} lg={3}>
          <Paper
            sx={{
              p: 2,
              display: 'flex',
              flexDirection: 'column',
              height: 240,
            }}
          >
            <Deposits name="Example deposits" />
          </Paper>
        </Grid>
        {/* Recent Orders */}
        <Grid item xs={12}>
          <Paper sx={{ p: 2, display: 'flex', flexDirection: 'column' }}>
            <Orders name="Example orders" />
          </Paper>
        </Grid>
      </Grid>
      <Copyright sx={{ pt: 4 }} />
    </Container>
  );
};

export default MuiTest;
