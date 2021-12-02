import { ActionType } from '../action-types';
import { AuthAction } from '../actions';
import IUser from '../../interfaces/userData';
import config from '../../config/config';

const authType = config.authType;
const userString =
  authType === 'OAUTH' ? localStorage.getItem('user_token') : localStorage.getItem('user_basic');
const user: (IUser & { token: string }) | null = userString ? JSON.parse(userString) : null;

const initialState = user ? { isLoggedIn: true, user } : { isLoggedIn: false, user: null };
type authState = typeof initialState;

const reducer = (state: authState = initialState, action: AuthAction): authState => {
  switch (action.type) {
    case ActionType.REGISTER_SUCCESS:
      return {
        ...state,
        isLoggedIn: false,
      };
    case ActionType.REGISTER_FAIL:
      return {
        ...state,
        isLoggedIn: false,
      };
    case ActionType.LOGIN_SUCCESS:
      return {
        ...state,
        isLoggedIn: true,
        user: action.payload.user,
      };
    case ActionType.LOGIN_SUCCESS_OAUTH:
      return {
        ...state,
        isLoggedIn: true,
      };
    case ActionType.LOGIN_FAIL:
      return {
        ...state,
        isLoggedIn: false,
        user: null,
      };
    case ActionType.LOGOUT:
      return {
        ...state,
        isLoggedIn: false,
        user: null,
      };
    default:
      return state;
  }
};

export default reducer;
