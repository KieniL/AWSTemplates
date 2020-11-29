import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CreateschemaService, Schema, TablescreatedefaultService, TablemanageService, FileContent } from '../api/database/index';

@Component({
  selector: 'app-schema-create',
  templateUrl: './schema-create.component.html',
  styleUrls: ['./schema-create.component.css'],
  providers: [CreateschemaService, TablescreatedefaultService, TablemanageService]
})
export class SchemaCreateComponent implements OnInit {

  newSchemaForm: FormGroup;

  file:any;
  selectedType = '1';
  result = "";


  submitted = false;
  loading = false;
  constructor(private schemaservice: CreateschemaService,
              private tabledefaultservice: TablescreatedefaultService,
              private tablemanageservice: TablemanageService,
              private router: Router,
              private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.newSchemaForm = this.formBuilder.group({
      name: ['', Validators.required],
      defaultcheckbox: [''],
      fileupload: [''],
    });
  }

  onChange(event) {
    this.selectedType = event.target.value;
  }


  fileChanged(e) {
      this.file = e.target.files[0];
  }



  uploadDocument(file) {
    let fileReader = new FileReader();
    fileReader.onload = (e) => {
      this.storeResults(fileReader.result)
    }

    fileReader.readAsText(this.file);

  }

  storeResults(result) {
    this.result = result;
  }

  onSubmit() {
    this.submitted = true;
    this.loading = true;
    if (this.f.name.value.length > 0){
      let schema:Schema = {
        name: this.f.name.value
      };
      this.schemaservice.schemacreate(schema).subscribe(
        success => {
          if (success.response ==  schema.name){
            if( this.selectedType == '1'){
              if (this.f.defaultcheckbox.value == true){
                this.tabledefaultservice.tablescreatedefault(schema.name).subscribe(
                  error => {
                    console.log(error);
                  }
                )
              }
            }else{
              if (this.result.length > 0){
                let filecontent:FileContent = {
                  content: this.result
                }
                this.tablemanageservice.tablemanage(schema.name, filecontent).subscribe(
                  error => {
                    console.log(error);
                  }
                )
              }
            }

            this.router.navigate(['/']);
          }else
          {
            if(success.response.match(/schema ".+" already exists/)){
              console.log("schema already exists");
            }else{
              console.log(success.response);
            }

          }
        },
        error => {
          console.log(error);
        });
    }

  }

  get f() { return this.newSchemaForm.controls; }
}
