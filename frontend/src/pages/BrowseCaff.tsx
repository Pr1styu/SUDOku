import { Box, FormControl, TextField, Typography } from '@mui/material';
import { RouteComponentProps } from 'react-router';
import { State } from '../state';
import { useSelector } from 'react-redux';
import Caff from '../components/Caff';
import Container from '@mui/material/Container';
import Copyright from '../components/test/Copyright';
import Grid from '@mui/material/Grid';
import IComponent from '../interfaces/component';
import Paper from '@mui/material/Paper';
import React, { useState } from 'react';

const BrowseCaff: React.FC<IComponent & RouteComponentProps<any>> = () => {
  const [name, setName] = useState('');
  const [tag, setTag] = useState('');

  const caffStore = useSelector((state: State) => state.CAFF);

  return (
    <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
      <Grid container spacing={3}>
        <Grid item xs={12}>
          <Paper sx={{ p: 2, display: 'flex', flexDirection: 'column' }}>
            <Box sx={{ ml: 2, mt: 2, pb: '5em' }}>
              <Typography variant="h3" gutterBottom component="div">
                Browse Caff files
              </Typography>
              <Grid container spacing={3} sx={{ mt: 2, mb: 2 }}>
                <Grid item xs={3}>
                  <Typography variant="h6" component="div">
                    Name
                  </Typography>
                  <FormControl fullWidth>
                    <TextField
                      name="name"
                      value={name}
                      variant="standard"
                      onChange={(e) => {
                        setName(e.target.value);
                      }}
                      sx={{ mt: '1.4em' }}
                    />
                  </FormControl>
                </Grid>
                <Grid item xs={3}>
                  <Typography variant="h6" component="div">
                    Tag
                  </Typography>
                  <FormControl fullWidth disabled>
                    <TextField
                      name="tag"
                      value={tag}
                      variant="standard"
                      onChange={(e) => {
                        setTag(e.target.value);
                      }}
                      sx={{ mt: '1.4em' }}
                    />
                  </FormControl>
                </Grid>
                <Grid item xs={3} />
              </Grid>
              {caffStore.caff_files?.map((caff, index) =>
                tag || name ? (
                  caff.metaData?.includes(tag) || caff.fileName?.match(name) ? (
                    <Caff key={index} name={caff.fileName} {...caff} />
                  ) : (
                    ''
                  )
                ) : (
                  <Caff key={index} name={caff.fileName} {...caff} />
                )
              )}
            </Box>
          </Paper>
        </Grid>
      </Grid>
      <Copyright sx={{ pt: 4 }} />
    </Container>
  );
};

export default BrowseCaff;
