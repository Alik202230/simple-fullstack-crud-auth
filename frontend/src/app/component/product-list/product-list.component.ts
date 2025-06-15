import {Component, inject, OnInit, signal} from '@angular/core';
import {ProductService} from '../../service/product/product.service';
import {AllProductResponse} from '../../interface/all-product-response';
import {CurrencyPipe} from '@angular/common';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-product-list',
  imports: [
    CurrencyPipe,
    RouterLink
  ],
  templateUrl: './product-list.component.html',
  styleUrl: './product-list.component.scss'
})
export class ProductListComponent implements OnInit{

  productService = inject(ProductService);

  products = signal<AllProductResponse[]>([]);

  ngOnInit(): void {
    this.productService.getAllProducts().subscribe(res => {
      this.products.set(res);
      console.log(res);
    })
  }
}
