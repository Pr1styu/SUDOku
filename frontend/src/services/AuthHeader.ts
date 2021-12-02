export default function AuthHeader(): any {
  const userStr = localStorage.getItem('user_token');
  let user = null;

  if (userStr) user = JSON.parse(userStr);

  if (user && user.id_token) {
    return { Authorization: 'Bearer ' + user.access_token };
  } else {
    return {};
  }
}
