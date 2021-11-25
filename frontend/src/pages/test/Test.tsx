import { Box, Button, Container, Typography } from '@mui/material';
import { State, actionCreators } from '../../state';
import { bindActionCreators } from 'redux';
import { useDispatch, useSelector } from 'react-redux';
import Copyright from '../../components/test/Copyright';
import Grid from '@mui/material/Grid';
import IPage from '../../interfaces/page';
import Paper from '@mui/material/Paper';
import React from 'react';

const TestPage: React.FC<IPage> = () => {
  const authUser = useSelector((state: State) => state.AUTH.user);
  const user = useSelector((state: State) => state.USER);

  const dispatch = useDispatch();
  const { depositMoney, withdrawMoney, bankrupt } = bindActionCreators(actionCreators, dispatch);

  const amount = useSelector((state: State) => state.TEST);

  return (
    <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
      <Grid container spacing={3}>
        <Grid item xs={12}>
          <Paper sx={{ p: 2, display: 'flex', flexDirection: 'column' }}>
            <Box sx={{ ml: 2, mt: 2 }}>
              <Typography variant="h3" gutterBottom component="div">
                Test page
              </Typography>
              <Typography variant="body1" gutterBottom>
                Welcome <strong>{user?.username ?? 'guest'}</strong>!
              </Typography>
              <Typography variant="body1" gutterBottom>
                This is just a test page.
              </Typography>
              <Typography variant="h5" gutterBottom sx={{ mt: '2em' }}>
                Your access token is the following:
              </Typography>
              <Paper sx={{ wordWrap: 'break-word', p: '1em', mt: '1em', mb: '2em' }}>
                {authUser?.token ?? 'unavailable'}
              </Paper>
              <Typography variant="h5" gutterBottom sx={{ mt: '2em' }}>
                Redux state test:
              </Typography>
              <Paper sx={{ p: '1em', mt: '1em', mb: '2em' }}>
                <Typography variant="h6" gutterBottom>
                  {`Current money: ${amount} $`}
                </Typography>
                <Button variant="contained" sx={{ mr: '1em' }} onClick={() => depositMoney(1000)}>
                  Deposit
                </Button>
                <Button variant="contained" sx={{ mr: '1em' }} onClick={() => withdrawMoney(100)}>
                  Withdraw
                </Button>
                <Button variant="contained" sx={{ mr: '1em' }} onClick={() => bankrupt()}>
                  Bankrupt
                </Button>
              </Paper>
            </Box>
          </Paper>
        </Grid>
      </Grid>
      <Copyright sx={{ pt: 4 }} />
    </Container>
  );
};

export default TestPage;
