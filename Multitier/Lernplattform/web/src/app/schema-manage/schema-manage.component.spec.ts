import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SchemaManageComponent } from './schema-manage.component';

describe('CourseCreateComponent', () => {
  let component: CourseCreateComponent;
  let fixture: ComponentFixture<SchemaManageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SchemaManageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SchemaManageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
