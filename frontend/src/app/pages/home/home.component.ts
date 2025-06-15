import {Component, inject, OnInit, signal} from '@angular/core';
import {UserService} from '../../service/user/user.service';
import {SearchComponent} from '../../component/search/search.component';
import {RouterLink} from '@angular/router';
import {ProductListComponent} from '../../component/product-list/product-list.component';

@Component({
  selector: 'app-home',
  imports: [
    SearchComponent,
    RouterLink,
    ProductListComponent
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit{

  userService = inject(UserService);
  firstName = signal<string | null>('');
  isLoggedIn = signal<boolean>(false);
  role = signal<string | null>('');

  ngOnInit(): void {

    const name = this.userService.firstName;
    const token = this.userService.token;
    this.role.set(this.userService.role);
    this.isLoggedIn.set(!!token);
    this.firstName.set(name ?? '');

  }

  onLogout() {
    this.userService.logout();
    this.isLoggedIn.set(false);
    this.firstName.set('');
    this.role.set('');
  }


}
