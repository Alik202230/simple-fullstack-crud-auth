import {inject, Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {SaveProductRequestModel} from '../../model/save-product-request.model';
import {SaveProductResponse} from '../../interface/save-product-response';
import {Observable} from 'rxjs';
import {AllProductResponse} from '../../interface/all-product-response';
import {UpdateProductRequestModel} from '../../model/update-product-request.model';
import {UpdateProductResponse} from '../../interface/update-product-response';
import {SingleProductResponse} from '../../interface/single-product-response';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  http = inject(HttpClient);

  baseUrl = 'http://localhost:8080/product';

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')!).token : null;
    let headers = new HttpHeaders();
    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }
    return headers;
  }



  addProduct(payload: SaveProductRequestModel, files: File[]) : Observable<SaveProductResponse> {

    const formData = new FormData();
    formData.append('name', payload.name);
    formData.append('description', payload.description);
    formData.append('price', String(payload.price));
    formData.append('quantity', String(payload.quantity));
    formData.append("category", String(payload.category))

    files.forEach(file => formData.append('image', file))

    return this.http.post<SaveProductResponse>(`${this.baseUrl}/create`, formData, {headers: this.getAuthHeaders()});
  }

  getAllProducts() {
    return this.http.get<AllProductResponse[]>(`${this.baseUrl}/all`);
  }

  updateProduct(payload: UpdateProductRequestModel, id: number) {
    return this.http.put<UpdateProductResponse>(`${this.baseUrl}/update/${id}`, payload)
  }

  getProductById(id: number) {
    return this.http.get<SingleProductResponse>(`${this.baseUrl}/${id}`);
  }

  deleteProduct(id: number): Observable<string> {
    return this.http.delete<string>(`${this.baseUrl}/delete/${id}`, {
      headers: this.getAuthHeaders(),
      responseType: "text" as "json"
    });
  }


}
