export class UpdateProductRequestModel {
  name: string;
  description: string;
  price: number;
  quantity: number;

  constructor() {
    this.name = "";
    this.description = "";
    this.price = 0;
    this.quantity = 0;
  }

}
