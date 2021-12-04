export default interface IUserData {
  username: string;
  fullName: string | null;
  email: string;
  userType?: string;
  profilePicture?: any | null;
  password?: string;
}
