import { ActionType } from '../action-types';
import { MessageAction } from '../actions';

const initialState = '';

const reducer = (state: string = initialState, action: MessageAction): string => {
  switch (action.type) {
    case ActionType.SET_AUTH_MESSAGE:
      return action.payload;
    case ActionType.CLEAR_AUTH_MESSAGE:
      return initialState;
    default:
      return state;
  }
};

export default reducer;
