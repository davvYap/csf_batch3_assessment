import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { order } from '../model';
import { PizzaService } from '../pizza.service';
import { Router } from '@angular/router';

const SIZES: string[] = [
  'Personal - 6 inches',
  'Regular - 9 inches',
  'Large - 12 inches',
  'Extra Large - 15 inches',
];

const PIZZA_TOPPINGS: string[] = [
  'chicken',
  'seafood',
  'beef',
  'vegetables',
  'cheese',
  'arugula',
  'pineapple',
];

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css'],
})
export class MainComponent implements OnInit {
  form!: FormGroup;
  pizzaSize = SIZES[0];

  constructor(
    private fb: FormBuilder,
    private pizzaSvc: PizzaService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.form = this.createForm();
  }

  updateSize(size: string) {
    this.pizzaSize = SIZES[parseInt(size)];
  }

  createForm(): FormGroup {
    return this.fb.group({
      name: this.fb.control('YAP TAT WAI', [Validators.required]),
      email: this.fb.control('davvyap@gmail.com', [Validators.required]),
      size: this.fb.control(1, [Validators.required]),
      base: this.fb.control('', [Validators.required]),
      sauce: this.fb.control('', [Validators.required]),
      toppings1: this.fb.control(''),
      toppings2: this.fb.control(''),
      toppings3: this.fb.control(''),
      toppings4: this.fb.control(''),
      toppings5: this.fb.control(''),
      toppings6: this.fb.control(''),
      toppings7: this.fb.control(''),
      comments: this.fb.control(''),
    });
  }

  invalidForm(): boolean {
    // console.log(this.getToppings().length);
    return this.form.invalid || this.getToppings().length === 0;
  }

  submitOrder() {
    const pizzaOrder: order = this.form.value as order;
    const toppings: string[] = this.getToppings();
    pizzaOrder.toppings = toppings;
    console.log('order submitted >>> ', pizzaOrder);
    this.pizzaSvc
      .placeOrder(pizzaOrder)
      .then((res) => {
        console.log(res);
        this.router.navigate(['/orders', res.email]);
      })
      .catch((err) => {
        alert(err);
      });
  }

  getToppings(): string[] {
    const top1 = this.form.get('toppings1')?.value;
    const top2 = this.form.get('toppings2')?.value;
    const top3 = this.form.get('toppings3')?.value;
    const top4 = this.form.get('toppings4')?.value;
    const top5 = this.form.get('toppings5')?.value;
    const top6 = this.form.get('toppings6')?.value;
    const top7 = this.form.get('toppings7')?.value;

    const result: boolean[] = [top1, top2, top3, top4, top5, top6, top7];

    const toppings: string[] = [];
    for (let i = 0; i < result.length; i++) {
      if (result[i] === true) {
        toppings.push(PIZZA_TOPPINGS[i]);
      }
    }
    return toppings;
  }
}
