import { Box, Typography } from '@mui/material';
import { RouteComponentProps } from 'react-router';
import Container from '@mui/material/Container';
import Copyright from '../../components/test/Copyright';
import Grid from '@mui/material/Grid';
import IComponent from '../../interfaces/component';
import Paper from '@mui/material/Paper';
import React from 'react';

const AdminBoard: React.FC<IComponent & RouteComponentProps<any>> = () => {
  return (
    <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
      <Grid container spacing={3}>
        <Grid item xs={12}>
          <Paper sx={{ p: 2, display: 'flex', flexDirection: 'column' }}>
            <Box sx={{ ml: 2, mt: 2, pb: '20em' }}>
              <Typography variant="h3" gutterBottom component="div">
                Admin Board
              </Typography>
              <Typography variant="h6" gutterBottom>
                Welcome to the admin board!
              </Typography>
              <Typography variant="body1" gutterBottom>
                Only admins have rights to view this page.
              </Typography>
            </Box>
          </Paper>
        </Grid>
      </Grid>
      <Copyright sx={{ pt: 4 }} />
    </Container>
  );
};

export default AdminBoard;
