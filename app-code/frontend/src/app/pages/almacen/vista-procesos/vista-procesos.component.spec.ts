import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VistaProcesosComponent } from './vista-procesos.component';

describe('VistaProcesosComponent', () => {
  let component: VistaProcesosComponent;
  let fixture: ComponentFixture<VistaProcesosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VistaProcesosComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(VistaProcesosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
