import { Redirect, RouteComponentProps } from 'react-router';
import { State } from '../state';
//import { setAuthMessage } from '../state/action-creators';
import { useSelector } from 'react-redux';
//import AuthService from '../services/AuthService';
import Avatar from '@mui/material/Avatar';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Checkbox from '@mui/material/Checkbox';
import CircularProgress from '@mui/material/CircularProgress';
import Copyright from '../components/test/Copyright';
import CssBaseline from '@mui/material/CssBaseline';
import FormControlLabel from '@mui/material/FormControlLabel';
import Grid from '@mui/material/Grid';
import IComponent from '../interfaces/component';
import Link from '@mui/material/Link';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Paper from '@mui/material/Paper';
import React, { useState } from 'react';
import TextField from '@mui/material/TextField';
import Typography from '@mui/material/Typography';

const Login: React.FC<IComponent & RouteComponentProps<any>> = (props) => {
  const [loading, setLoading] = useState(false);

  const { isLoggedIn } = useSelector((state: State) => state.AUTH);

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);

    setLoading(true);

    // Basic login for the time being
    // TODO: Delete this (Http Basic login with admin)
    localStorage.setItem(
      'user',
      JSON.stringify({ username: data.get('username'), password: data.get('password') })
    );
    props.history.push('/profile');
    window.location.reload();

    // TODO: Jwt login
    /*
    AuthService.login(
      data.get('username')?.toString() ?? '',
      data.get('password')?.toString() ?? ''
    ).then(
      () => {
        props.history.push('/profile');
        window.location.reload();
      },
      (error) => {
        const resMessage = error.response?.data?.message ?? error.message ?? error.toString();
        setLoading(false);
        setAuthMessage(resMessage);
      }
    );*/
  };

  if (isLoggedIn) {
    return <Redirect to="/profile" />;
  }

  return (
    <Grid container component="main" sx={{ height: '100vh' }}>
      <CssBaseline />
      <Grid
        item
        xs={false}
        sm={4}
        md={7}
        sx={{
          backgroundImage: 'url(https://source.unsplash.com/featured/?cybersecurity)',
          backgroundRepeat: 'no-repeat',
          backgroundColor: (t) =>
            t.palette.mode === 'light' ? t.palette.grey[50] : t.palette.grey[900],
          backgroundSize: 'cover',
          backgroundPosition: 'center',
        }}
      />
      <Grid item xs={12} sm={8} md={5} component={Paper} elevation={6} square>
        <Box
          sx={{
            my: 8,
            mx: 4,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
          }}
        >
          <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
            <LockOutlinedIcon />
          </Avatar>
          <Typography component="h1" variant="h5">
            Sign in
          </Typography>
          <Box component="form" noValidate onSubmit={handleSubmit} sx={{ mt: 1 }}>
            <TextField
              margin="normal"
              required
              fullWidth
              id="username"
              label="Username"
              name="username"
              autoComplete="username"
              autoFocus
            />
            <TextField
              margin="normal"
              required
              fullWidth
              name="password"
              label="Password"
              type="password"
              id="password"
              autoComplete="current-password"
            />

            <Button type="submit" fullWidth variant="contained" sx={{ mt: 3, mb: 2 }}>
              {loading ? <CircularProgress /> : 'Sign In'}
            </Button>
            <Grid container>
              <Grid item xs></Grid>
              <Grid item>
                <Link href="/register" variant="body2">
                  {"Don't have an account? Sign Up"}
                </Link>
              </Grid>
            </Grid>
            <Copyright sx={{ mt: 5 }} />
          </Box>
        </Box>
      </Grid>
    </Grid>
  );
};

export default Login;
