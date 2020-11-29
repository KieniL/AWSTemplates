import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CourseRestoreComponent } from './course-restore.component';

describe('CourseRestoreComponent', () => {
  let component: CourseRestoreComponent;
  let fixture: ComponentFixture<CourseRestoreComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CourseRestoreComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CourseRestoreComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
