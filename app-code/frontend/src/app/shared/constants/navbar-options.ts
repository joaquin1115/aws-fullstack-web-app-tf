export default [
  {
    routeLink: 'pedidos',
    icon: 'fa fa-cart-plus',
    label: 'Pedidos'
  },
  {
    routeLink: 'seguimiento',
    icon: 'fa fa-map-marked',
    label: 'Seguimiento',
    items: [
      {
        routeLink: 'vehiculos',
        label: 'Vehículos'
      },
      {
        routeLink: 'rutas',
        label: 'Rutas'
      },
      {
        routeLink: 'transportistas',
        label: 'Transportistas'
      }
    ]
  },
  {
    routeLink: 'almacen',
    icon: 'fa fa-box',
    label: 'Almacén'
  },
  {
    routeLink: 'control',
    icon: 'fa fa-wrench',
    label: 'Control'
  },
  {
    routeLink: 'reportes',
    icon: 'fa fa-file-contract',
    label: 'Reportes'
  },
  {
    routeLink: 'reclamos',
    icon: 'fa fa-flag',
    label: 'Reclamos'
  },
  {
    routeLink: 'home',
    icon: 'fa fa-home',
    label: 'Home'
  },
];
