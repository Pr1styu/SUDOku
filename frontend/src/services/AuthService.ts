import axios, { AxiosResponse } from 'axios';
import config from '../config/config';

const register = (
  username: string,
  password: string,
  passwordAgain: string,
  nickname: string,
  email: string
): Promise<AxiosResponse<any>> => {
  return axios.post(config.urls.auth.signUp, {
    username,
    password,
    passwordAgain,
    nickname,
    email,
  });
};

const login = (username: string, password: string): Promise<AxiosResponse<any>> => {
  return axios
    .post(config.urls.auth.signIn, {
      username,
      password,
    })
    .then((response) => {
      if (response.data.token) {
        localStorage.setItem('user_basic', JSON.stringify(response.data));
      }
      return response.data;
    });
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
