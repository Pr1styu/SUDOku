import AdminBoard from '../pages/test/AdminBoard';
import BrowseCaff from '../pages/BrowseCaff';
import HomePage from '../pages/Home';
import IRoute from '../interfaces/route';
import LoginPage from '../pages/Login';
import LogoutPage from '../pages/Logout';
import MuiTest from '../pages/test/MuiTest';
import NotFoundPage from '../pages/NotFound';
import ProfilePage from '../pages/Profile';
import RegisterPage from '../pages/Register';
import Test from '../pages/test/Test';
import UserBoard from '../pages/test/UserBoard';

const routes: IRoute[] = [
  {
    path: '/',
    name: 'Home',
    component: HomePage,
    exact: true,
  },
  {
    path: '/home',
    name: 'Home',
    component: HomePage,
    exact: true,
  },
  {
    path: '/register',
    name: 'Register',
    component: RegisterPage,
    exact: true,
  },
  {
    path: '/login',
    name: 'Login',
    component: LoginPage,
    exact: true,
  },
  {
    path: '/logout',
    name: 'Logout',
    component: LogoutPage,
    exact: true,
  },
  {
    path: '/profile',
    name: 'Profile',
    component: ProfilePage,
    exact: true,
  },
  {
    path: '/user',
    name: 'UserBoard',
    component: UserBoard,
    exact: true,
  },
  {
    path: '/admin',
    name: 'AdminBoard',
    component: AdminBoard,
    exact: true,
  },
  {
    path: '/test',
    name: 'TestPage',
    component: Test,
    exact: true,
  },
  {
    path: '/muitest',
    name: 'MuiTestPage',
    component: MuiTest,
    exact: true,
  },
  {
    path: '/caff',
    name: 'BrowseCaffPage',
    component: BrowseCaff,
    exact: true,
  },
  {
    path: '',
    name: 'Not found',
    component: NotFoundPage,
    exact: false,
  },
];

export default routes;
