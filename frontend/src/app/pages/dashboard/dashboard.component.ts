import {Component, inject, OnInit, signal} from '@angular/core';
import {UserService} from '../../service/user/user.service';
import {TitleCasePipe} from '@angular/common';
import {RouterLink} from '@angular/router';
import {ProductService} from '../../service/product/product.service';
import {catchError, of, tap} from 'rxjs';

@Component({
  selector: 'app-dashboard',
  imports: [
    TitleCasePipe,
    RouterLink
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {

  userService = inject(UserService);
  productService = inject(ProductService);

  firstName = signal<string | null>('');
  token = signal<string | null>('');
  role = signal<string | null>('');

  products = signal<any>([])

  deleteProduct(id: number) {
    this.productService.deleteProduct(id).pipe(
      tap(() => {
        const updatedProducts = this.products().filter((product: any) => product.id !== id);
        this.products.set(updatedProducts);
      }),
      catchError((error) => {
        console.error('Error deleting product:', error);
        alert('Failed to delete product. Please try again.');
        return of(null);
      })
    ).subscribe();

  }

  ngOnInit(): void {
    this.firstName.set(this.userService.firstName);
    this.token.set(this.userService.token);
    this.role.set(this.userService.role);
    this.productService.getAllProducts().pipe(
      tap(res => {
        this.products.set(res);
      }),
      catchError((error) => {
        console.error('Error loading products:', error);
        return of([]);
      })
    ).subscribe();

  }

}
