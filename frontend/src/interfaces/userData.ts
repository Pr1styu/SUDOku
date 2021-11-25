export default interface IUserData {
  username: string | null;
  displayname: string | null;
  role: string;
  email: string;
  registration_date: [year: number, month: number, day: number];
  last_login_date: [year: number, month: number, day: number];
  icon: any | null;
}
