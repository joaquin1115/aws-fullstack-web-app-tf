import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegistroOperacionComponent } from './registro-operacion.component';

describe('RegistroOperacionComponent', () => {
  let component: RegistroOperacionComponent;
  let fixture: ComponentFixture<RegistroOperacionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegistroOperacionComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(RegistroOperacionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
