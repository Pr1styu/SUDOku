import { AxiosResponse } from 'axios';
import IUserData from '../interfaces/userData';
import config from '../config/config';
import useAxios from './useAxios';

const httpClient = useAxios();

const register = (user: IUserData): Promise<AxiosResponse<any>> => {
  return httpClient.post(config.urls.signUp, user);
};

const login = (username: string, password: string): void => {
  localStorage.setItem('user_basic', JSON.stringify({ username, password }));
};

const logout = (): void => {
  localStorage.removeItem('user_basic');
  localStorage.removeItem('user_token');
  localStorage.removeItem('token_valid_since');
  localStorage.removeItem('token_expires_at');
};

export default {
  register,
  login,
  logout,
};
