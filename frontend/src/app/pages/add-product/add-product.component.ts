import {Component, inject, signal} from '@angular/core';
import {ProductService} from '../../service/product/product.service';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {Category} from '../../model/category.model';
import {UserService} from '../../service/user/user.service';
import {ActivatedRoute, Router} from '@angular/router';
import {catchError, of, tap} from 'rxjs';

@Component({
  selector: 'app-add-product',
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './add-product.component.html',
  styleUrl: './add-product.component.scss'
})
export class AddProductComponent {

  productService = inject(ProductService);

  userService = inject(UserService);

  categoryOptions = Object.values(Category);

  router = inject(Router);

  form = new FormGroup({
    name: new FormControl<string | null>('', Validators.required),
    description: new FormControl<string | null>('', Validators.required),
    price: new FormControl<string | null>('', Validators.required),
    quantity: new FormControl<string | null>('', Validators.required),
    category: new FormControl<Category | null>(null, Validators.required),
    image: new FormControl<File[] | null>([], Validators.required)
  })


  token = signal<string | null>('');
  role = signal<string | null>('');

  constructor() {
    this.token.set(this.userService.token);
    this.role.set(this.userService.role);
  }

  onFileChange(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const files = Array.from(input.files);
      this.form.get('image')?.setValue(files);
    }
  }

  onSubmit() {
    if (this.form.value?.image) {
      const formData = this.form.value as any;
      const files = formData.image as File[];
      this.productService.addProduct(formData, files).pipe(
        tap(res => {
          console.log(res);
          this.router.navigate(['/dashboard']);
        }),
        catchError(error => {
          console.error('Error adding product:', error);
          alert('Failed to add product. Please try again.');
          return of(null);
        })
      ).subscribe();

    }
  }
}
