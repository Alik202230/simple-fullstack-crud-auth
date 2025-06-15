import {HttpInterceptorFn} from '@angular/common/http';
import {inject} from '@angular/core';
import {UserService} from '../service/user/user.service';

export const authTokenInterceptor: HttpInterceptorFn = (req, next) => {

  const userService = inject(UserService);
  const token = userService.token;

  if (!token) return next(req);

  req = req.clone({
    setHeaders: {
      Authorization: `Bearer ${token}`
    }
  });

  return next(req);
}
