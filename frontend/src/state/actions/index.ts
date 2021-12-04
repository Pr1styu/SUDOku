import { ActionType } from '../action-types';
import ICaff from '../../interfaces/caff';
import IComment from '../../interfaces/comment';
import IPropertyUpdate from '../../interfaces/propertyUpdate';
import IToken from '../../interfaces/token';
import IUser from '../../interfaces/user';
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
  type: ActionType.SET_INFO_MESSAGE;
  payload: string;
}

interface IClearMessageAction {
  type: ActionType.CLEAR_INFO_MESSAGE;
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
  payload: IUser;
}

interface ILoginSuccessOAuthAction {
  type: ActionType.LOGIN_SUCCESS_OAUTH;
  payload: IToken;
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
  | ILoginSuccessOAuthAction
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
  payload: IPropertyUpdate;
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

interface IUploadCaffFile {
  type: ActionType.UPLOAD_CAFF_FILE;
  payload: ICaff;
}

interface IDeleteCaffFile {
  type: ActionType.DELETE_CAFF_FILE;
  payload: number;
}

interface IResetUploadDone {
  type: ActionType.RESET_UPLOAD_DONE;
}

export type CaffAction =
  | IGetAllCaffFilesAction
  | IGetCaffFileAction
  | IDownloadCaffFileAction
  | IAddCommentAction
  | IUploadCaffFile
  | IDeleteCaffFile
  | IResetUploadDone;
