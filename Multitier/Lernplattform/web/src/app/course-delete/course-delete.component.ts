import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CoursesService, CourseOverview } from '../api/courses/index';

@Component({
  selector: 'app-course-delete',
  templateUrl: './course-delete.component.html',
  styleUrls: ['./course-delete.component.css'],
  providers: [CoursesService]
})
export class CourseDeleteComponent implements OnInit {

  deleteCourseForm: FormGroup;
  courses: CourseOverview[] = [];

  selectedcourse:any;

  loading = false;
  constructor(private coursesService: CoursesService,
              private router: Router,
              private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.loadCourses();

    this.deleteCourseForm = this.formBuilder.group({
      selectedcourse: ['', Validators.required]
    });
  }

  onSubmit() {
    this.loading = true;
    this.coursesService.deleteCourse(this.f.selectedcourse.value).subscribe(
      success => {
        this.router.navigate(['/']);
      },
      error => {
        console.log(error);
      });
  }

  get f() { return this.deleteCourseForm.controls; }

  loadCourses(){
    this.coursesService.getCourses(localStorage.getItem('user'), false).subscribe(
      courses => {
        this.courses = courses;
      },
      error => {
        console.log(error);
      });
    }
}
