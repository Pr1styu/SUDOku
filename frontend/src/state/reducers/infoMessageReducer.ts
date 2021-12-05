import { ActionType } from '../action-types';
import { MessageAction } from '../actions';

const initialState = {
  message: '',
  type: '',
};

type messageState = {
  message: string;
  type: string;
};

const reducer = (state: messageState = initialState, action: MessageAction): messageState => {
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
