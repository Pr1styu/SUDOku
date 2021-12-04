import { ActionType } from '../action-types';
import { AuthAction } from '../actions';
import IToken from '../../interfaces/token';
import IUser from '../../interfaces/user';

const tokenString = localStorage.getItem('user_token') || '{}';
const userString = localStorage.getItem('user_basic') || '{}';

const initialState = {
  isLoggedIn: false,
  user: JSON.parse(userString),
  token: JSON.parse(tokenString),
};

type authState = {
  isLoggedIn: boolean;
  user: IUser | null;
  token: IToken | null;
};

const reducer = (state: authState = initialState, action: AuthAction): authState => {
  switch (action.type) {
    case ActionType.LOGIN_SUCCESS:
      return {
        ...state,
        isLoggedIn: true,
        user: action.payload,
      };
    case ActionType.LOGIN_SUCCESS_OAUTH:
      return {
        ...state,
        isLoggedIn: true,
        token: action.payload,
      };
    case ActionType.LOGOUT:
      return {
        ...state,
        isLoggedIn: false,
        user: null,
        token: null,
      };
    default:
      return state;
  }
};

export default reducer;
