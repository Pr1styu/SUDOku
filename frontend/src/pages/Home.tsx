import { Box, Button, Typography } from '@mui/material';
import { Link } from 'react-router-dom';
import { RouteComponentProps } from 'react-router';
import CaffService from '../services/CaffService';
import Container from '@mui/material/Container';
import Copyright from '../components/test/Copyright';
import Grid from '@mui/material/Grid';
import IComponent from '../interfaces/component';
import Paper from '@mui/material/Paper';
import React from 'react';

const Home: React.FC<IComponent & RouteComponentProps<any>> = () => {
  return (
    <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
      <Grid container spacing={3}>
        <Grid item xs={12}>
          <Paper sx={{ p: 2, display: 'flex', flexDirection: 'column' }}>
            <Box sx={{ ml: 2, mt: 2, pb: '5em' }}>
              <Typography variant="h3" gutterBottom component="div">
                Home
              </Typography>
              <Typography variant="h6" gutterBottom>
                Welcome to the home page!
              </Typography>
              <Button variant="contained" sx={{ mr: '1em' }} component={Link} to={'/test'}>
                Test page
              </Button>
              <Button variant="contained" sx={{ mr: '1em' }} component={Link} to={'/muitest'}>
                Material UI test page
              </Button>
              <Button variant="contained" sx={{ mr: '1em' }} component={Link} to={'/404'}>
                404 page
              </Button>
              <Button
                variant="contained"
                sx={{ mr: '1em' }}
                onClick={() => {
                  CaffService.oauth();
                }}
              >
                OAuth
              </Button>
            </Box>
          </Paper>
        </Grid>
      </Grid>
      <Copyright sx={{ pt: 4 }} />
    </Container>
  );
};

export default Home;
