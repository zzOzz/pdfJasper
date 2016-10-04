'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.generateReport = generateReport;
exports.getReport = getReport;
exports.cleanCache = cleanCache;
exports.getPNGReport = getPNGReport;
exports.getPDFReport = getPDFReport;
exports.getPDF = getPDF;
exports.getPng = getPng;
exports.on = on;
var events = require('events');
var fs = require('fs');
var gs = require('./lib/gs');
var extend = require('util')._extend;
var debug = require('debug')('pdfJasper');
var eventEmitter = new events.EventEmitter();
var jasper;
var crypto = require('crypto');
var config = require('config');
// var jasper = require('node-jasper')({
//   path: '../jasperreports-5.6.1',
//   reports: {
//     hw: {
//       jrxml: './reports/CarteCumul.jrxml',
//       conn: 'in_memory_json'
//     }
//   },
//   debug: 'TRACE',
//   java: ['-Djava.awt.headless=true']
// })

var pdfJasper = {
  greet: function greet() {
    return 'hello';
  }
};
exports.default = pdfJasper;
function generateReport(id, fakedata) {
  fakedata = JSON.parse(unescape(encodeURIComponent(JSON.stringify(fakedata, 4))));
  jasper.ready(function () {
    var report = {
      report: 'hw',
      data: {
        id: id,
        SHOW_BACKGROUND: 'yes'
      },
      dataset: fakedata
    };
    var pdf = jasper.pdf(report);
    eventEmitter.emit('pdfReady', pdf);
    gs().batch().nopause().device('pngalpha')
    // .input('test.pdf')
    .input(pdf).output('./' + id + '-%d.png').r(600).spawn();
    getPng(id, 1, []);
  });
}
function getReport(id, fakedata, printOptions, jasper) {
  this.jasper = jasper;
  fakedata = JSON.parse(unescape(encodeURIComponent(JSON.stringify(fakedata, 4))));
  var p1 = new Promise(function (resolve, reject) {
    console.log('Creating PDF report without background 1@@@@');
    // JSON.stringify(fakedata))
    var report = {
      report: 'hw',
      data: {
        id: id,
        SHOW_BACKGROUND: 'no'
      },
      dataset: fakedata
    };

    var hash = crypto.createHash('sha256');
    // var hashFileName = './cache/' + hash.update(JSON.stringify(report.dataset)).digest('hex')
    var hashFileName = config.get('cache.folder') + hash.update(JSON.stringify(report.dataset)).digest('hex');
    var pdf;
    var pngFile = hashFileName + '-' + ((report.data.id === 0 ? '' : report.data.id) + 1) + '.png';
    fs.exists(pngFile, function (exists) {
      console.log(pngFile + ' exists:' + exists + ', ' + printOptions.pdf);
      if (!printOptions.pdf && exists) {
        console.log('cache cache');
        var returnValue = extend({}, report);
        resolve(getPNGReport(returnValue));
      } else {
        fs.exists(hashFileName + '-print.pdf', function (exists) {
          console.log(exists ? hashFileName + '-print.pdf' + ' is in cache' : hashFileName + '.pdf:' + 'no cache!');
          if (exists) {
            // console.log('print-pdf exists in cache')
            fs.readFile(hashFileName + '-print.pdf', function read(err, data) {
              pdf = data;
              // console.log('cache@@@@')
              var returnValue = extend({}, report);
              console.log('options: ' + printOptions.pdf);
              if (printOptions.pdf === 'true') {
                extend(returnValue, { pdf: 'pdf' });
                console.log('include pdf:');
              }
              fs.readFile(hashFileName + '.pdf', function read(err, data) {
                // report.data.SHOW_BACKGROUND = 'yes'
                // pdf = jasper.pdf(report)
                // if (printOptions.pdf)
                extend(returnValue, { pdf_with_background: data });
                if (printOptions.pdf === 'true') {
                  resolve(getPDFReport(returnValue));
                } else {
                  resolve(getPNGReport(returnValue));
                }
              });
            });
          } else {
            pdf = jasper.pdf(report);
            fs.writeFile(hashFileName + '-print.pdf', pdf, function (err, data) {
              // console.log('print-pdf written in cache')
              console.log(printOptions);
              var returnValue = extend({}, report);
              if (printOptions.pdf === 'true') {
                extend(returnValue, { pdf: 'data' });
                console.log('include pdf:');
              }
              report.data.SHOW_BACKGROUND = 'yes';
              pdf = jasper.pdf(report);
              // if (printOptions.pdf)
              extend(returnValue, { pdf_with_background: pdf });
              if (printOptions.pdf === 'true') {
                resolve(getPDFReport(returnValue));
              } else {
                resolve(getPNGReport(returnValue));
              }
            });
          }
        });
      }
    });
  });
  return p1;
}

