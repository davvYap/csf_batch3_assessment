import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, lastValueFrom } from 'rxjs';
import { emailOrders, order, orderResponse } from './model';

@Injectable({
  providedIn: 'root',
})
export class PizzaService {
  constructor(private http: HttpClient) {}

  // TODO: Task 3
  // You may add any parameters and return any type from placeOrder() method
  // Do not change the method name
  placeOrder(o: order): Promise<orderResponse> {
    return lastValueFrom(this.http.post<orderResponse>('/api/order', o));
  }

  // TODO: Task 5
  // You may add any parameters and return any type from getOrders() method
  // Do not change the method name
  getOrders(email: string): Observable<emailOrders[]> {
    return this.http.get<emailOrders[]>(`/api/orders/${email}`);
  }

  // TODO: Task 7
  // You may add any parameters and return any type from delivered() method
  // Do not change the method name
  delivered(id: string): Promise<any> {
    return lastValueFrom(this.http.delete<any>(`/api/order/${id}`));
  }

  // http://localhost:8080/api/order
}
