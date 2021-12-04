import IUserData from '../interfaces/userData';
import axios, { AxiosResponse } from 'axios';
import config from '../config/config';

const register = (user: IUserData): Promise<AxiosResponse<any>> => {
  return axios.post(config.urls.signUp, user);
};

const login = (username: string, password: string): void => {
  localStorage.setItem('user_basic', JSON.stringify({ username, password }));
};

const logout = (): void => {
  localStorage.removeItem('user_basic');
  localStorage.removeItem('user_token');
};

export default {
  register,
  login,
  logout,
};
