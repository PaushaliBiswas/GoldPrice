import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { GoldPrice } from 'src/app/models/gold-price';
import { User } from 'src/app/models/user';
import { AppService } from 'src/app/services/app.service';
//import * as fileSaver from 'file-saver';

@Component({
  selector: 'app-calculator',
  templateUrl: './calculator.component.html',
  styleUrls: ['./calculator.component.css']
})
export class CalculatorComponent implements OnInit, OnDestroy {

  goldPrice : GoldPrice;
  usertype : string;
  rateError : string;
  weightError : string;
  printError : string;
  
  calculatorForm = new FormGroup({
    rate: new FormControl('', Validators.required),
    weight: new FormControl('', Validators.required),
    discount: new FormControl(''),
    price: new FormControl('')
  });
  
  constructor(private service: AppService, private router: Router) { }

  ngOnInit(): void {
    this.usertype = this.service.getUserType();
    if(this.usertype === 'privileged')
      this.service.getDiscount().subscribe((res: GoldPrice) => this.calculatorForm.get("discount").setValue(res.discount));
  }

  ngOnDestroy(): void {
    this.logout();
  }

  validateForm(action: string): boolean{
    
    this.rateError = this.calculatorForm.controls.rate.errors ? 'Gold Price is required' : null;
    this.weightError = this.calculatorForm.controls.weight.errors ? 'Weight is required' : null;
    this.printError = (action === 'download' && this.calculatorForm.value.price === '') ? 'Please calculate the price first' : null;
    return (this.rateError === null && this.weightError === null && this.printError === null);
  }

  createGoldPrice(): GoldPrice{
    if(this.validateForm('download')){
      var goldPrice : GoldPrice = new GoldPrice();
      goldPrice.rate = this.calculatorForm.value.rate;
      goldPrice.weight = this.calculatorForm.value.weight;
      goldPrice.discount = this.calculatorForm.value.discount === null ? 0 : this.calculatorForm.value.discount;
      goldPrice.price = this.calculatorForm.value.price;
      return goldPrice;
    }
    return null;
  }
  calculate(){
    if(this.validateForm('calculate')){
      var price = this.calculatorForm.value.rate * this.calculatorForm.value.weight;
      if(this.usertype === 'privileged'){
        price = price - (this.calculatorForm.value.discount * price)/100;
      }
      this.calculatorForm.get("price").setValue(price);
    }
  }

  download(event: string){
    var goldPrice = this.createGoldPrice();
    if(goldPrice != null){
      this.service.download(goldPrice).subscribe(
        (response: any) => {
          var blob = new Blob([response.body], { type: "text/plain" });
          var url = window.URL.createObjectURL(blob);
          if(event === 'S') window.open(url);
          if(event === 'F') this.printToFile(url, response.headers.get('content-disposition').split("=")[1]);
          if(event === 'P') alert('Method not implemented.');
        }
      );
    }
  }

  printToFile(url: string, filename: string){
    var link = document.createElement('a');
    link.href = url;
    link.download = filename;
    link.click();
    setTimeout(() => window.URL.revokeObjectURL(url), 400);
  }

  logout(){
    this.service.logout();
  }
}
