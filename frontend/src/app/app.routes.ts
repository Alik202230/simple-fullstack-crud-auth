import {Routes} from '@angular/router';
import {LoginComponent} from './pages/login/login.component';
import {LayoutComponent} from './pages/layout/layout.component';
import {DashboardComponent} from './pages/dashboard/dashboard.component';
import {canActivateAuth} from './auth/access.guard';
import {RegisterComponent} from './pages/register/register.component';
import {HomeComponent} from './pages/home/home.component';
import {UpdateProductComponent} from './pages/update-product/update-product.component';
import {AddProductComponent} from './pages/add-product/add-product.component';
import {ViewProductComponent} from './pages/view-product/view-product.component';

export const routes: Routes = [
  {path: '', redirectTo: 'login', pathMatch: 'full'},

  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'home', component: HomeComponent},
  {path: 'add-product', component: AddProductComponent},
  {path: 'update-product/:id', component: UpdateProductComponent},
  {path: 'product/:id', component: ViewProductComponent},

  {
    path: 'dashboard',
    component: LayoutComponent,
    canActivate: [canActivateAuth],
    children: [
      {path: '', component: DashboardComponent}
    ]
  }
];
