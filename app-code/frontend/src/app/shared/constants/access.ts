/*
  ( 1, 'I', 'A',   'Almacen', '20100154308','SAN FERNANDO S.A.', '2002-06-01'), 
  ( 2, 'I', 'A' ,'Atencion al cliente' , '20100154308', 'SAN FERNANDO S.A.', '2002-06-01'), 
  ( 3, 'I', 'A' ,'Produccion' , '20100154308', 'SAN FERNANDO S.A.', '2002-06-01'), 
  ( 4, 'I', 'A' ,'Mantenimiento' , '20100154308', 'SAN FERNANDO S.A.', '2002-06-01'), 

  ( 1, 'Supervisor de Almacén'),
  ( 2, 'Encargado de almacén'),
  ( 3, 'Asistente operativo'),
  ( 4, 'Almacenero'),
  ( 5, 'Transportista'),
  ( 6, 'Técnico de almacén'),
  ( 7, 'Supervisor de producción'),
  ( 8, 'Técnico de mantenimiento');

  {
    "dni": "77688137",
    "area": "Almacen",
    "cargo": "Gerente de Almacén",
    "representante": true,
    "idEmpleado": 1
  }
*/

export interface Access {
  modulo?: string;
  areas?: string[];
  users?: string[];
}

export const ACCESO_MODULO = {
  almacen:
  {
    area: ["almacen", "atencion al cliente", "produccion", "mantenimiento"],
    users: ["supervisor de almacén", "asistente operativo"]
  },
  reportes:
  {
    area: [],
    users: []
  },
  home:
  {
    area: ["almacen", "atencion al cliente", "produccion", "mantenimiento"],
    users: []
  },
  seguimiento:
  {
    area: [],
    users: []
  },
  control:
  {
    area: [],
    users: []
  },
  pedidos:
  {
    area: ["almacen", "atencion al cliente", "produccion", "mantenimiento"],
    users: ["supervisor de almacén", "asistente operativo"]
  },
}

// export const ACCESO_MODULO: Access[] = [
//   {
//     modulo: "almacen",
//     areas: ["Almacen", "Atencion al cliente", "Produccion", "Mantenimiento"],
//     users: ["Supervisor de Almacén", "Asistente operativo"]
//   },
//   {
//     modulo: "reportes",
//     areas: [],
//     users: []
//   },
//   {
//     modulo: "home",
//     areas: ["Almacen", "Atencion al cliente", "Produccion", "Mantenimiento"],
//     users: []
//   },
//   {
//     modulo: "seguimiento",
//     areas: [],
//     users: []
//   },
//   {
//     modulo: "control",
//     areas: [],
//     users: []
//   },
//   {
//     modulo: "pedidos",
//     areas: [],
//     users: []
//   },
//   {
//     modulo: "reclamos",
//     areas: [],
//     users: []
//   }
// ]
