import { ActionType } from '../action-types';
import { MessageAction } from '../actions';
import { VariantType } from 'notistack';

const initialState = {
  message: '',
  type: 'success' as VariantType,
};

type messageState = {
  message: string;
  type: VariantType;
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
