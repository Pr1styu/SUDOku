import * as React from 'react';
import { Alert } from '@mui/material';
import { RouteComponentProps } from 'react-router';
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
  const [successful, setSuccessful] = useState(false);
  const [error, setError] = useState(false);
  const [errorMessage, setErrorMessage] = useState('Failed to create new user.');
  const [loading, setLoading] = useState(false);

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);

    setSuccessful(false);
    setLoading(true);

    AuthService.register({
      username: data.get('username')?.toString() ?? '',
      fullName: data.get('fullName')?.toString() ?? '',
      email: data.get('email')?.toString() ?? '',
      password: data.get('password')?.toString() ?? '',
    }).then(
      () => {
        setSuccessful(true);
      },
      (error) => {
        setSuccessful(false);
        setLoading(false);
        setError(true);
        const errorMessage = error.response?.data || 'Error: Could not create account.';
        errorMessage && setErrorMessage(errorMessage);
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
                autoComplete="name"
                id="fullName"
                label="Full Name"
                name="fullName"
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
                name="password"
                label="Password Again"
                type="password"
                id="passwordAgain"
                autoComplete="new-password"
              />
            </Grid>
            {successful && (
              <Grid item xs={12}>
                <Alert severity="success">
                  <Typography
                    variant="body1"
                    component="div"
                    sx={{ color: 'green', fontWeight: 'bold' }}
                  >
                    Registration successful! You can <Link href="/login">sing in </Link>.
                  </Typography>
                </Alert>
              </Grid>
            )}
            {error && (
              <Grid item xs={12}>
                <Alert severity="error">
                  <Typography
                    variant="body1"
                    component="div"
                    sx={{ color: 'red', fontWeight: 'bold' }}
                  >
                    {errorMessage}
                  </Typography>
                </Alert>
              </Grid>
            )}
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
