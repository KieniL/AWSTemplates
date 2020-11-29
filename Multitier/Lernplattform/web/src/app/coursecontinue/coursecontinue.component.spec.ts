import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CourseContinueComponent } from './coursecontinue.component';

describe('CourseContinueComponent', () => {
  let component: CoursesComponent;
  let fixture: ComponentFixture<CourseContinueComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CourseContinueComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CourseContinueComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
