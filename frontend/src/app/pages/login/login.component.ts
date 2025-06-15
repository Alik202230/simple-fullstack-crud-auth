import {Component, inject} from '@angular/core';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {UserService} from "../../service/user/user.service";
import {UserAuthRequestModel} from "../../model/user-auth-request.model";
import {Router, RouterLink} from "@angular/router";


@Component({
  selector: 'app-login',
  imports: [
    ReactiveFormsModule,
    RouterLink
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {

  userService = inject(UserService)
  router = inject(Router);

  form = new FormGroup({
    email: new FormControl<string | null>(null, Validators.required),
    password: new FormControl<string | null>(null, Validators.required)
  })



  onSubmit() {
    this.userService.login(this.form.value as UserAuthRequestModel)
        .subscribe(res => {
          this.userService.role = res.role;
          res.role === 'ADMIN' ? this.router.navigate(['/dashboard']) : this.router.navigate(['/home']);
          console.log(res);
        });
    localStorage.setItem('user', JSON.stringify(this.form.value));
  }

}
