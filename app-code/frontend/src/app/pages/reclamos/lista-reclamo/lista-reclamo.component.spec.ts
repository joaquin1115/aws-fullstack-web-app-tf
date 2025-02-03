import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListaReclamoComponent } from './lista-reclamo.component';

describe('ListaReclamoComponent', () => {
  let component: ListaReclamoComponent;
  let fixture: ComponentFixture<ListaReclamoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListaReclamoComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ListaReclamoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
