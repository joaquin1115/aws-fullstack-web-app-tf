export interface Nacionalidad {
  tipo: string;
  id: number;
}

export interface EstadoCivil {
  tipo: string;
  id: number;
}

export interface Genero {
  tipo: string;
  id: number;
}

export const PERSONA_NACIONALIDAD: Nacionalidad[] = [
  { tipo: "Peruano", id: 1 },
];

export const PERSONA_ESTADO_CIVIL: EstadoCivil[] = [
  { tipo: "Soltero", id: 1 },
  { tipo: "Casado", id: 2 },
  { tipo: "Conviviente", id: 3 },
  { tipo: "Divorciado", id: 4 },
  { tipo: "Viudo", id: 5 },
];

export const PERSONA_GENERO: Genero[] = [
  { tipo: "Masculino", id: 1 },
  { tipo: "Femenino", id: 2 },
];