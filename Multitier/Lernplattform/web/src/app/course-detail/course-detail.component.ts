import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from "@angular/router";

import { CoursesService, Course } from '../api/courses/index'

@Component({
  selector: 'app-course-detail',
  templateUrl: './course-detail.component.html',
  styleUrls: ['./course-detail.component.css'],
  providers: [CoursesService]
})
export class CourseDetailComponent implements OnInit {

  course: Course = {};

  constructor(private coursesService: CoursesService,
              private route: ActivatedRoute) { }

  ngOnInit() {
    this.loadCourse();
  }

  private loadCourse() {
    this.route.params.subscribe( params =>
      this.coursesService.getCourse(params['courseId']).subscribe(
        course => {
          this.course = course;
        },
        error => {
          console.log(error);
        }));
  }

}
