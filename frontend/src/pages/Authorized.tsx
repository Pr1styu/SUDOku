import { Box, Paper, Typography } from '@mui/material';
import { Redirect, useLocation } from 'react-router-dom';
import { State, actionCreators } from '../state';
import { bindActionCreators } from 'redux';
import { useDispatch, useSelector } from 'react-redux';
import Container from '@mui/material/Container';
import Copyright from '../components/test/Copyright';
import Grid from '@mui/material/Grid';
import IComponent from '../interfaces/component';
import IToken from '../interfaces/token';
import React, { useEffect } from 'react';
import TokenService from '../services/TokenService';
import logging from '../config/logging';

const useQuery = () => {
  const { search } = useLocation();
  return React.useMemo(() => new URLSearchParams(search), [search]);
};

const Authorized: React.FC<IComponent> = () => {
  const query = useQuery();
  const dispatch = useDispatch();
  const { loginOauth } = bindActionCreators(actionCreators, dispatch);

  const isLoggedIn = useSelector((state: State) => state.AUTH.isLoggedIn);

  useEffect(() => {
    logging.info('Authorized, token granted!');
    const code = query.get('code');
    logging.info('Code valid: ' + !!code);
    if (code) {
      TokenService.getOauthToken(code).then((token: IToken) => {
        loginOauth(token);
      });
    }
  }, [isLoggedIn]);

  return (
    <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
      <Grid container spacing={3}>
        <Grid item xs={12}>
          <Paper sx={{ p: 2, display: 'flex', flexDirection: 'column' }}>
            <Box sx={{ ml: 2, mt: 2, mb: '2em' }}>
              <Typography variant="h4" gutterBottom component="div" sx={{ mb: '1em' }}>
                Authorized
              </Typography>
              <Typography variant="body1" gutterBottom component="div" sx={{ mb: '1em' }}>
                Successfully authorized!
              </Typography>
              <Redirect to="/home" />
            </Box>
          </Paper>
        </Grid>
      </Grid>
      <Copyright sx={{ pt: 4 }} />
    </Container>
  );
};

export default Authorized;
