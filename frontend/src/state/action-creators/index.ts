import { ActionType } from '../action-types';
import { AuthAction, CaffAction, MessageAction, TestAction, UserAction } from '../actions';
import { Dispatch } from 'redux';
import { VariantType } from 'notistack';
import AuthService from '../../services/AuthService';
import CaffService from '../../services/CaffService';
import ICaff from '../../interfaces/caff';
import IComment from '../../interfaces/comment';
import IPropertyUpdate from '../../interfaces/propertyUpdate';
import IToken from '../../interfaces/token';
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

export const setInfoMessage = (message: string, type: VariantType) => {
  return (dispatch: Dispatch<MessageAction>): void => {
    dispatch({
      type: ActionType.SET_INFO_MESSAGE,
      payload: { message, type },
    });
  };
};

export const clearInfoMessage = () => {
  return (dispatch: Dispatch<MessageAction>): void => {
    dispatch({
      type: ActionType.CLEAR_INFO_MESSAGE,
    });
  };
};

export const login = (username: string, password: string) => {
  return (dispatch: Dispatch<AuthAction>): void => {
    AuthService.login(username, password);
    dispatch({
      type: ActionType.LOGIN_SUCCESS,
      payload: { username, password },
    });
  };
};

export const loginOauth = (token: IToken) => {
  return (dispatch: Dispatch<AuthAction>): void => {
    dispatch({
      type: ActionType.LOGIN_SUCCESS_OAUTH,
      payload: token,
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

export const editUserData = (editedProperty: IPropertyUpdate) => {
  return (dispatch: Dispatch<UserAction>): void => {
    dispatch({
      type: ActionType.EDIT_USER_DATA,
      payload: editedProperty,
    });
  };
};

export const saveUserDataEdit = (edited: IUserData) => {
  return (dispatch: Dispatch<UserAction | MessageAction>): Promise<void> => {
    return UserService.updateUserData(authType, edited).then(() => {
      dispatch({
        type: ActionType.SAVE_USER_DATA_EDIT,
      });

      dispatch({
        type: ActionType.SET_INFO_MESSAGE,
        payload: { message: 'Updated profile', type: 'success' as VariantType },
      });

      return Promise.resolve();
    });
  };
};

export const getAllCaffFiles = () => {
  return (dispatch: Dispatch<CaffAction>): Promise<any> => {
    return CaffService.getAllCaffFiles(authType)
      .then((response) => {
        dispatch({
          type: ActionType.GET_ALL_CAFF_FILES,
        });

        return Promise.resolve(response.data);
      })
      .then((files) => {
        files.forEach((caff: ICaff) => {
          CaffService.getCaffFile(authType, caff.id).then((response) => {
            dispatch({
              type: ActionType.GET_CAFF_FILE,
              payload: response.data,
            });
          });
        });
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

// Helper function for download
const download = (fileUrl: string, fileName: string) => {
  const a = document.createElement('a');
  a.href = fileUrl;
  a.setAttribute('download', fileName);
  a.click();
};

export const downloadCaffFile = (id: number, filename: string) => {
  return (dispatch: Dispatch<CaffAction>): Promise<void> => {
    return CaffService.downloadCaffFile(authType, id).then((response) => {
      dispatch({
        type: ActionType.DOWNLOAD_CAFF_FILE,
        payload: response.data,
      });

      const file = new Blob([response.data.file]);
      const fileUrl = window.URL.createObjectURL(file);
      download(fileUrl, `${filename}.caff`);
    });
  };
};

export const addComment = (comment: IComment, id: number) => {
  return (dispatch: Dispatch<CaffAction | MessageAction>): Promise<void> => {
    return CaffService.addComment(authType, comment, id).then(() => {
      dispatch({
        type: ActionType.ADD_COMMENT,
        payload: { comment, id },
      });

      dispatch({
        type: ActionType.SET_INFO_MESSAGE,
        payload: { message: 'Added comment', type: 'success' as VariantType },
      });

      return Promise.resolve();
    });
  };
};

export const uploadCaffFile = (fileName: string, file: File) => {
  return (dispatch: Dispatch<CaffAction | MessageAction>): Promise<void> => {
    return CaffService.uploadCaffFile(authType, fileName, file).then((response) => {
      dispatch({
        type: ActionType.UPLOAD_CAFF_FILE,
        payload: response.data,
      });

      dispatch({
        type: ActionType.SET_INFO_MESSAGE,
        payload: { message: 'Uploaded CAFF file', type: 'success' as VariantType },
      });

      return Promise.resolve();
    });
  };
};

export const deleteCaffFile = (id: number) => {
  return (dispatch: Dispatch<CaffAction | MessageAction>): Promise<void> => {
    return CaffService.deleteCaffFile(authType, id)
      .then(() => {
        dispatch({
          type: ActionType.DELETE_CAFF_FILE,
          payload: id,
        });

        dispatch({
          type: ActionType.SET_INFO_MESSAGE,
          payload: { message: 'Deleted CAFF file', type: 'success' as VariantType },
        });

        return Promise.resolve();
      })
      .catch(() => {
        dispatch({
          type: ActionType.SET_INFO_MESSAGE,
          payload: { message: "Can't delete file!", type: 'error' as VariantType },
        });
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
