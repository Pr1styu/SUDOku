import { default as dayjs } from 'dayjs';
import axios from 'axios';
import config from '../config/config';

const dateFormat = 'YYYY.MM.DD. HH:mm:ss';

const getOauthToken = (code: string): Promise<any> => {
  return axios
    .post(
      config.urls.oauth2.token(code),
      {},
      {
        auth: {
          username: config.urls.oauth2.params.clientId,
          password: config.urls.oauth2.params.clientPassword,
        },
      }
    )
    .then((response) => {
      localStorage.setItem('user_token', JSON.stringify(response.data));
      localStorage.setItem('token_valid_since', JSON.stringify(dayjs().format(dateFormat)));
      localStorage.setItem(
        'token_expires_at',
        JSON.stringify(
          dayjs()
            .add(response.data.expires_in ?? 300, 'second')
            .format(dateFormat)
        )
      );

      return Promise.resolve(response.data);
    });
};

const getRefreshToken = (refresh_token: string): Promise<any> => {
  return axios
    .post(
      config.urls.oauth2.refresh_token(refresh_token),
      {},
      {
        auth: {
          username: config.urls.oauth2.params.clientId,
          password: config.urls.oauth2.params.clientPassword,
        },
      }
    )
    .then((response) => {
      localStorage.setItem('user_token', JSON.stringify(response.data));
      localStorage.setItem('token_valid_since', JSON.stringify(dayjs().format(dateFormat)));
      localStorage.setItem(
        'token_expires_at',
        JSON.stringify(
          dayjs()
            .add(response.data.expires_in ?? 300, 'second')
            .format(dateFormat)
        )
      );
      return Promise.resolve(response.data);
    });
};

export default {
  getOauthToken,
  getRefreshToken,
};
