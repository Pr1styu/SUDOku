import { Box, Button, Paper, Typography } from '@mui/material';
import { Link } from 'react-router-dom';
import { actionCreators } from '../state';
import { bindActionCreators } from 'redux';
import { useCookies } from 'react-cookie';
import { useDispatch } from 'react-redux';
import Container from '@mui/material/Container';
import Copyright from '../components/test/Copyright';
import Grid from '@mui/material/Grid';
import IComponent from '../interfaces/component';
import React, { useEffect } from 'react';

const Logout: React.FC<IComponent> = () => {
  const dispatch = useDispatch();
  const { logout, clearUserData } = bindActionCreators(actionCreators, dispatch);

  const [, , removeCookie] = useCookies(['JSESSIONID']);

  useEffect(() => {
    logout();
    removeCookie('JSESSIONID');
    clearUserData();
  }, []);

  return (
    <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
      <Grid container spacing={3}>
        <Grid item xs={12}>
          <Paper sx={{ p: 2, display: 'flex', flexDirection: 'column' }}>
            <Box sx={{ ml: 2, mt: 2, mb: '2em' }}>
              <Typography variant="h4" gutterBottom component="div" sx={{ mb: '1em' }}>
                Successfully logged out!
              </Typography>
              <Typography variant="h5" gutterBottom component="div">
                Want to log in again?
              </Typography>
              <Button variant="contained" sx={{ mr: '1em' }} component={Link} to={'/login'}>
                Go to login page
              </Button>
            </Box>
          </Paper>
        </Grid>
      </Grid>
      <Copyright sx={{ pt: 4 }} />
    </Container>
  );
};

export default Logout;
