import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportarProblemaComponent } from './reportar-problema.component';

describe('ReportarProblemaComponent', () => {
  let component: ReportarProblemaComponent;
  let fixture: ComponentFixture<ReportarProblemaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReportarProblemaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReportarProblemaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
