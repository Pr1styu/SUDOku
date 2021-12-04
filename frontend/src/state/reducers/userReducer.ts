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
      return action.payload;
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
