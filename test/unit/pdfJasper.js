/*eslint no-unused-vars: "off" */

import * as pdfJasper from '../../src/pdfJasper'
var jasper = require('node-jasper')({
  path: '../../jasperreports-5.6.1',
  reports: {
    hw: {
      jrxml: '../../src/reports/CarteCumul.jrxml',
      conn: 'in_memory_json'
    }
  },
  debug: 'ERROR',
  java: ['-Djava.awt.headless=true']
})

jasper.ready(function () {
  describe('pdfJasper', () => {
    describe('generateReport', () => {
      var file = './test/mocks/fakedata.json'
      var fs = require('fs')
      var fakedata = JSON.parse(fs.readFileSync(file, 'utf8'))
      pdfJasper.getReport(2, fakedata, jasper).then(
        function (report) {
          // console.log('###final' + report.png)
          console.log(report)
          // pdfJasper.getPNGReport(2, report.pdf)
        })
    })
  })
})
