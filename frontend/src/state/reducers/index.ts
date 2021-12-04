import { combineReducers } from 'redux';
import authReducer from './authReducer';
import caffReducer from './caffReducer';
import infoMessageReducer from './infoMessageReducer';
import testReducer from './testReducer';
import userReducer from './userReducer';

const reducers = combineReducers({
  TEST: testReducer,
  INFO_MESSAGE: infoMessageReducer,
  AUTH: authReducer,
  USER: userReducer,
  CAFF: caffReducer,
});

export default reducers;
export type State = ReturnType<typeof reducers>;
