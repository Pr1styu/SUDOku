import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Box, Toolbar } from '@mui/material';
import { Route, RouteComponentProps, Switch, withRouter } from 'react-router-dom';
import { actionCreators } from './state';
import { bindActionCreators } from 'redux';
import { useDispatch } from 'react-redux';
import Menu from './components/Menu';
import React, { useEffect } from 'react';
import logging from './config/logging';
import routes from './config/routes';

const App: React.FC<RouteComponentProps> = (props) => {
  const dispatch = useDispatch();
  const { getUserData, getAllCaffFiles } = bindActionCreators(actionCreators, dispatch);

  useEffect(() => {
    logging.info('Loading Application...');
    getUserData();
    getAllCaffFiles();
  }, []);

  const loginAndRegisterPage = routes.filter(
    (route) => route.name === 'Login' || route.name === 'Register'
  );
  const showMenu =
    loginAndRegisterPage.filter((route) => route.path === props.location.pathname).length === 0;

  return (
    <Box sx={{ display: 'flex' }} className="app">
      {showMenu && <Menu name="menu" />}
      <Box
        component="main"
        sx={{
          backgroundColor: (theme) =>
            theme.palette.mode === 'light' ? theme.palette.grey[100] : theme.palette.grey[900],
          flexGrow: 1,
          height: '100vh',
          overflow: 'auto',
        }}
      >
        {showMenu && <Toolbar />}
        <Switch>
          {routes.map((route, index) => {
            return (
              <Route
                key={index}
                path={route.path}
                exact={route.exact}
                render={(props: RouteComponentProps<any>) => (
                  <route.component name={route.name} {...props} {...route.props} />
                )}
              />
            );
          })}
        </Switch>
      </Box>
    </Box>
  );
};

export default withRouter(App);
