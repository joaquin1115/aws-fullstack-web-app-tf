import { ReporteOperacionResponse, ReporteReclamoUrgenciaResponse } from "../../core/models/response/reporte-responses";

interface datasetHorBar {
  label?: string;
  backgroundColor?: any;
  borderColor?: any;
  data?: [];
}

export interface chartHorBar {
  data: any;
  options: any;
}

export interface ChartContent {
  dataX: any[];
  dataY: any[];
}

export interface EChartOption {
  title: {
    left: 'center',
    text: string,
    top: '-3%',
  },
  tooltip: {
    trigger: 'axis',
    axisPointer: {
      type: 'shadow'
    }
  },
  legend: {
    data: String[],
  }
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true
  },
  toolbox: {
    feature: {
      saveAsImage: {}
    }
  },
  xAxis: {
    type: 'category',
    boundaryGap: true,
    data: [],
  }
  yAxis: {
    type: 'value',
  },
  series: []
}

export function getArrayDataUrgenciaTipo(responses: ReporteReclamoUrgenciaResponse[], labelsY: string[], labelX: string): ChartContent {
  const chartContent: ChartContent = {
    dataX: [],
    dataY: []
  }
  labelsY.forEach((label: string, index: number) => {
    chartContent.dataY.push(
      {
        name: label,
        type: 'bar',
        stack: 'Group',
        emphasis: {
          focus: 'series'
        },
        data: []
      }
    );
    responses.forEach((response: ReporteReclamoUrgenciaResponse) => {
      chartContent.dataY[index].data.push((response as any)[label])
    });
  })

  responses.forEach((response: ReporteReclamoUrgenciaResponse) => {
    chartContent.dataX.push((response as any)[labelX]);
  })
  return chartContent;
}

export function getEChartUrgenciaTipo(responses: any, labelsY: string[], labelX: string, title: string, legend: string) {
  const chartContent = getArrayDataUrgenciaTipo(responses, labelsY, labelX);
  const echartOption: EChartOption = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    legend: {

    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    toolbox: {
      feature: {
        saveAsImage: {}
      }
    },
    xAxis: {
      data: chartContent.dataX,
      type: 'category',
      boundaryGap: true,
    },
    yAxis: {
      type: 'value',
    },
    series: [
      ...chartContent.dataY
    ] as any
  } as any;

  return echartOption;
}

export function getArrayData(responses: any, labelY: string, labelX: string): ChartContent {
  const chartContent: ChartContent = {
    dataX: [],
    dataY: []
  }

  chartContent.dataY = responses?.map((response: any) => {
    return (response as any)[labelY];
  })
  chartContent.dataX = responses?.map((response: any) => {
    return (response as any)[labelX];
  })

  return chartContent;
}

export function getEchart(responses: any, labelY: string, labelX: string, title: string, legend: string, tipo: string, display: string) {
  const chartContent = getArrayData(responses, labelY, labelX);
  const echartOption: EChartOption = {
    title: {
      left: 'center',
      text: title,
      top: '-10%',
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    legend: {
      data: [
        legend
      ]
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    toolbox: {
      feature: {
        saveAsImage: {}
      }
    },
    xAxis: {
      boundaryGap: true,
      data: display === "vertical" ? chartContent.dataX : null,
      type: display === "vertical" ? 'category' : 'value',
    },
    yAxis: {
      data: display === "vertical" ? null : chartContent.dataX,
      type: display === "vertical" ? 'value' : 'category',
    },
    series: [
      {
        name: legend,
        type: tipo,
        stack: 'Total',
        data: chartContent.dataY,
      },
    ] as any
  } as any;

  return echartOption;
}

export function getDataSetHorBar(responses: any, label: string, data: string, title: string, colorBar: string, colorBor: string) {
  const documentStyle = getComputedStyle(document.documentElement);
  const textColor = documentStyle.getPropertyValue('--text-color');
  const textColorSecondary = documentStyle.getPropertyValue('--text-color-secondary');
  const surfaceBorder = documentStyle.getPropertyValue('--surface-border');

  let datasets = []
  datasets.push({
    label: title,
    backgroundColor: documentStyle.getPropertyValue(`--${colorBar}-500`),
    borderColor: documentStyle.getPropertyValue(`--${colorBor}-500`),
    data: responses?.map((response: any) => {
      return (response as any)[data];
    })
  } as datasetHorBar)

  let labels = responses?.map((response: any) => {
    return (response as any)[label]
  })

  let options = {
    indexAxis: 'y',
    maintainAspectRatio: false,
    aspectRatio: 1.3,
    plugins: {
      legend: {
        labels: {
          color: textColor
        }
      }
    },
    scales: {
      x: {
        ticks: {
          color: textColorSecondary,
          font: {
            weight: 500
          }
        },
        grid: {
          color: surfaceBorder,
          drawBorder: false
        }
      },
      y: {
        ticks: {
          color: textColorSecondary
        },
        grid: {
          color: surfaceBorder,
          drawBorder: false
        }
      }
    }
  };

  const dataRes: chartHorBar = {
    data: {
      labels,
      datasets
    },
    options
  }

  return dataRes;
}

