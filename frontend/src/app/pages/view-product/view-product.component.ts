import {Component, inject, OnInit, signal} from '@angular/core';
import {ProductService} from '../../service/product/product.service';
import {SingleProductResponse} from '../../interface/single-product-response';
import {ActivatedRoute, RouterLink} from '@angular/router';
import {CurrencyPipe} from '@angular/common';

@Component({
  selector: 'app-view-product',
  imports: [
    RouterLink,
    CurrencyPipe
  ],
  templateUrl: './view-product.component.html',
  styleUrl: './view-product.component.scss'
})
export class ViewProductComponent implements OnInit{

  productService = inject(ProductService);

  route = inject(ActivatedRoute);

  product = signal<SingleProductResponse | null>(null);

  ngOnInit() {
    const idParam = this.route.snapshot.paramMap.get('id');
    const id = idParam ? Number(idParam) : 0;
    this.productService.getProductById(id).subscribe(res => {
      this.product.set(res);
      console.log(res);
    })
  }

}
