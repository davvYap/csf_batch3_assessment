import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { emailOrders } from '../model';
import { PizzaService } from '../pizza.service';

@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.css'],
})
export class OrdersComponent implements OnInit {
  email!: string;

  emailOrders$!: Observable<emailOrders[]>;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private pizzaSvc: PizzaService
  ) {}
  ngOnInit(): void {
    this.email = this.route.snapshot.params['email'];

    this.emailOrders$ = this.pizzaSvc.getOrders(this.email);
  }

  back() {
    this.router.navigate(['/']);
  }

  delivered(id: string) {
    this.pizzaSvc
      .delivered(id)
      .then((res) => {
        this.emailOrders$ = this.pizzaSvc.getOrders(this.email);
      })
      .catch((err) => {
        alert(err);
      });
  }
}
