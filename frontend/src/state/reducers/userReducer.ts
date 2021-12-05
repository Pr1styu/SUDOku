import { ActionType } from '../action-types';
import { UserAction } from '../actions';
import IUserData from '../../interfaces/userData';

const initialState = {
  username: '',
  fullName: '',
  email: '',
  userType: '',
  profilePicture: null,
  password: '',
};

const reducer = (state: IUserData = initialState, action: UserAction): IUserData => {
  switch (action.type) {
    case ActionType.GET_USER_DATA:
      return {
        ...state,
        username: action.payload.username ? action.payload.username : '',
        fullName: action.payload.fullName ? action.payload.fullName : '',
        email: action.payload.email ? action.payload.email : '',
        userType: action.payload.userType ? action.payload.userType : '',
        profilePicture: action.payload.profilePicture ? action.payload.profilePicture : '',
        password: action.payload.password ? action.payload.password : '',
      };
    case ActionType.EDIT_USER_DATA:
      return {
        ...state,
        [action.payload.name]: action.payload.value,
      };
    case ActionType.CLEAR_USER_DATA:
      return initialState;
    default:
      return state;
  }
};

export default reducer;
