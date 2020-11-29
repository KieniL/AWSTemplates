import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CoursesService, CourseOverview } from '../api/courses/index';
import { GetSchemasService, Schema} from '../api/database/index';

@Component({
  selector: 'app-course-create',
  templateUrl: './course-create.component.html',
  styleUrls: ['./course-create.component.css'],
  providers: [CoursesService, GetSchemasService]
})
export class CourseCreateComponent implements OnInit {

  newCourseForm: FormGroup;

  schemas: Schema[];

  selectedschema:any;

  constructor(private courseService: CoursesService,
              private schemaService: GetSchemasService,
              private router: Router,
              private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.loadSchemas();
    this.newCourseForm = this.formBuilder.group({
      name: ['', Validators.required],
      selectedschema: ['', Validators.required]
    });
  }

  onSubmit() {
    let course:CourseOverview = {
      name: this.f.name.value,
      schema: this.f.selectedschema.value
    };
    this.courseService.postCourses(course).subscribe(
      success => {
        this.router.navigate(['/']);
      },
      error => {
        console.log(error);
      });
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

  get f() { return this.newCourseForm.controls; }
}
