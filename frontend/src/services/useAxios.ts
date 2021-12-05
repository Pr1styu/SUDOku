import { default as dayjs } from 'dayjs';
import TokenService from '../services/TokenService';
import axios, { AxiosInstance } from 'axios';
import config from '../config/config';
import customParseFormat from 'dayjs/plugin/customParseFormat';

const interceptorDebug = config.interceptorDebug;
const dateFormat = 'YYYY.MM.DD. HH:mm:ss';

const axiosInstance = axios.create();

axiosInstance.interceptors.request.use((request) => {
  dayjs.extend(customParseFormat);
  const expiresAtString = localStorage.getItem('token_expires_at') || '{}';
  const expiresAt = dayjs(expiresAtString, dateFormat).unix();
  const isExpired = dayjs.unix(expiresAt).diff(dayjs()) < 5;
  const tokenString = localStorage.getItem('user_token') || '{}';
  const token = JSON.parse(tokenString);
  if (interceptorDebug) {
    console.info(
      '⏰ Access token expired: ' +
        isExpired +
        ' | Time left: ' +
        dayjs.unix(expiresAt).diff(dayjs()) / 1000.0 +
        ' seconds'
    );
  }
  if (isExpired && token !== {} && token.refresh_token) {
    TokenService.getRefreshToken(token.refresh_token);
  }

  return request;
});

const useAxios = (): AxiosInstance => {
  return axiosInstance;
};

export default useAxios;
