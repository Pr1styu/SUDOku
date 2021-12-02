import { Button, IconButton } from '@mui/material';
import { Link, NavLink } from 'react-router-dom';
import { State } from '../state';
import { styled } from '@mui/material/styles';
import { useSelector } from 'react-redux';
import AddPhotoAlternateIcon from '@mui/icons-material/AddPhotoAlternate';
import Box from '@mui/material/Box';
import ChevronLeftIcon from '@mui/icons-material/ChevronLeft';
import CreateIcon from '@mui/icons-material/Create';
import CssBaseline from '@mui/material/CssBaseline';
import Divider from '@mui/material/Divider';
import HomeIcon from '@mui/icons-material/Home';
import IComponent from '../interfaces/component';
import ImageIcon from '@mui/icons-material/Image';
import ImageSearchIcon from '@mui/icons-material/ImageSearch';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import ListSubheader from '@mui/material/ListSubheader';
import LockIcon from '@mui/icons-material/Lock';
import LoginIcon from '@mui/icons-material/Login';
import LogoutIcon from '@mui/icons-material/Logout';
import MenuIcon from '@mui/icons-material/Menu';
import MuiAppBar, { AppBarProps as MuiAppBarProps } from '@mui/material/AppBar';
import MuiDrawer from '@mui/material/Drawer';
import PersonIcon from '@mui/icons-material/Person';
import React, { useEffect, useState } from 'react';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';

const drawerWidth = 240;

interface AppBarProps extends MuiAppBarProps {
  open?: boolean;
}

const AppBar = styled(MuiAppBar, {
  shouldForwardProp: (prop) => prop !== 'open',
})<AppBarProps>(({ theme, open }) => ({
  zIndex: theme.zIndex.drawer + 1,
  transition: theme.transitions.create(['width', 'margin'], {
    easing: theme.transitions.easing.sharp,
    duration: theme.transitions.duration.leavingScreen,
  }),
  ...(open && {
    marginLeft: drawerWidth,
    width: `calc(100% - ${drawerWidth}px)`,
    transition: theme.transitions.create(['width', 'margin'], {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.enteringScreen,
    }),
  }),
}));

const Drawer = styled(MuiDrawer, { shouldForwardProp: (prop) => prop !== 'open' })(
  ({ theme, open }) => ({
    '& .MuiDrawer-paper': {
      position: 'relative',
      whiteSpace: 'nowrap',
      width: drawerWidth,
      transition: theme.transitions.create('width', {
        easing: theme.transitions.easing.sharp,
        duration: theme.transitions.duration.enteringScreen,
      }),
      boxSizing: 'border-box',
      ...(!open && {
        overflowX: 'hidden',
        transition: theme.transitions.create('width', {
          easing: theme.transitions.easing.sharp,
          duration: theme.transitions.duration.leavingScreen,
        }),
        width: theme.spacing(7),
        [theme.breakpoints.up('sm')]: {
          width: theme.spacing(9),
        },
      }),
    },
  })
);

