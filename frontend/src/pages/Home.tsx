import { Box, Button, Typography } from '@mui/material';
import { Link } from 'react-router-dom';
import { RouteComponentProps } from 'react-router';
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
              <Typography variant="body1" gutterBottom sx={{ mt: 2 }}>
                See your profile settings on the Profile page:
              </Typography>
              <Button variant="outlined" sx={{ mr: '1em' }} component={Link} to={'/profile'}>
                Profile
              </Button>
              <Typography variant="body1" gutterBottom sx={{ mt: 2 }}>
                Browse CAFF file images on the Browse CAFF fiels page:
              </Typography>
              <Button variant="outlined" sx={{ mr: '1em' }} component={Link} to={'/browse'}>
                Browse files
              </Button>
              <Typography variant="body1" gutterBottom sx={{ mt: 2 }}>
                Upload your CAFF file on the Add new CAFF file page:
              </Typography>
              <Button variant="outlined" sx={{ mr: '1em' }} component={Link} to={'/addcaff'}>
                Add file
              </Button>
              <Typography variant="body1" gutterBottom sx={{ mt: 2 }}>
                Are you done with your task? Sign out:
              </Typography>
              <Button variant="outlined" sx={{ mr: '1em' }} component={Link} to={'/logout'}>
                Sign out
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
