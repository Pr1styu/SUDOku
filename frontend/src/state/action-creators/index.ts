import { ActionType } from '../action-types';
import { AuthAction, CaffAction, MessageAction, TestAction, UserAction } from '../actions';
import { Dispatch } from 'redux';
import AuthService from '../../services/AuthService';
import CaffService from '../../services/CaffService';
import ICaff from '../../interfaces/caff';
import IComment from '../../interfaces/comment';
import IUserData from '../../interfaces/userData';
import UserService from '../../services/UserService';
import config from '../../config/config';

const authType = config.authType;

export const depositMoney = (amount: number) => {
  return (dispatch: Dispatch<TestAction>): void => {
    dispatch({
      type: ActionType.DEPOSIT,
      payload: amount,
    });
  };
};

export const withdrawMoney = (amount: number) => {
  return (dispatch: Dispatch<TestAction>): void => {
    dispatch({
      type: ActionType.WITHDRAW,
      payload: amount,
    });
  };
};

export const bankrupt = () => {
  return (dispatch: Dispatch<TestAction>): void => {
    dispatch({
      type: ActionType.BANKRUPT,
    });
  };
};

export const setAuthMessage = (message: string) => {
  return (dispatch: Dispatch<MessageAction>): void => {
    dispatch({
      type: ActionType.SET_AUTH_MESSAGE,
      payload: message,
    });
  };
};

export const clearMessage = () => {
  return (dispatch: Dispatch<MessageAction>): void => {
    dispatch({
      type: ActionType.CLEAR_AUTH_MESSAGE,
    });
  };
};

export const register = (
  username: string,
  password: string,
  passwordAgain: string,
  nickname: string,
  email: string
) => {
  return (dispatch: Dispatch<AuthAction | MessageAction>): Promise<void> => {
    return AuthService.register(username, password, passwordAgain, nickname, email).then(
      (response) => {
        dispatch({
          type: ActionType.REGISTER_SUCCESS,
        });

        dispatch({
          type: ActionType.SET_AUTH_MESSAGE,
          payload: response.data.message,
        });

        return Promise.resolve();
      },

      (error) => {
        const message = error.response?.data?.message ?? error.message ?? error.toString();

        dispatch({
          type: ActionType.REGISTER_FAIL,
        });

        dispatch({
          type: ActionType.SET_AUTH_MESSAGE,
          payload: message,
        });

        return Promise.reject();
      }
    );
  };
};

export const login = (username: string, password: string) => {
  return (dispatch: Dispatch<AuthAction | MessageAction>): Promise<void> => {
    return AuthService.login(username, password).then(
      (data) => {
        dispatch({
          type: ActionType.LOGIN_SUCCESS,
          payload: { user: data },
        });

        return Promise.resolve();
      },

      (error) => {
        const message = error.response?.data?.message ?? error.message ?? error.toString();

        dispatch({
          type: ActionType.LOGIN_FAIL,
        });

        dispatch({
          type: ActionType.SET_AUTH_MESSAGE,
          payload: message,
        });

        return Promise.reject();
      }
    );
  };
};

export const loginOauth = () => {
  return (dispatch: Dispatch<AuthAction>): void => {
    dispatch({
      type: ActionType.LOGIN_SUCCESS_OAUTH,
    });
  };
};

export const logout = () => {
  return (dispatch: Dispatch<AuthAction>): void => {
    AuthService.logout();
    dispatch({
      type: ActionType.LOGOUT,
    });
  };
};

export const getUserData = () => {
  return (dispatch: Dispatch<UserAction>): Promise<void> => {
    return UserService.getUserData(authType).then((response) => {
      dispatch({
        type: ActionType.GET_USER_DATA,
        payload: response.data,
      });

      return Promise.resolve();
    });
  };
};

export const clearUserData = () => {
  return (dispatch: Dispatch<UserAction>): void => {
    dispatch({
      type: ActionType.CLEAR_USER_DATA,
    });
  };
};

export const editUserData = (edited: IUserData) => {
  return (dispatch: Dispatch<UserAction>): void => {
    dispatch({
      type: ActionType.EDIT_USER_DATA,
      payload: edited,
    });
  };
};

export const saveUserDataEdit = (edited: IUserData) => {
  return (dispatch: Dispatch<UserAction>): Promise<void> => {
    return UserService.updateUserData(authType, edited).then(() => {
      dispatch({
        type: ActionType.SAVE_USER_DATA_EDIT,
      });

      return Promise.resolve();
    });
  };
};

export const getAllCaffFiles = () => {
  return (dispatch: Dispatch<CaffAction>): Promise<void> => {
    return CaffService.getAllCaffFiles(authType).then((response) => {
      dispatch({
        type: ActionType.GET_ALL_CAFF_FILES,
      });

      response.data.forEach((caff: ICaff) => {
        CaffService.getCaffFile(authType, caff.id).then((response) => {
          dispatch({
            type: ActionType.GET_CAFF_FILE,
            payload: response.data,
          });
        });
      });

      return Promise.resolve();
    });
  };
};

export const getCaffFile = (id: number) => {
  return (dispatch: Dispatch<CaffAction>): Promise<void> => {
    return CaffService.getCaffFile(authType, id).then((response) => {
      dispatch({
        type: ActionType.GET_CAFF_FILE,
        payload: response.data,
      });

      return Promise.resolve();
    });
  };
};

export const downloadCaffFile = (id: number) => {
  return (dispatch: Dispatch<CaffAction>): Promise<void> => {
    return CaffService.downloadCaffFile(authType, id).then((response) => {
      dispatch({
        type: ActionType.DOWNLOAD_CAFF_FILE,
        payload: response.data,
      });

      return Promise.resolve();
    });
  };
};

export const addComment = (comment: IComment, id: number) => {
  return (dispatch: Dispatch<CaffAction>): Promise<void> => {
    return CaffService.addComment(authType, comment, id).then(() => {
      dispatch({
        type: ActionType.ADD_COMMENT,
        payload: { comment, id },
      });

      return Promise.resolve();
    });
  };
};

export const uploadCaffFile = (fileName: string, file: File) => {
  return (dispatch: Dispatch<CaffAction>): Promise<void> => {
    return CaffService.uploadCaffFile(authType, fileName, file).then((response) => {
      dispatch({
        type: ActionType.UPLOAD_CAFF_FILE,
        payload: response.data,
      });

      return Promise.resolve();
    });
  };
};

export const deleteCaffFile = (id: number) => {
  return (dispatch: Dispatch<CaffAction>): Promise<void> => {
    return CaffService.deleteCaffFile(authType, id).then(() => {
      dispatch({
        type: ActionType.DELETE_CAFF_FILE,
        payload: id,
      });

      return Promise.resolve();
    });
  };
};

export const resetUploadDone = () => {
  return (dispatch: Dispatch<CaffAction>): Promise<void> => {
    dispatch({
      type: ActionType.RESET_UPLOAD_DONE,
    });

    return Promise.resolve();
  };
};
