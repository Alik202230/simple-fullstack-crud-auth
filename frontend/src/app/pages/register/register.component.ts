import {Component, inject} from '@angular/core';
import {UserService} from '../../service/user/user.service';
import {FormControl, FormGroup, ReactiveFormsModule} from '@angular/forms';
import {Router} from '@angular/router';

@Component({
  selector: 'app-register',
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {

  userService = inject(UserService);
  router = inject(Router);

  form = new FormGroup({
    firstName: new FormControl<string | null>(''),
    lastName: new FormControl<string | null>(''),
    email: new FormControl<string | null>(''),
    password: new FormControl<string | null>(''),
  })

  onSubmit() {
    this.userService.register(this.form.value as any).subscribe(res => {
        this.router.navigate(['/login'])
        console.log(res);
      }
    )
  }

}
