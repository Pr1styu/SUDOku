import { Box, Paper, Typography } from '@mui/material';
import { actionCreators } from '../state';
import { bindActionCreators } from 'redux';
import { useDispatch } from 'react-redux';
import { useLocation } from 'react-router-dom';
import CaffService from '../services/CaffService';
import Container from '@mui/material/Container';
import Copyright from '../components/test/Copyright';
import Grid from '@mui/material/Grid';
import IComponent from '../interfaces/component';
import React, { useEffect } from 'react';

const useQuery = () => {
  const { search } = useLocation();
  return React.useMemo(() => new URLSearchParams(search), [search]);
};

const Authorized: React.FC<IComponent> = () => {
  const query = useQuery();
  const dispatch = useDispatch();
  const { loginOauth } = bindActionCreators(actionCreators, dispatch);

  useEffect(() => {
    const code = query.get('code');
    code &&
      CaffService.getOauthToken(code).then(() => {
        loginOauth();
      });
  }, []);

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
            </Box>
          </Paper>
        </Grid>
      </Grid>
      <Copyright sx={{ pt: 4 }} />
    </Container>
  );
};

export default Authorized;