function cleanCache(hashFileName) {
  var glob = require('glob');
  console.log('cleann started ' + hashFileName + '*.pdf');
  glob(hashFileName + '*.pdf', {}, function (er, files) {
    console.log(files);
    files.forEach(function (value, index) {
      console.log('clean:' + files[index]);
      fs.unlink(files[index], function (err) {
        console.log(err);
      });
    });
    console.log('clean ended');
  });
}

/*eslint no-unused-vars: "off" */
/*eslint handle-callback-err: "off" */
function getPNGReport(report) {
  var p1 = new Promise(function (resolve, reject) {
    var hash = crypto.createHash('sha256');
    // var hashFileName = './cache/' + hash.update(JSON.stringify(report.dataset)).digest('hex')
    var hashFileName = config.get('cache.folder') + hash.update(JSON.stringify(report.dataset)).digest('hex');
    console.log('filename:' + hashFileName);
    fs.exists(hashFileName + '-' + report.data.id + '.png', function (exists) {
      if (!exists) {
        fs.writeFile(hashFileName + '.pdf', report.pdf_with_background, function (err, data) {
          var gsProcess = gs().batch().nopause().device('pngalpha').output(hashFileName + '-%d.png').input(hashFileName + '.pdf').r(600).exec(function (err, data) {
            getPng(hashFileName, report.dataset, 1, []);
            cleanCache(hashFileName);
          });
        });
      } else {
        getPng(hashFileName, report.dataset, 1, []);
      }
    });
    eventEmitter.once('pngReady', function (pngArray) {
      var pngBase64 = [];
      for (var key in pngArray) {
        pngBase64.push('data:image/png;base64,' + pngArray[key].toString('base64'));
        extend(report.dataset[key], { pngBase64: 'data:image/png;base64,' + pngArray[key].toString('base64') });
      }
      console.log((report.dataset[0].pngBase64 + ' ').length);
      resolve(report);
    });
  });
  return p1;
}

function getPDFReport(report) {
  var p1 = new Promise(function (resolve, reject) {
    var hash = crypto.createHash('sha256');
    // var hashFileName = './cache/' + hash.update(JSON.stringify(report.dataset)).digest('hex')
    var hashFileName = config.get('cache.folder') + hash.update(JSON.stringify(report.dataset)).digest('hex');
    console.log('filename:' + hashFileName);
    fs.exists(hashFileName + '-' + report.data.id + '.pdf', function (exists) {
      if (!exists) {
        fs.writeFile(hashFileName + '.pdf', report.pdf_with_background, function (err, data) {
          var gsProcess = gs().batch().nopause().device('pdfwrite').output(hashFileName + '-%d.pdf').input(hashFileName + '-print.pdf').exec(function (err, data) {
            console.log('&&&&&&' + hashFileName);
            getPDF(hashFileName, report.dataset, 1, []);
            // cleanCache(hashFileName)
          });
        });
      } else {
        getPDF(hashFileName, report.dataset, 1, []);
      }
    });
    eventEmitter.once('pdfReady', function (pdfArray) {
      var pdfBase64 = [];
      for (var key in pdfArray) {
        pdfBase64.push('data:application/pdf;base64,' + pdfArray[key].toString('base64'));
        extend(report.dataset[key], { pdfBase64: 'data:application/pdf;base64,' + pdfArray[key].toString('base64') });
      }
      console.log((report.dataset[0].pdfBase64 + ' ').length);
      // console.log(report.dataset[0].pdfBase64)
      resolve(report);
    });
  });
  return p1;
}

function getPDF(hashFileName, dataset, index, array) {
  fs.readFile(hashFileName + '-' + index + '.pdf', function read(err, data) {
    if (err) {
      console.log('size:' + array.length);
      eventEmitter.emit('pdfReady', array);
    } else {
      array.push(data);
      console.log('&' + index);
      return getPDF(hashFileName, dataset, index + 1, array);
    }
  });
}

function getPng(hashFileName, dataset, index, array) {
  fs.readFile(hashFileName + '-' + index + '.png', function read(err, data) {
    if (err) {
      console.log('size:' + array.length);
      eventEmitter.emit('pngReady', array);
    } else {
      array.push(data);
      return getPng(hashFileName, dataset, index + 1, array);
    }
  });
}

function on(eventname, listener) {
  eventEmitter.once(eventname, listener);
}

// export function writePng (eventname, listener) {
//   fs.writeFile(hashFileName + '.pdf', report.pdf_with_background, function (err, data) {
//     // console.log('4@@@@')
//     var gsProcess = gs().batch().nopause().device('pngalpha').output(hashFileName + '-%d.png').input(hashFileName + '.pdf').r(600).exec(function (err, data) {
//       // console.log('5@@@@')
//       getPng(hashFileName, report.data.id, 1, [])
//     })
//   })
// }
console.log('start');