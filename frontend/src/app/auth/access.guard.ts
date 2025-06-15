import {UserService} from '../service/user/user.service';
import {inject} from '@angular/core';


export const canActivateAuth = () => {

  const userService = inject(UserService);
  const isLoggedIn = userService.isAuth;

  if (!isLoggedIn) return false;

  return true;
}
