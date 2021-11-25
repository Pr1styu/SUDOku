import { combineReducers } from 'redux';
import authMessageReducer from './authMessageReducer';
import authReducer from './authReducer';
import caffReducer from './caffReducer';
import testReducer from './testReducer';
import userReducer from './userReducer';

const reducers = combineReducers({
  TEST: testReducer,
  AUTH_MESSAGE: authMessageReducer,
  AUTH: authReducer,
  USER: userReducer,
  CAFF: caffReducer,
});

export default reducers;
export type State = ReturnType<typeof reducers>;
