import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import { LoginService, Login } from '../api/users/index'
import { AuthenticationService } from '../authentication.service'

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  providers: [LoginService, AuthenticationService]
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;

  submitted = false;
  loading = false;
  constructor(private loginService: LoginService,
              private router: Router,
              private authenticationService: AuthenticationService,
              private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.loginForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  onSubmit() {
    this.submitted = true;
    this.loading = true;
    let login:Login = {
      email: this.f.username.value,
      password: this.f.password.value
    };
    this.loginService.login(login).subscribe(
      success => {
        this.authenticationService.login(success);
        this.router.navigate(['/']);
      },
      error => {}
    );
  }

  get f() { return this.loginForm.controls; }


}
