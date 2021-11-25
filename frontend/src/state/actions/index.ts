import { ActionType } from '../action-types';
import ICaff from '../../interfaces/caff';
import IComment from '../../interfaces/comment';
import IEditUserData from '../../interfaces/userDataEdit';
import IUserData from '../../interfaces/userData';

interface IDepositAction {
  type: ActionType.DEPOSIT;
  payload: number;
}

interface IWithdrawAction {
  type: ActionType.WITHDRAW;
  payload: number;
}

interface IBankruptAction {
  type: ActionType.BANKRUPT;
}

export type TestAction = IDepositAction | IWithdrawAction | IBankruptAction;

interface IMessageAction {
  type: ActionType.SET_AUTH_MESSAGE;
  payload: string;
}

interface IClearMessageAction {
  type: ActionType.CLEAR_AUTH_MESSAGE;
}

export type MessageAction = IMessageAction | IClearMessageAction;

interface IRegisterSuccessAction {
  type: ActionType.REGISTER_SUCCESS;
}

interface IRegisterFailAction {
  type: ActionType.REGISTER_FAIL;
}

interface ILoginSuccessAction {
  type: ActionType.LOGIN_SUCCESS;
  payload: { user: any };
}

interface ILoginFailAction {
  type: ActionType.LOGIN_FAIL;
}

interface ILogoutAction {
  type: ActionType.LOGOUT;
}

export type AuthAction =
  | IRegisterSuccessAction
  | IRegisterFailAction
  | ILoginSuccessAction
  | ILoginFailAction
  | ILogoutAction;

interface IUserDataAction {
  type: ActionType.GET_USER_DATA;
  payload: IUserData;
}

interface IClearUserDataAction {
  type: ActionType.CLEAR_USER_DATA;
}

interface IEditUserDataAction {
  type: ActionType.EDIT_USER_DATA;
  payload: IEditUserData;
}

interface ISaveUserDataEdit {
  type: ActionType.SAVE_USER_DATA_EDIT;
}

export type UserAction =
  | IUserDataAction
  | IClearUserDataAction
  | IEditUserDataAction
  | ISaveUserDataEdit;

interface IGetAllCaffFilesAction {
  type: ActionType.GET_ALL_CAFF_FILES;
}

interface IGetCaffFileAction {
  type: ActionType.GET_CAFF_FILE;
  payload: ICaff;
}

interface IDownloadCaffFileAction {
  type: ActionType.DOWNLOAD_CAFF_FILE;
  payload: any;
}

interface IAddCommentAction {
  type: ActionType.ADD_COMMENT;
  payload: { id: number; comment: IComment };
}

export type CaffAction =
  | IGetAllCaffFilesAction
  | IGetCaffFileAction
  | IDownloadCaffFileAction
  | IAddCommentAction;
