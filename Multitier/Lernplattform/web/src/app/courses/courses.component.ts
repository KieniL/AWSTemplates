import { Component, OnInit } from '@angular/core';

import { CoursesService, CourseOverview } from '../api/courses/index'

@Component({
  selector: 'app-courses',
  templateUrl: './courses.component.html',
  styleUrls: ['./courses.component.css'],
  providers: [CoursesService]
})
export class CoursesComponent implements OnInit {

  courses: CourseOverview[] = [];

  constructor(private coursesService: CoursesService) {}

  ngOnInit() {
    this.loadCourses();
  }

  loadCourses(){
    this.coursesService.getCourses(null, false).subscribe(
      courses => {
        this.courses = courses;
      },
      error => {
        console.log(error);
      });
  }

}
