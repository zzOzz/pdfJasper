var pdf = require('./lib/pdfJasper')
var express = require('express')

var app = express()


app.get('/pdf', function (req, res, next) {
  //beware of the datatype of your parameter.
  var id = parseInt(req.query.id, 10)
  var pdf = pdf.pdf(report)
  console.log('done')
  res.set({
    'Content-type': 'application/png',
    'Content-Length': pdf.length
  })
  res.send(pdf)
})

app.listen(3000)
