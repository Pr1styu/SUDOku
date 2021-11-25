export default function AuthHeader(): any {
  const userStr = localStorage.getItem('user');
  let user = null;

  if (userStr) user = JSON.parse(userStr);

  if (user && user.token) {
    return { Authorization: 'Bearer ' + user.token };
  } else {
    return {};
  }
}
