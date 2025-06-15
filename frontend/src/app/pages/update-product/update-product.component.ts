import {Component, inject, OnInit, signal} from '@angular/core';
import {ProductService} from '../../service/product/product.service';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-update-product',
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './update-product.component.html',
  styleUrl: './update-product.component.scss'
})
export class UpdateProductComponent implements OnInit{

  productService = inject(ProductService);
  router = inject(Router);
  route = inject(ActivatedRoute);

  productId = signal<any>(0);

  form = new FormGroup({
    name: new FormControl<string | null>('', Validators.required),
    description: new FormControl<string | null>('', Validators.required),
    price: new FormControl<string | null>('', Validators.required),
    quantity: new FormControl<string | null>('', Validators.required),
  })


  ngOnInit() {
    const idParam = this.route.snapshot.paramMap.get('id');
    const id = idParam ? Number(idParam) : 0;
    this.productId.set(id);
    this.productService.getProductById(id).subscribe((product) => {
      this.form.patchValue({
        name: product.name,
        description: product.description,
        price: String(product.price),
        quantity: String(product.quantity)
      })
    })
  }

  onSubmit() {
    if (this.form.valid) {
      const updateData = {
        name: this.form.get('name')?.value || '',
        description: this.form.get('description')?.value || '',
        price: Number(this.form.get('price')?.value),
        quantity: Number(this.form.get('quantity')?.value)
      };

      this.productService.updateProduct(updateData, this.productId()).subscribe({
        next: () => {
          this.router.navigate(['/dashboard']);
        },
        error: (error) => {
          console.error('Error updating product:', error);
        }
      });
    }
  }
}
