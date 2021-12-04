import { ActionType } from '../action-types';
import { MessageAction } from '../actions';

const initialState = '';

const reducer = (state: string = initialState, action: MessageAction): string => {
  switch (action.type) {
    case ActionType.SET_INFO_MESSAGE:
      return action.payload;
    case ActionType.CLEAR_INFO_MESSAGE:
      return initialState;
    default:
      return state;
  }
};

export default reducer;
