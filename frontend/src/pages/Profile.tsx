import { Button, FormControl, TextField, Typography } from '@mui/material';
import { RouteComponentProps } from 'react-router';
import { State, actionCreators } from '../state';
import { bindActionCreators } from 'redux';
import { useDispatch, useSelector } from 'react-redux';
import Container from '@mui/material/Container';
import Copyright from '../components/test/Copyright';
import DateAdapter from '@mui/lab/AdapterMoment';
import Grid from '@mui/material/Grid';
import IComponent from '../interfaces/component';
import LocalizationProvider from '@mui/lab/LocalizationProvider';
import Paper from '@mui/material/Paper';
import React, { useState } from 'react';

const Profile: React.FC<IComponent & RouteComponentProps<any>> = () => {
  const user = useSelector((state: State) => state.USER);

  const [editing, setEditing] = useState(false);
  const [edited, setEdited] = useState(false);

  const dispatch = useDispatch();
  const { editUserData, saveUserDataEdit } = bindActionCreators(actionCreators, dispatch);

  const basicAuthUserStr = localStorage.getItem('user');
  const basicAuthUserName = basicAuthUserStr ? JSON.parse(basicAuthUserStr).username : 'Unknown';

  return (
    <LocalizationProvider dateAdapter={DateAdapter}>
      <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
        <Grid container spacing={3}>
          <Grid item xs={12}>
            <Paper sx={{ p: 2, display: 'flex', flexDirection: 'column' }}>
              <Typography variant="h3" gutterBottom component="div" sx={{ ml: 2, mt: 2 }}>
                Profile
              </Typography>
              <Grid container spacing={3} sx={{ mt: 2, mb: 2 }}>
                <Grid item xs={3}>
                  <Typography variant="h6" component="div" sx={{ textAlign: 'right' }}>
                    Username:
                  </Typography>
                </Grid>
                <Grid item xs={6}>
                  <FormControl fullWidth disabled>
                    <TextField
                      value={user.username ?? basicAuthUserName}
                      variant="standard"
                      disabled={!editing}
                      onChange={(e) => {
                        setEdited(true);
                        editUserData({
                          username: e.target.value,
                          fullName: user.fullName,
                          email: user.email,
                          userType: user.userType,
                          profilePicture: user.profilePicture,
                        });
                      }}
                    />
                  </FormControl>
                </Grid>
                <Grid item xs={3} />
                <Grid item xs={3}>
                  <Typography variant="h6" component="div" sx={{ textAlign: 'right' }}>
                    Full Name:
                  </Typography>
                </Grid>
                <Grid item xs={6}>
                  <FormControl fullWidth>
                    <TextField
                      value={user.fullName}
                      variant="standard"
                      disabled={!editing}
                      onChange={(e) => {
                        setEdited(true);
                        editUserData({
                          username: user.username,
                          fullName: e.target.value,
                          email: user.email,
                          userType: user.userType,
                          profilePicture: user.profilePicture,
                        });
                      }}
                    />
                  </FormControl>
                </Grid>
                <Grid item xs={3} />
                <Grid item xs={3}>
                  <Typography variant="h6" component="div" sx={{ textAlign: 'right' }}>
                    Role:
                  </Typography>
                </Grid>
                <Grid item xs={6}>
                  <FormControl fullWidth>
                    <TextField value={user.userType} variant="standard" disabled />
                  </FormControl>
                </Grid>
                <Grid item xs={3} />
                <Grid item xs={3}>
                  <Typography variant="h6" component="div" sx={{ textAlign: 'right' }}>
                    Email address:
                  </Typography>
                </Grid>
                <Grid item xs={6}>
                  <FormControl fullWidth>
                    <TextField
                      value={user.email}
                      variant="standard"
                      disabled={!editing}
                      onChange={(e) => {
                        setEdited(true);
                        editUserData({
                          username: user.username,
                          fullName: user.fullName,
                          email: e.target.value,
                          userType: user.userType,
                          profilePicture: user.profilePicture,
                        });
                      }}
                    />
                  </FormControl>
                </Grid>
                <Grid item xs={3} />
                <Grid item xs={3} sx={{ textAlign: 'right', mt: '4em' }}>
                  <Button variant="contained" size="large" onClick={() => setEditing(!editing)}>
                    {editing ? 'Stop editing' : 'Edit profile'}
                  </Button>
                </Grid>
                <Grid item xs={3} sx={{ textAlign: 'left', mt: '4em' }}>
                  <Button
                    color="secondary"
                    variant="contained"
                    size="large"
                    onClick={() => {
                      saveUserDataEdit({
                        username: user.username,
                        fullName: user.fullName,
                        email: user.username,
                        userType: user.userType,
                        profilePicture: user.profilePicture,
                      });
                      setEditing(false);
                    }}
                    disabled={!edited}
                  >
                    Save modifications
                  </Button>
                </Grid>
                <Grid item xs={3} />
              </Grid>
            </Paper>
          </Grid>
        </Grid>
        <Copyright sx={{ pt: 4 }} />
      </Container>
    </LocalizationProvider>
  );
};

export default Profile;
