import {Category} from './category.model';

export class SaveProductRequestModel {
  name: string;
  description: string;
  price: number;
  quantity: number;
  category: Category | null;
  images: []

  constructor() {
    this.name = '';
    this.description = '';
    this.price = 0;
    this.quantity = 0;
    this.category = null;
    this.images = [];
  }

}
