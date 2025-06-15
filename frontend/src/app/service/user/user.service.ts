import {inject, Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {UserAuthRequestModel} from '../../model/user-auth-request.model';
import {tap} from "rxjs";
import {UserAuthResponse} from "../../interface/user-auth-response";
import {SaveUserRequestModel} from '../../model/save-user-request.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  http = inject(HttpClient);

  token: string | null = null;
  firstName: string | null = null;
  lastName: string | null = null;
  email: string | null = null;
  role: string | null = null;

  get isAuth() {
    return !!this.token;
  }

  constructor() {
    if (typeof window !== 'undefined' && localStorage.getItem('user')) {
      const parse = JSON.parse(localStorage.getItem('user')!);
      this.token = parse.token;
      this.firstName = parse.firstName;
      this.lastName = parse.lastName;
      this.email = parse.email;
      this.role = parse.role;
    }
  }

  login(payload: UserAuthRequestModel) {
    return this.http.post<UserAuthResponse>('http://localhost:8080/user/login', payload,  {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      })
    }).pipe(tap(val => {
      this.token = val.token;
      this.firstName = val.firstName;
      this.lastName = val.lastName;
      this.email = val.email;
      this.role = val.role;

      localStorage.setItem('user', JSON.stringify(val));
    }));
  }

  register(payload: SaveUserRequestModel) {
    return this.http.post<any>('http://localhost:8080/user/register', payload,  {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      })
    });
  }

  logout() {
    const token = localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')!).token : null;

    if (token) {
      this.http.post<any>(`http://localhost:8080/user/logout`, {}, {
        headers: new HttpHeaders({
          'Authorization': `Bearer ${token}`
        })
      })
    }
    localStorage.removeItem('user');
    this.token = null;
    this.firstName = null;
    this.lastName = null;
    this.email = null;

  }

}
