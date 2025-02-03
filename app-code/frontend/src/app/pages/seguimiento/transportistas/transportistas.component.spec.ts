import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TransportistasComponent } from './transportistas.component';

describe('TransportistasComponent', () => {
  let component: TransportistasComponent;
  let fixture: ComponentFixture<TransportistasComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TransportistasComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TransportistasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
