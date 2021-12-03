import { ActionType } from '../action-types';
import { UserAction } from '../actions';

const initialState = {};

const reducer = (state: any = initialState, action: UserAction): any => {
  switch (action.type) {
    case ActionType.GET_USER_DATA:
      return action.payload;
    case ActionType.CLEAR_USER_DATA:
      return initialState;
    case ActionType.EDIT_USER_DATA:
      return {
        ...state,
        username: action.payload.username,
        fullName: action.payload.fullName,
        email: action.payload.email,
      };
    default:
      return state;
  }
};

export default reducer;
