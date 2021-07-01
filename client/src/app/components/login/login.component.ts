import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { User } from 'src/app/models/user';
import { AppService } from 'src/app/services/app.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  user: User;
  usernameError : string;
  passwordError : string;
  
  loginUserDetails = new FormGroup({
    uname: new FormControl('', Validators.required),
    pwd: new FormControl('', Validators.required)
  });
  
  constructor(private service: AppService, private router: Router) { }

  login(){
    if(this.loginUserDetails.controls.uname.errors || this.loginUserDetails.controls.pwd.errors){
      this.usernameError = this.loginUserDetails.controls.uname.errors ? 'Username is required!' : null;
      this.passwordError = this.loginUserDetails.controls.pwd.errors ? 'Password is required!' : null;
    }
    else{
      this.service.login(this.loginUserDetails.value.uname, this.loginUserDetails.value.pwd).subscribe(
        (res: User) => {
          this.user = res;
          this.router.navigateByUrl('home/calculator');
        },
        err => this.user.error = err
      )
    } 
  }
  reset(){

  }

}
