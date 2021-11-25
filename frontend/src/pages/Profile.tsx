import { Button, FormControl, TextField, Typography } from '@mui/material';
import { RouteComponentProps } from 'react-router';
import { State, actionCreators } from '../state';
import { bindActionCreators } from 'redux';
import { useDispatch, useSelector } from 'react-redux';
import Container from '@mui/material/Container';
import Copyright from '../components/test/Copyright';
import DateAdapter from '@mui/lab/AdapterMoment';
import DesktopDatePicker from '@mui/lab/DesktopDatePicker';
import Grid from '@mui/material/Grid';
import IComponent from '../interfaces/component';
import LocalizationProvider from '@mui/lab/LocalizationProvider';
import Paper from '@mui/material/Paper';
import React, { useEffect, useState } from 'react';

const Profile: React.FC<IComponent & RouteComponentProps<any>> = () => {
  const user = useSelector((state: State) => state.USER);

  const [registrationDate, setRegistrationDate] = useState(new Date());
  const [lastLoginDate, setLastLoginDate] = useState(new Date());

  const [editing, setEditing] = useState(false);
  const [edited, setEdited] = useState(false);

  const dispatch = useDispatch();
  const { editUserData, saveUserDataEdit } = bindActionCreators(actionCreators, dispatch);

  useEffect(() => {
    setRegistrationDate(
      user.registration_date
        ? new Date(user.registration_date[0], user.registration_date[1], user.registration_date[2])
        : new Date()
    );
    setLastLoginDate(
      user.registration_date
        ? new Date(user.last_login_date[0], user.last_login_date[1], user.last_login_date[2])
        : new Date()
    );
  }, [user.registration_date]);

  const handleRegistrationDateChange = (newValue: any) => {
    setRegistrationDate(newValue);
  };

  const handleLastLoginDateChange = (newValue: any) => {
    setLastLoginDate(newValue);
  };

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
                      value={user.username}
                      variant="standard"
                      disabled={!editing}
                      onChange={(e) => {
                        setEdited(true);
                        editUserData({
                          username: e.target.value,
                          displayname: user.displayname,
                          email: user.email,
                        });
                      }}
                    />
                  </FormControl>
                </Grid>
                <Grid item xs={3} />
                <Grid item xs={3}>
                  <Typography variant="h6" component="div" sx={{ textAlign: 'right' }}>
                    Nickname:
                  </Typography>
                </Grid>
                <Grid item xs={6}>
                  <FormControl fullWidth>
                    <TextField
                      value={user.displayname}
                      variant="standard"
                      disabled={!editing}
                      onChange={(e) => {
                        setEdited(true);
                        editUserData({
                          username: user.username,
                          displayname: e.target.value,
                          email: user.email,
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
                    <TextField value={user.role} variant="standard" disabled />
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
                          displayname: user.displayname,
                          email: e.target.value,
                        });
                      }}
                    />
                  </FormControl>
                </Grid>
                <Grid item xs={3} />
                <Grid item xs={3}>
                  <Typography variant="h6" component="div" sx={{ textAlign: 'right' }}>
                    Registration date:
                  </Typography>
                </Grid>
                <Grid item xs={6}>
                  <FormControl>
                    <DesktopDatePicker
                      disabled
                      mask={'____. __. __.'}
                      inputFormat="yyyy. MM. DD."
                      value={registrationDate}
                      onChange={(newValue) => handleRegistrationDateChange(newValue)}
                      renderInput={(params) => <TextField {...params} variant="outlined" />}
                    />
                  </FormControl>
                </Grid>
                <Grid item xs={3} />
                <Grid item xs={3}>
                  <Typography variant="h6" component="div" sx={{ textAlign: 'right' }}>
                    Last login date:
                  </Typography>
                </Grid>
                <Grid item xs={6}>
                  <FormControl>
                    <DesktopDatePicker
                      disabled
                      mask={'____. __. __.'}
                      inputFormat="yyyy. MM. DD."
                      value={lastLoginDate}
                      onChange={(newValue) => handleLastLoginDateChange(newValue)}
                      renderInput={(params) => <TextField {...params} variant="outlined" />}
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
                        displayname: user.displayname,
                        email: user.email,
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
