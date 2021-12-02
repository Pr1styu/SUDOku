export default function BasicAuthHeader(): any {
  const userString = localStorage.getItem('user');
  const user = userString ? JSON.parse(userString) : null;

  if (user && user.username && user.password) {
    return {
      auth: {
        username: user.username,
        password: user.password,
      },
    };
  } else {
    return {};
  }
}
