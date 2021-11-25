import * as React from 'react';
import { RouteComponentProps } from 'react-router';
import { State, actionCreators } from '../state';
import { bindActionCreators } from 'redux';
import { useDispatch, useSelector } from 'react-redux';
import { useState } from 'react';
import AuthService from '../services/AuthService';
import Avatar from '@mui/material/Avatar';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import CircularProgress from '@mui/material/CircularProgress';
import Container from '@mui/material/Container';
import Copyright from '../components/test/Copyright';
import CssBaseline from '@mui/material/CssBaseline';
import Grid from '@mui/material/Grid';
import IComponent from '../interfaces/component';
import Link from '@mui/material/Link';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import TextField from '@mui/material/TextField';
import Typography from '@mui/material/Typography';

const Register: React.FC<IComponent & RouteComponentProps<any>> = () => {
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  const [successful, setSuccessful] = useState(false);
  const [loading, setLoading] = useState(false);

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  const authMessage = useSelector((state: State) => state.AUTH_MESSAGE);

  const dispatch = useDispatch();
  const { setAuthMessage } = bindActionCreators(actionCreators, dispatch);

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);

    setAuthMessage('');
    setSuccessful(false);
    setLoading(true);

    AuthService.register(
      data.get('username')?.toString() ?? '',
      data.get('password')?.toString() ?? '',
      data.get('passwordAgain')?.toString() ?? '',
      data.get('dispalyName')?.toString() ?? '',
      data.get('email')?.toString() ?? ''
    ).then(
      (response) => {
        setAuthMessage(response.data.message);
        setSuccessful(true);
      },
      (error) => {
        const resMessage = error.response?.data?.message ?? error.message ?? error.toString();

        setAuthMessage(resMessage);
        setSuccessful(false);
        setLoading(false);
      }
    );
  };

  return (
    <Container component="main" maxWidth="xs">
      <CssBaseline />
      <Box
        sx={{
          marginTop: 8,
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
        }}
      >
        <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
          <LockOutlinedIcon />
        </Avatar>
        <Typography component="h1" variant="h5">
          Sign up
        </Typography>
        <Box component="form" noValidate onSubmit={handleSubmit} sx={{ mt: 3 }}>
          <Grid container spacing={2}>
            <Grid item xs={12} sm={6}>
              <TextField
                autoComplete="username"
                name="username"
                required
                fullWidth
                id="username"
                label="Username"
                autoFocus
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                required
                fullWidth
                id="nickname"
                label="Nickname"
                name="nickname"
                autoComplete="nickname"
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                required
                fullWidth
                id="email"
                label="Email Address"
                name="email"
                autoComplete="email"
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                required
                fullWidth
                name="password"
                label="Password"
                type="password"
                id="password"
                autoComplete="new-password"
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                required
                fullWidth
                name="passwordAgain"
                label="Password Again"
                type="passwordAgain"
                id="passwordAgain"
                autoComplete="new-password"
              />
            </Grid>
          </Grid>
          <Button type="submit" fullWidth variant="contained" sx={{ mt: 3, mb: 2 }}>
            {loading ? <CircularProgress /> : 'Sign Up'}
          </Button>
          <Grid container justifyContent="flex-end">
            <Grid item>
              <Link href="/login" variant="body2">
                Already have an account? Sign in
              </Link>
            </Grid>
          </Grid>
        </Box>
      </Box>
      <Copyright sx={{ mt: 5 }} />
    </Container>
  );
};

export default Register;
