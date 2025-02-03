import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TrasladosProgramadoComponent } from './traslados-programado.component';

describe('TrasladosProgramadoComponent', () => {
  let component: TrasladosProgramadoComponent;
  let fixture: ComponentFixture<TrasladosProgramadoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TrasladosProgramadoComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TrasladosProgramadoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
