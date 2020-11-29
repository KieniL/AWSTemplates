import { Injectable } from '@angular/core';

import { Token } from './api/users/index'

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  constructor() { }

  login(token:Token) {
    localStorage.setItem('token', token.token);
    localStorage.setItem('user', token.userid);
  }

  logout() {
    localStorage.removeItem('token');
  }

  isLoggedIn():boolean {
    return localStorage.getItem('token') != null;
  }

  getUserId(): string {
    return localStorage.getItem('user');
  }

}
