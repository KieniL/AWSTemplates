import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { GetSchemasService, Schema, TablemanageService, FileContent } from '../api/database/index';
import { templateJitUrl } from '@angular/compiler';

@Component({
  selector: 'app-schema-manage',
  templateUrl: './schema-manage.component.html',
  styleUrls: ['./schema-manage.component.css'],
  providers: [TablemanageService, GetSchemasService]
})
export class SchemaManageComponent implements OnInit {

  manageSchemaForm: FormGroup;

  file:any;
  result = "";
  selectedschema:any;
  schemas: Schema[];


  loading = false;
  constructor(private tablemanageservice: TablemanageService,
              private schemaService: GetSchemasService,
              private router: Router,
              private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.loadSchemas();
    this.manageSchemaForm = this.formBuilder.group({
      selectedschema: ['', Validators.required],
      fileupload: [''],
    });
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
    this.loading = true;
    if (this.f.selectedschema.value.length > 0){
      let schema:Schema = {
        name: this.f.selectedschema.value
      };
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
        this.router.navigate(['/']);
        }
  }

  loadSchemas(){
    this.schemaService.getSchemas(localStorage.getItem('user')).subscribe(
      schemas => {
        this.schemas = schemas;
      },
      error => {
        console.log(error);
      });
    }

  get f() { return this.manageSchemaForm.controls; }
}