const Menu: React.FC<IComponent> = () => {
  const { isLoggedIn } = useSelector((state: State) => state.AUTH);
  const [showUserBoard, setShowUserBoard] = useState(false);
  const [showAdminBoard, setShowAdminBoard] = useState(false);

  const user = useSelector((state: State) => state.USER);

  const [open, setOpen] = React.useState(true);
  const toggleDrawer = () => {
    setOpen(!open);
  };

  useEffect(() => {
    setShowAdminBoard(user.role === 'Admin' ?? false);
    setShowUserBoard((user.role === 'Admin' || user.role === 'Normal') ?? false);
  }, [user]);

  const menuElementsHome = [
    {
      path: '/home',
      name: 'Home',
      icon: <HomeIcon />,
      show: true,
    },
  ];

  const menuElementsUser = [
    {
      path: '/profile',
      name: 'Profile',
      icon: <PersonIcon />,
      show: isLoggedIn,
    },
    {
      path: '/login',
      name: 'Sign in',
      icon: <LoginIcon />,
      show: !isLoggedIn,
    },
    {
      path: '/register',
      name: 'Sign up',
      icon: <CreateIcon />,
      show: !isLoggedIn,
    },
  ];

  const menuElementsCaff = [
    {
      path: '/browse',
      name: 'Browse CAFF files',
      icon: <ImageSearchIcon />,
      show: isLoggedIn,
    },
    {
      path: '/addcaff',
      name: 'Add new CAFF file',
      icon: <AddPhotoAlternateIcon />,
      show: isLoggedIn,
    },
  ];

  const menuElementsAdmin = [
    {
      path: '/admin',
      name: 'Admin Board',
      icon: <LockIcon />,
      show: isLoggedIn && showAdminBoard,
    },
  ];

  const logoutElement = {
    path: '/logout',
    name: 'Sign out',
    icon: <LogoutIcon />,
    show: isLoggedIn,
  };

  return (
    <Box>
      <CssBaseline />
      <AppBar position="absolute" open={open}>
        <Toolbar
          sx={{
            pr: '24px', // keep right padding when drawer closed
          }}
        >
          <IconButton
            edge="start"
            color="inherit"
            aria-label="open drawer"
            onClick={toggleDrawer}
            sx={{
              marginRight: '36px',
              ...(open && { display: 'none' }),
            }}
          >
            <MenuIcon />
          </IconButton>
          <Typography component="h1" variant="h6" color="inherit" noWrap sx={{ flexGrow: 1 }}>
            Welcome {user?.username ?? 'Guest'}!
          </Typography>
          {isLoggedIn ? (
            <Button
              sx={{ color: 'white', borderColor: 'white', borderWidth: '1.5px' }}
              variant="outlined"
              component={Link}
              to={'/logout'}
            >
              Sign out
            </Button>
          ) : (
            <div>
              <Button
                sx={{ color: 'white', borderColor: 'white', borderWidth: '1.5px', mr: '10px' }}
                variant="outlined"
                component={Link}
                to={'/register'}
              >
                SIGN UP
              </Button>
              <Button
                sx={{ color: 'white', borderColor: 'white', borderWidth: '1.5px' }}
                variant="outlined"
                component={Link}
                to={'/login'}
              >
                SIGN IN
              </Button>
            </div>
          )}
        </Toolbar>
      </AppBar>
      <Drawer variant="permanent" open={open}>
        <Toolbar
          sx={{
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'flex-end',
            px: [1],
          }}
        >
          <ImageIcon />
          <Typography
            component="h1"
            variant="h6"
            color="inherit"
            sx={{ flexGrow: 1, fontSize: '20 px', marginLeft: '1em' }}
          >
            CAFFViewer
          </Typography>
          <IconButton onClick={toggleDrawer}>
            <ChevronLeftIcon />
          </IconButton>
        </Toolbar>
        <Divider />
        <List>
          <ListSubheader inset>Public</ListSubheader>
          {menuElementsHome.map(
            (element) =>
              element.show && (
                <ListItem button key={element.name} component={NavLink} to={element.path}>
                  <ListItemIcon>{element.icon}</ListItemIcon>
                  <ListItemText primary={element.name} />
                </ListItem>
              )
          )}
        </List>
        <Divider />
        <List>
          <ListSubheader inset>User</ListSubheader>
          {menuElementsUser.map(
            (element) =>
              element.show && (
                <ListItem button key={element.name} component={NavLink} to={element.path}>
                  <ListItemIcon>{element.icon}</ListItemIcon>
                  <ListItemText primary={element.name} />
                </ListItem>
              )
          )}
        </List>
        <List>
          {menuElementsCaff[0].show && (
            <div>
              <Divider /> <ListSubheader inset>CAFF</ListSubheader>
            </div>
          )}
          {menuElementsCaff.map(
            (element) =>
              element.show && (
                <ListItem button key={element.name} component={NavLink} to={element.path}>
                  <ListItemIcon>{element.icon}</ListItemIcon>
                  <ListItemText primary={element.name} />
                </ListItem>
              )
          )}
        </List>
        <List>
          {menuElementsAdmin[0].show && (
            <div>
              <Divider /> <ListSubheader inset>Administrator</ListSubheader>
            </div>
          )}
          {menuElementsAdmin.map(
            (element) =>
              element.show && (
                <ListItem button key={element.name} component={NavLink} to={element.path}>
                  <ListItemIcon>{element.icon}</ListItemIcon>
                  <ListItemText primary={element.name} />
                </ListItem>
              )
          )}
        </List>
        <List>
          {logoutElement.show && (
            <div>
              <Divider />
              <ListItem button key={logoutElement.name} component={NavLink} to={logoutElement.path}>
                <ListItemIcon>{logoutElement.icon}</ListItemIcon>
                <ListItemText primary={logoutElement.name} />
              </ListItem>
            </div>
          )}
        </List>
      </Drawer>
    </Box>
  );
};

export default Menu;
