import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CoursesService, CourseOverview, Course } from '../api/courses/index';

@Component({
  selector: 'app-course-restore',
  templateUrl: './course-restore.component.html',
  styleUrls: ['./course-restore.component.css'],
  providers: [CoursesService]
})
export class CourseRestoreComponent implements OnInit {

  restoreCourseForm: FormGroup;
  courses: CourseOverview[] = [];

  selectedcourse:any;

  constructor(private coursesService: CoursesService,
              private router: Router,
              private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.loadCourses();

    this.restoreCourseForm = this.formBuilder.group({
      selectedcourse: ['', Validators.required]
    });
  }

  onSubmit() {
    let course:Course = {
      id: this.f.selectedcourse.value,
    };
    this.coursesService.restoreCourse(this.f.selectedcourse.value).subscribe(
      success => {
        this.router.navigate(['/']);
      },
      error => {
        console.log(error);
      });
  }

  get f() { return this.restoreCourseForm.controls; }

  loadCourses(){
    this.coursesService.getCourses(localStorage.getItem('user'),true).subscribe(
      courses => {
        this.courses = courses;
      },
      error => {
        console.log(error);
      });
    }
}
