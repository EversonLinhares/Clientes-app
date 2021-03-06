import { Component, OnInit } from '@angular/core';
import { Router , ActivatedRoute, Params } from '@angular/router';
import { Cliente } from '../cliente';
import { ClientesService } from '../../clientes.service';
import { IfStmt } from '@angular/compiler';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-clientes-form',
  templateUrl: './clientes-form.component.html',
  styleUrls: ['./clientes-form.component.css']
})
export class ClientesFormComponent implements OnInit {
  
  cliente: Cliente;
  success: boolean = false;
  errors: String[];
  id: number;
  constructor( 
    private service: ClientesService,
    private router: Router,
    private activatedRoute : ActivatedRoute ) {
    this.cliente = new Cliente();
    
   }

  ngOnInit(): void {
    let params: Observable<Params> = this.activatedRoute.params;
    params.subscribe( urlParams =>{
      this.id = urlParams['id'];
      if(this.id){
        this.service
         .getClienteById(this.id)
         .subscribe( response => this.cliente = response,
          errorResponse => this.cliente = new Cliente()
         )
      }
    })
  }

  voltar(){
     this.router.navigate(['/clientes/lista'])
  }

  onSubmit(){
    if(this.id){
      console.log('1')
        this.service
        .atualizar(this.cliente)
        .subscribe(response => {
          this.success = true;
          this.errors = null;
        }, errorResponse => {
          this.errors = ['Erro ao atualizar o cliente.']
          setTimeout(()=> {
            this.router.navigate(['/clientes/lista'])
          }, 2000)
        }
        )
    }else{
    }
     this.service
     .salvar(this.cliente)
     .subscribe( response =>{
      console.log('2')
       this.success= true;
       this.errors = null;
       setTimeout(() => {
         this.router.navigate(['/clientes/lista']);
       }, 2000)
      }, errorResponse =>{
        this.success = false;
        console.log('3')
        this.errors = errorResponse.error.errors;
        setTimeout(()=> {
          this.ngOnInit()
          this.errors = null
        }, 2000);
      })
      
  }

}
